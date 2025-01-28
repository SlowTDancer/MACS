/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "tokenscanner.h"
#include "filelib.h"
#include "strlib.h"
#include "simpio.h"

using namespace std;

// constansts.
const double C0 = -15.59;
const double C1 = 0.39;
const double C2 = 11.8;

//instance variables.
int wordsCount = 0;
int sentencesCount = 0;
int syllablesCount =0;

// methods.
void readFile(ifstream& file);
void modifyScanner(TokenScanner& scanner);
void countThings(TokenScanner& scanner);
bool checkIfVowel(char ch);
int countSyllables(string s);
double calculateGrade();

// main function.
int main() {
	ifstream file;
	readFile(file);
	TokenScanner scanner(file);
	modifyScanner(scanner);
	countThings(scanner);
	double answer = calculateGrade();
	cout<<"grade is: "<<answer<<endl;
    return 0;
}

//this method reads file.
void readFile(ifstream& file){
	string name = getLine("enter file name: ");
	file.open(name.c_str());
	while(file.fail()){
		cout<<"enter valid filename"<<endl;
		readFile(file);
	}
}

//this method modifies scanner.
void modifyScanner(TokenScanner& scanner){
	scanner.ignoreWhitespace();
	scanner.addWordCharacters("'");
}

//this method count sentences, syllables and words.
void countThings(TokenScanner& scanner){
	while(scanner.hasMoreTokens()){
		string line = scanner.nextToken();
		if(isalpha(line[0])){
			wordsCount++;
			syllablesCount += countSyllables(line);
		}else if(line=="."||line=="!"||line=="?"){
			sentencesCount++;
		}
	}
	if(sentencesCount == 0 && wordsCount == 0){
		sentencesCount++;
		wordsCount++;
	}
}

//this method checks if character is vowel.
bool checkIfVowel(char ch){
	return ch=='a'||ch=='i'||ch=='e'||ch=='o'||ch=='u'||ch=='y';
}

//this method counts syllables in word.
int countSyllables(string s){
	int answer =0;
	int size = s.length();
	s = toLowerCase(s);
	if(checkIfVowel(s[0])){
	answer++;
	}
	for(int i=1;i<size;i++){
		if(checkIfVowel(s[i])&&!checkIfVowel(s[i-1])){
			if(size>1){
			if(i==size-1 && s[i]=='e'){
			continue;
			}
			}
			answer++;
		}
	}
	if(answer==0){
		answer=1;
	}
	return answer;
	}

//this method calculates grade.
double calculateGrade(){
	double answer = C0 + C1 * wordsCount / sentencesCount + C2 * syllablesCount / wordsCount;
	return answer;
}