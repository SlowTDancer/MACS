/*
 * File: Combinations.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Combinations problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;
/*it counts C(n,k) and user will enter them.
*/
int countCombos(int n,int k);
int main() {
	int n;
	int k;
	while(true){
		n = getInteger("please enter n: ");
		k = getInteger("please enter k: ");
		if(n>=k){
		break;
		}else{
		cout<<"enter valid values"<<endl;
		}
	}
    int answer = countCombos(n,k);
	cout<<answer<<endl;
    return 0;
}
	
int countCombos(int n,int k){
	if(n==k||k==0){
		return 1;
	}
	return countCombos(n-1,k-1)+countCombos(n-1,k);
}
