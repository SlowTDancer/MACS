/*
 * File: WordLadder.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Word Ladder problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "lexicon.h"
#include "vector.h"
#include "queue.h"
#include "set.h"
#include "strlib.h"
#include "simpio.h"

using namespace std;

//methods
void playWordLadder(Lexicon& englishWords);
Vector<string> wordLadder(string& startingWord, string& finalWord, Lexicon& englishWords);
Set<string> simWords(string& word, Lexicon& englishWords);
string ladderToString(Vector<string>& ladder);

//main function
int main() {
	cout<<"Welcome :sparklingHeart:"<<endl;
	Lexicon englishWords("EnglishWords.dat");
	playWordLadder(englishWords);
    return 0;
}

//gets information and finds a wordladder.
void playWordLadder(Lexicon& englishWords){
	while(true){
		string startingWord = getLine("enter starting word(RETURN to quit): ");
		if(startingWord=="RETURN") break;
		string finalWord = getLine("enter destination word: ");
		startingWord = toLowerCase(startingWord);
		finalWord = toLowerCase(finalWord);
		bool valid = englishWords.contains(startingWord) && englishWords.contains(finalWord) && finalWord.length()==startingWord.length();
		if(!valid){
			cout<<"please enter a valid word."<<endl;
			continue;
		}
		Vector<string> ladder = wordLadder(startingWord, finalWord,englishWords);
		if(ladder.size()==1){
			cout<<"ladder not found."<<endl;
			continue;
		}
		string answer = ladderToString(ladder);
		cout<<"found ladder: "<<answer<<endl;
	}
}

//finds wordladder.
Vector<string> wordLadder(string& startingWord, string& finalWord, Lexicon& englishWords){
	Queue<Vector<string> > ladders;
	Set<string> fix;
	Vector<string> ladder;
	ladder.add(startingWord);
	ladders.enqueue(ladder);
	fix.insert(startingWord);
	while(!ladders.isEmpty()){
		Vector<string> ladderNow = ladders.dequeue();
		string wordNow = ladderNow[ladderNow.size()-1];
		if(wordNow == finalWord){
		return ladderNow;
		} 
		Set<string> almostEqualWords = simWords(wordNow,englishWords);
		foreach(string word in almostEqualWords){
			if(!fix.contains(word)){
			fix.insert(word);
			Vector<string> newLadder = ladderNow;
			newLadder.add(word);
			ladders.enqueue(newLadder);
			}
		}
	}
	return ladder;
}

//gets similar words.
Set<string> simWords(string& word, Lexicon& englishWords){
	Set<string> almostEqualWords;
	string alpha = "abcdefghijklmnopqrstuvwxyz";
	for(int i=0;i<(int)word.length();i++){
		string temp = word;
		for(int j=0;j<(int)alpha.length();j++){
			temp[i] = alpha[j];
			if((!almostEqualWords.contains(temp)|| word!=temp)&&englishWords.contains(temp)){
			almostEqualWords.add(temp);
			}
		}
	}
	return almostEqualWords;
}

//ladderToString method.
string ladderToString(Vector<string>& ladder){
	string answer = "";
	for(int i=0;i<(int)ladder.size();i++){
		answer+=ladder[i];
		if(i!=(int)ladder.size()-1) answer+= " -> ";
	}
	return answer;
}