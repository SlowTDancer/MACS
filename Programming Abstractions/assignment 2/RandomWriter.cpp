/*
 * File: RandomWriter.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Random Writer problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
#include "filelib.h"
#include "random.h"
#include "strlib.h"
#include "map.h"
#include "vector.h"
#include "random.h"

using namespace std;

//methods.
void readFile(ifstream& file);
int enterMarkovOrder();
void createMap(ifstream& file,int& order,Map<string,Vector<char> >& seedMap);
void generateText(int& order,Map<string,Vector<char> >& seedMap,int& wordAmount);
string findMostPopular(Map<string,Vector<char> >& seedMap);

//main function.
int main() {
	ifstream file;
    readFile(file);
	int order = enterMarkovOrder();
	Map<string,Vector<char> > seedMap;
	int wordAmount = getInteger("how many chars do you want to generate? ");
	createMap(file,order,seedMap);
	generateText(order,seedMap,wordAmount);
	cout<<endl;
    return 0;
}

//reads file.
void readFile(ifstream& file){
	string name = getLine("enter file name: ");
	file.open(name.c_str());
	if(file.fail()){
		cout<<"enter a valid filename."<<endl;
		readFile(file);
	}
}

//costumer enters makarovs order.
int enterMarkovOrder(){
	int order = getInteger("choose Markov model order you would like to use (between 1 and 10): ");
	if(order<1||order>10){
		cout<<"please enter a valid order"<<endl;
		enterMarkovOrder();
	}
	return order;
}

//creates map where sequances are stored.
void createMap(ifstream& file,int& order,Map<string,Vector<char> >& seedMap) {
	string seed = "";
	char ch;
	while(file.get(ch)){
		if(order>(int)seed.length()){
			seed += ch;
			continue;
		}
		if(seedMap.containsKey(seed)){
			seedMap[seed].add(ch);
		}else{
			Vector<char> temp;
			temp.add(ch);
			seedMap[seed]=temp;
		}
		seed = seed.substr(1)+ch;
	}
}

//generates text.
void generateText(int& order, Map<string,Vector<char> >& seedMap, int& wordAmount){
	string popularKey = findMostPopular(seedMap);	
	string text = popularKey;
	for(int i=order;i<wordAmount;i++){
		string key = text.substr(i-order,order);
		int rand = randomInteger(0,seedMap[key].size()-1);
		text += seedMap[key][rand];
	}
	cout<<text;
}

//finds the most populars sequance.
string findMostPopular(Map<string,Vector<char> >& seedMap){
	string answer = "";
	int count = 0;
	foreach(string key in seedMap){
		int size = seedMap[key].size();
		if(count<size){
			answer = key;
			count = size;
		}
	}
	return answer;
}