/*
 * File: InverseGenetics.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Inverse Genetics problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include <fstream>
#include "simpio.h"
#include "set.h"
#include "map.h"
#include "console.h"
using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */

//some methods.
Map<char, Set<string> > loadCodonMap();
bool is_word(string& protein);
void get_entries(string& protein);
Set<string> all_possible_combinations(string protein, Map<char, Set<string> >& codons);

//main function.
int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap();
	string protein;
	get_entries(protein);
	listAllRNAStrandsFor(protein, codons);
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

//checks if string is a word.
bool is_word(string& protein){
	for(int i = 0; i < protein.length(); i++){
		if(!isalpha(protein[i])) return false;
	}
	return true;
}

//costumer enters values.
void get_entries(string& protein){
	while(true){
		protein = getLine("please enter a protein you want: ");
		if(is_word(protein)) break;
		cout << "please enter a valid value!" << endl;
	}
	protein = toUpperCase(protein);
}

//finds and writes all possible codons for protein.
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons){
	Set<string> result = all_possible_combinations(protein, codons);
	cout << "all possible codon combinations for protein " << protein << ": "<< result << endl;
}

//finds all possible codons for certian protein.
Set<string> all_possible_combinations(string protein, Map<char, Set<string> >& codons){
	Set<string> result;
	if(protein.length() == 1)return codons[protein[0]];
	char pro = protein[0];
	protein = protein.substr(1);
	Set<string> codon = codons[pro];
	foreach(string cod in codon){
		foreach(string codo in all_possible_combinations(protein, codons)){
			result += cod + codo;
		}
	}
	return result;
}