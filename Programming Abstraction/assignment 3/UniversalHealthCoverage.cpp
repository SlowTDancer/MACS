/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <string>
#include "simpio.h"
#include "set.h"
#include "vector.h"
#include "console.h"
using namespace std;

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
void fill_testcases(Set<string>& cities, Vector< Set<string> >& locations, int& numHospitals, int& num_testcase);
bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
                               Vector< Set<string> >& result);

//some methods.
void get_entries(int& num_testcase);
void fill_testcases(Set<string>& cities, Vector< Set<string> >& locations, int& numHospitals, int& num_testcase);
bool check_if_yes(Set<string>& cities, Vector< Set<string> >& result);
void write_answer(bool ans, Vector< Set<string> >& result);

//main function.
int main() {
    Vector< Set<string> > result, locations;
	int numHospitals, num_testcase;
	Set<string> cities;
	get_entries(num_testcase);
	fill_testcases(cities, locations, numHospitals, num_testcase);
	bool answer = canOfferUniversalCoverage(cities, locations, numHospitals, result);
	write_answer(answer, result);
    return 0;
}

//costumer enters values.
void get_entries(int& num_testcase){
	while(true){
		num_testcase = getInteger("please enter which testcase do you want?(choose in 1-3 interval): ");
		if(num_testcase>=1 && num_testcase<=3) break;
	}
}

//some specific testcases.
void fill_testcases(Set<string>& cities, Vector< Set<string> >& locations, int& numHospitals, int& num_testcase){
	if(num_testcase == 1){	
		cities.add("A");
		cities.add("B");
		cities.add("C");
		cities.add("D");
		cities.add("E");
		Set<string> s1; s1.add("A"); s1.add("B"); s1.add("C");
		Set<string> s2; s2.add("A"); s2.add("E");
		Set<string> s3; s3.add("A"); s3.add("D");
		Set<string> s4; s4.add("A"); s4.add("D"); s4.add("C");
		Set<string> s5; s5.add("D"); s5.add("B"); s5.add("C");
		locations.add(s1); locations.add(s2); locations.add(s3); locations.add(s4); locations.add(s5);
		numHospitals = 2;
	}
	if(num_testcase == 2){
		cities.add("A");
		cities.add("B");
		cities.add("C");
		cities.add("D");
		cities.add("E");
		Set<string> s1; s1.add("A"); s1.add("B"); s1.add("C");
		Set<string> s2; s2.add("A"); s2.add("E");
		Set<string> s3; s3.add("A"); s3.add("D");
		Set<string> s4; s4.add("A"); s4.add("D"); s4.add("C");
		Set<string> s5; s5.add("D"); s5.add("B"); s5.add("C");
		locations.add(s1); locations.add(s2); locations.add(s3); locations.add(s4); locations.add(s5);
		numHospitals = 1;
	}
	if(num_testcase == 3){
		cities.add("A");
		cities.add("B");
		cities.add("C");
		cities.add("D");
		Set<string> s1; s1.add("A");
		Set<string> s2; s2.add("A"); s2.add("D"); s2.add("C");
		Set<string> s3; s3.add("D"); s3.add("B"); s3.add("C");
		locations.add(s1); locations.add(s2); locations.add(s3);
		numHospitals = 2;
	}
}

//this method finds if ministry can offer universal coverage.
bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
							   Vector< Set<string> >& result){
	if(numHospitals == 0) return check_if_yes(cities, result);
	for(int i = 0; i<locations.size(); i++){
		result += locations[i];
		Vector<Set<string> > locations_left = locations;
		locations_left.remove(i);
		if(check_if_yes(cities, result) || canOfferUniversalCoverage(cities, locations_left, numHospitals-1, result)) return true;
		result.remove(result.size()-1);
	}
	return false;
}

//checks if result can cover cities.
bool check_if_yes(Set<string>& cities, Vector< Set<string> >& result){
	Set<string> res;
	foreach(Set<string> locs in result){
		res += locs;
	}
	return cities.isSubsetOf(res);
}

//writes answer.
void write_answer(bool ans, Vector< Set<string> >& result){
	if(ans){
		cout << "can offer universal coverage: " << result << endl; 
	}else{
		cout << "can't offer universal coverage." << endl;
	}
}