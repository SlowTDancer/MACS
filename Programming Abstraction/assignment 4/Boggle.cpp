/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include <cmath>
#include <utility>

using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */
void welcome();
bool ask_about_instructions();
void giveInstructions();
void instructions();
string question_one();
string question_two();
void shuffle(Vector<string> &cubes);
void costum_cubes(int x, Vector<string> &cubes);
void choose_board_configuration(Vector<string> &cubes);
char random_char(string str);
void fill_board(Vector<string> &cubes, int size, Grid<char> &board);
Grid<char> boggle_setup(Vector<string> &cubes);
bool in_bounds(int i,int j, int rows, int cols);
bool find_word_path(string word, Grid<char> &board, Vector<pair<int, int> > &word_path, pair<int, int> loc, Set<pair<int, int> > &fix);
void highlight_path(Vector<pair<int, int> > &word_path);
bool on_board(string word, Grid<char> &board);
Set<string> players_turn(Lexicon &words, Grid<char> &board);
void find_all_words(string prefix, Lexicon &words, Set<string> &used_words, Grid<char> &board, pair<int, int> loc,Set<pair<int, int> > &word_path, Player player);
void find_all_words_from_region(int m, int n, int x, int y, Lexicon &words, Set<string> &used_words, Grid<char> &board, Player player);
void computers_turn(Lexicon &words, Set<string> &players_words, Grid<char> &board);
string last_question();
void play_boggle(GWindow &gw);

/* Main program */

int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
    welcome();
	instructions();
	play_boggle(gw);
    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

//asks if user wants instructions.
bool ask_about_instructions(){
	string ans;
	while(true){
		ans = getLine("Do you need instructions? (please answer yes or no) ");
		ans = toUpperCase(ans);
		if(ans=="YES" || ans=="NO") break;
		cout<<"please answer yes or no!"<<endl;
	}
	if(ans == "NO") return false;
	return true;
}

//if needed writes instructions.
void instructions(){
	if(ask_about_instructions()){
		giveInstructions();
	}
}

//ask question one.
string question_one(){
	string answer;
	while(true){
		answer = getLine("You can choose standard Boggle (4x4 grid) or Big Boggle (5x5). Would you like Big Boggle? " );
		answer = toUpperCase(answer);
		if(answer=="YES" || answer=="NO") break;
		cout<<"please answer yes or no!"<<endl;
	}
	return answer;
}

//ask question two.
string question_two(){
	string answer;
	while(true){
		answer = getLine("I'll give you a chance to set up the board to your specification, which makes it easier to confirm your boggle program is working. Do you want to force the board configuration? ");
		answer = toUpperCase(answer);
		if(answer=="YES" || answer=="NO") break;
		cout<<"please answer yes or no!"<<endl;
	}
	return answer;
}

//shuffles cubes.
void shuffle(Vector<string> &cubes){
	for(int i=0; i<cubes.size(); i++){
		int x = randomInteger(i,cubes.size()-1);
		string temp = cubes[i];
		cubes[i] = cubes[x];
		cubes[x] = temp;
	}
}

//creates costum cubes.
void costum_cubes(int x, Vector<string> &cubes){
	string line;
	cout << "enter a string with length " << x << ": ";
	while(true){
		line = getLine();
		if(line.length()==x)break;
		cout<<"please enter valid entry! "<<endl;
		continue;
	}
	line = toUpperCase(line);
	for(int i=0; i<line.length(); i++){
		string now = "";
		now += line[i];
		cubes.add(now);
	}
}

//user chooses board configuration.
void choose_board_configuration(Vector<string> &cubes){
	string answer_one = question_one();
	string answer_two = question_two();
	if(answer_one=="YES"){
		if(answer_two=="YES"){
			costum_cubes(25, cubes);
		}else{
			for(int i=0; i<25; i++){
				cubes.add(BIG_BOGGLE_CUBES[i]);
			}
			shuffle(cubes);
		}
	}else{
		if(answer_two=="YES"){
			costum_cubes(16, cubes);			
		}else{
			for(int i=0; i<16; i++){
				cubes.add(STANDARD_CUBES[i]);
			}
			shuffle(cubes);
		}
	}
}

//you get a random char from a string.
char random_char(string str){
	int x = randomInteger(0, str.length()-1);
	return str[x];
}

//fills board.
void fill_board(Vector<string> &cubes, int size, Grid<char> &board) {
    for(int i = 0; i < cubes.size(); i++) {
        board[i/size][i%size] = random_char(cubes[i]);
    }
}

//draws boggle.
void draw_boggle(Grid<char> &board){
	for(int i=0; i<board.numRows(); i++) {
        for(int j=0; j<board.numCols(); j++) {
            char ch = board[i][j];
            labelCube(i, j, ch);
        }
    }
}

//prepares boogle setup.
Grid<char> boggle_setup(Vector<string> &cubes){
	int size = 0;
	if(cubes.size()==16)size=4;
	if(cubes.size()==25)size=5;
	Grid<char> board(size,size);
	drawBoard(size, size);
	fill_board(cubes, size, board);
	draw_boggle(board);
	return board;
}

