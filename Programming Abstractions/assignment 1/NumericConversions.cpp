/*
 * File: NumericConversions.cpp
 * ---------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Numeric Conversions problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Function prototypes */

string intToString(int n);
int stringToInt(string str);

/* Main program */

int main() {
	int num = getInteger("enter integer: ");
	string line = getLine("enter line: ");
	int intAns = stringToInt(line);
	string strAns = intToString(num);
	cout<<"string is: "<<strAns<<endl;
	cout<<"int is: "<<intAns<<endl;
    return 0;
}
/*this method turns int to string.
*/
string intToString(int n){
	if(n<0){
	return "-"+intToString(-n);
	}
	string s = "";
	if(n<10){
		s+= '0'+n%10;
		return s;
	}
	s = intToString(n/10);
	s+='0'+n%10;
	return s;
}
/*this method turns string to int.
*/
int stringToInt(string str){
	if(str=="-2147483648"){
	 return INT_MIN;
	}
	int size = str.length();
	if(size==1){
		return str[0]-'0';
	}
	if(str[0]=='-'){
		string newStr = str.substr(1);
		return -(stringToInt(newStr));
	}
	char a = str[size-1];
	int x = a-'0';
	string newStr = str.substr(0,size-1);
	return 10*stringToInt(newStr)+x;
}