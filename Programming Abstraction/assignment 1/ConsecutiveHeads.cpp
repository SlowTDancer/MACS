/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include <time.h>

using namespace std;
/*there is an equal probability that integer is even, so 
it's probability is equal to coinflip. if it will get three 
consucitive heads it stops and writes message.
*/
int main() {
	srand(time(NULL));
    int countHeads = 0;
	int count =0;
	while(true){
		int num = rand();
		if(num%2==0){
			cout<<"heads"<<endl;
			countHeads++;
		}else{
			cout<<"tails"<<endl;
			countHeads=0;
		}
		count++;
		if(countHeads==3){
			cout<<"It took "<<count<<" flips to get 3 consucitive heads."<<endl;
			break;
		}
	}
    return 0;
}