//checks if we are in bounds.
bool in_bounds(int i,int j, int rows, int cols){
        return min(i,j)>=0 && i<rows && j<cols;
}

//finds word path and returns true if it exists.
bool find_word_path(string word, Grid<char> &board, Vector<pair<int, int> > &word_path, pair<int, int> loc, Set<pair<int, int> > &fix){
	if(word=="") return true;
	if(word[0]!=board[loc.first][loc.second]) return false;
	fix.add(loc);
	word_path.add(loc);
	string wordNow = word.substr(1);
	for(int i=-1; i<=1; i++){
		for(int j=-1; j<=1; j++){
			if(i==0 && j==0) continue;
			if(in_bounds(loc.first+i, loc.second+j, board.numRows(), board.numCols())){
			pair<int, int> locNow;
			locNow.first = loc.first+i;
			locNow.second = loc.second+j;
			if(!fix.contains(locNow)){
				if(find_word_path(wordNow, board, word_path, locNow, fix)) return true;
				}		
			}
		}
	}
	word_path.remove(word_path.size() - 1);
	return false;
}

//highlights path.
void highlight_path(Vector<pair<int, int> > &word_path) {
    for(int i = 0; i < word_path.size(); i++) {
		highlightCube(word_path[i].first, word_path[i].second, true);
		pause(200);
    }
    pause(500);
    for(int i = 0; i < word_path.size(); i++) {
		highlightCube(word_path[i].first, word_path[i].second, false);
    }
}

//checks if word is on board.
bool on_board(string word, Grid<char> &board){
	Vector<pair<int, int> > word_path;
	pair<int, int> loc;
	for(int i=0; i<board.numRows(); i++) {
        for(int j=0; j<board.numCols(); j++) {
			if(word[0]==board[i][j]){
				Set<pair<int, int> > fix;
				loc.first = i;
				loc.second = j;
				if(find_word_path(word, board, word_path, loc, fix)){
					highlight_path(word_path);
					return true;
				}
			}
        }
    }
	return false;
}

//players turn.
Set<string> players_turn(Lexicon &words, Grid<char> &board){
	Player player = HUMAN;
	Set<string> used_words;
	while(true){
		string word = getLine("please enter a word: ");
		word = toUpperCase(word);
		if(word=="") break;
		if(word.length()<4 || !words.contains(toLowerCase(word)) || used_words.contains(word) || !on_board(word, board)){
			cout<<"please enter a vaild word!"<<endl;
			continue;
		}
		recordWordForPlayer(word, player);
		used_words.add(word);
	}

	return used_words;
}

//finds all words.
void find_all_words(string prefix, Lexicon &words, Set<string> &used_words, Grid<char> &board, pair<int, int> loc, Set<pair<int, int> > &fix, Player player){
    if(!words.containsPrefix(prefix)) return;
	if(!used_words.contains(prefix) && prefix.length() >= 4 && words.contains(prefix)) {
        used_words.add(prefix);
        recordWordForPlayer(prefix, player);
	}
	fix.insert(loc);
	for(int i=-1; i<=1; i++){
		for(int j=-1; j<=1; j++){
			if(i==0 && j==0) continue;
			if(in_bounds(loc.first+i, loc.second+j, board.numRows(), board.numCols())){
				pair<int, int> locNow;
				locNow.first = loc.first+i;
				locNow.second = loc.second+j;
				string wordNow = prefix + board[locNow.first][locNow.second];
				if(!fix.contains(locNow)){
					find_all_words(wordNow, words, used_words, board, locNow, fix, player);
					fix.remove(locNow);
				}
			}
		}
	}
}

// finds all words from certain regions.
void find_all_words_from_region(int m, int n, int x, int y, Lexicon &words, Set<string> &used_words, Grid<char> &board, Player player){
	for(int i = m; i < x; i++) {
        for(int j = n; j < y; j++) {
            string prefix = "";
			prefix = prefix + board[i][j];
            Set<pair<int, int> > fix;
			pair<int, int> loc;
			loc.first = i;
			loc.second = j;
            find_all_words(prefix, words, used_words, board, loc, fix, player);
        }
	}
}

//computers turn.
void computers_turn(Lexicon &words, Set<string> &used_words, Grid<char> &board){
	Player player = COMPUTER;
	find_all_words_from_region(0, 0, board.numRows(), board.numCols(), words, used_words, board, player);
}

//asks if player wants to play again.
string last_question(){
	string answer;
	while(true){
		answer =  getLine("do you want to play again?(answer YES or NO) ");
		answer = toUpperCase(answer);
		if(answer=="YES" || answer=="NO") break;
		cout<<"please answer"<<endl;
	}
	return answer;
}

//plays boggle.
void play_boggle(GWindow &gw){
	Lexicon words("EnglishWords.dat");
	while(true){
		Vector<string> cubes;
		choose_board_configuration(cubes);
		Grid<char> board = boggle_setup(cubes);
		Set<string> players_words = players_turn(words, board);
		computers_turn(words, players_words, board);
		string again = last_question();
		if(again=="NO") break;
	}
}