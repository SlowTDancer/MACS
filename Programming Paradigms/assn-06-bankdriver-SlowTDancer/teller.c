#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <inttypes.h>
#include <semaphore.h>

#include "teller.h"
#include "account.h"
#include "account.c"
#include "error.h"
#include "debug.h"

/*
 * deposit money into an account
 */
int
Teller_DoDeposit(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoDeposit(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);
  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  sem_wait(&account->account_lock);
  BranchID branch_index = AccountNum_GetBranchID(accountNum);
  sem_wait(&bank->branches[branch_index].branch_lock);
  Account_Adjust(bank,account, amount, 1);
  sem_post(&bank->branches[branch_index].branch_lock);
  sem_post(&account->account_lock);
  return ERROR_SUCCESS;
}

/*
 * withdraw money from an account
 */
int
Teller_DoWithdraw(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);
  DPRINTF('t', ("Teller_DoWithdraw(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);
  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  sem_wait(&account->account_lock);
  BranchID branch_index = AccountNum_GetBranchID(accountNum);
  sem_wait(&bank->branches[branch_index].branch_lock);
  if (amount > Account_Balance(account)) {
    sem_post(&bank->branches[branch_index].branch_lock);
    sem_post(&account->account_lock);
    return ERROR_INSUFFICIENT_FUNDS;
  }

  Account_Adjust(bank,account, -amount, 1);
  sem_post(&bank->branches[branch_index].branch_lock);
  sem_post(&account->account_lock);
  return ERROR_SUCCESS;
}

/*
 * do a tranfer from one account to another account
 */
int
Teller_DoTransfer(Bank *bank, AccountNumber srcAccountNum,
                  AccountNumber dstAccountNum,
                  AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoTransfer(src 0x%"PRIx64", dst 0x%"PRIx64
                ", amount %"PRId64")\n",
                srcAccountNum, dstAccountNum, amount));

  Account *srcAccount = Account_LookupByNumber(bank, srcAccountNum);
  if (srcAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  Account *dstAccount = Account_LookupByNumber(bank, dstAccountNum);
  if (dstAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  int updateBranch = Account_IsSameBranch(srcAccountNum, dstAccountNum);
  if(updateBranch){
    if(srcAccount->accountNumber < dstAccount->accountNumber){
      sem_wait(&srcAccount->account_lock);
      sem_wait(&dstAccount->account_lock);
    }else if(srcAccount->accountNumber > dstAccount->accountNumber){
      sem_wait(&dstAccount->account_lock);
      sem_wait(&srcAccount->account_lock);
    }else{
      return ERROR_SUCCESS;
    }
  }else{
    int src_branch_id = AccountNum_GetBranchID(srcAccountNum);
    int dst_branch_id = AccountNum_GetBranchID(dstAccountNum);
    if(src_branch_id > dst_branch_id){
      sem_wait(&(dstAccount->account_lock));
      sem_wait(&(srcAccount->account_lock));
      sem_wait(&(bank->branches[dst_branch_id].branch_lock));
      sem_wait(&(bank->branches[src_branch_id].branch_lock));
    }else{
      sem_wait(&(srcAccount->account_lock));
      sem_wait(&(dstAccount->account_lock));
      sem_wait(&(bank->branches[src_branch_id].branch_lock));
      sem_wait(&(bank->branches[dst_branch_id].branch_lock));
    }
  }
  if (amount > Account_Balance(srcAccount)) {
    sem_post(&srcAccount->account_lock);
    sem_post(&dstAccount->account_lock);
    if(!updateBranch){
      int src_branch_id = AccountNum_GetBranchID(srcAccountNum);
      int dst_branch_id = AccountNum_GetBranchID(dstAccountNum);
      sem_post(&(bank->branches[src_branch_id].branch_lock));
      sem_post(&(bank->branches[dst_branch_id].branch_lock));
    }
    return ERROR_INSUFFICIENT_FUNDS;
  }

  /*
   * If we are doing a transfer within the branch, we tell the Account module to
   * not bother updating the branch balance since the net change for the
   * branch is 0.
   */
  updateBranch = !updateBranch;
  Account_Adjust(bank, srcAccount, -amount, updateBranch);
  Account_Adjust(bank, dstAccount, amount, updateBranch);
  sem_post(&srcAccount->account_lock);
  sem_post(&dstAccount->account_lock);
  if(updateBranch){
      int src_branch_id = AccountNum_GetBranchID(srcAccountNum);
      int dst_branch_id = AccountNum_GetBranchID(dstAccountNum);
      sem_post(&(bank->branches[src_branch_id].branch_lock));
      sem_post(&(bank->branches[dst_branch_id].branch_lock));
    }
  return ERROR_SUCCESS;
}
