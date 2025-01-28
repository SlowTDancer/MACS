/*
 * File: Sierpinski.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Sierpinski problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <cmath>
#include "gwindow.h"
#include "gtypes.h"
#include "simpio.h"

using namespace std;

//some methods.
void get_entries(int& length, int& rank, GWindow& gw);
GPoint get_position(GWindow& gw, int length);
void draw_triangle(int length,GWindow& gw, GPoint pos);
void draw_sierpinski_triangle(int length, int rank,GWindow& gw, GPoint pos);

// main function.
int main() {
	int length, rank;
	GWindow gw;
    get_entries(length, rank, gw);
	GPoint pos = get_position(gw, length);
	draw_sierpinski_triangle(length, rank, gw, pos);
    return 0;
}

//costumer enters values.
void get_entries(int& length, int& rank, GWindow& gw){
	while(true){
		length = getInteger("please enter a sides length: ");
		rank = getInteger("please enter a fractal rank: ");
		if(rank >= 0 && length > 0 && length <= gw.getWidth() && length*sqrt(3.0)/2<=gw.getHeight()) break;
		cout << "please enter valid entries!" << endl;
	}
}

//gets starting position.
GPoint get_position(GWindow& gw, int length){
	double x =(gw.getWidth()- length)/2;
	double y =(gw.getHeight()+ length*sqrt(3.0)/2)/2;
	GPoint pt(x,y);
	return pt;
}

//draws triangle.
void draw_triangle(int length,GWindow& gw, GPoint pos){
	GPoint p1 = gw.drawPolarLine(pos, length, 0);
	GPoint p2 = gw.drawPolarLine(pos, length, 60);
	gw.drawLine(p1,p2);
}

//draws sierpinski triangle.
void draw_sierpinski_triangle(int length, int rank, GWindow& gw, GPoint pos){
	draw_triangle(length, gw, pos);
	if(rank==0) return;
	GPoint p1 = gw.drawPolarLine(pos, length / 2, 60);
    GPoint p2 = gw.drawPolarLine(pos, length / 2, 0);
    draw_sierpinski_triangle(length / 2, rank - 1, gw, pos);
	draw_sierpinski_triangle(length / 2, rank - 1, gw, p1);
    draw_sierpinski_triangle(length / 2, rank - 1, gw, p2);
}