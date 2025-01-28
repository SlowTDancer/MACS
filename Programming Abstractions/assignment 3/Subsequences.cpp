/*
 * File: Subsequences.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Subsequences problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include "simpio.h"
#include "console.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */

//some methods.
void get_entries(string& text, string& subsequence);
bool isSubsequence(string text, string subsequence);
void write_answer(string& ans, bool& answer);

// main function.
int main() {
	string text, subsequence, ans;
	get_entries(text, subsequence);
	bool answer = isSubsequence(text, subsequence);
	write_answer(ans, answer);
	cout << subsequence << ans << " a subsequence of " << text << endl;
    return 0;
}

//costumer enters values.
void get_entries(string& text, string& subsequence){
	while(true){
		text = getLine("please enter text: ");
		subsequence = getLine("please enter subsequnce: ");
		if(text.length() >= subsequence.length()) break;
		cout << "please enter valid values!" << endl;
	}
}

//checks if text is subsequence is an entry.
bool isSubsequence(string text, string subsequence){
	if(subsequence.length()==0) return true;
	if(text.length() < subsequence.length())return false;
	if(text[0]==subsequence[0]){
		subsequence = subsequence.substr(1);
	}
	text = text.substr(1);
	return isSubsequence(text, subsequence);
}

//writes answer.
void write_answer(string& ans, bool& answer){
	if(answer){
		ans = " is";
	}else{
		ans = " is not";
	}	
}