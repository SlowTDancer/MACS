/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include <limits>
using namespace std;

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */
Vector<Loc> shortestPath(Loc start, Loc end, 
						 Grid<double>& world, 
						 double costFn(Loc from, Loc to, Grid<double>& world), 
						 double heuristic(Loc start, Loc end, Grid<double>& world)) {
	Vector<Loc> answer;
	TrailblazerPQueue<Loc> pq;
	Set<Loc> used;
	Set<Loc> greens;
	Grid<pair<Loc, double> > data(world.nRows, world.nCols);
	data[start.row][start.col].first = start;
	data[start.row][start.col].second = 0.0;
	used.add(start);
	colorCell(world, start, YELLOW);
	pq.enqueue(start, heuristic(start, end, world));
	while(!pq.isEmpty()){
		Loc now = pq.dequeueMin();
		greens.add(now);
		colorCell(world, now, GREEN);
		if(now==end)break;
		for(int i=-1; i<=1; i++){
			for(int j=-1; j<=1; j++){
				Loc next = makeLoc(now.row+i, now.col+j);
				if((i==0 && j==0) || !in_bounds(now.row+i, now.col+j, world.nRows, world.nCols) || greens.contains(next)) continue;
				double cost = data[now.row][now.col].second+costFn(now, next, world);
				if(used.contains(next) && data[next.row][next.col].second > cost){
					data[next.row][next.col].first = now;
					data[next.row][next.col].second = cost;
					pq.decreaseKey(next, cost+heuristic(next, end, world));
				}
				if(!used.contains(next)){
					if(cost != numeric_limits<double>::infinity())colorCell(world, next, YELLOW);
					data[next.row][next.col].first = now;
					data[next.row][next.col].second = cost;
					used.add(next);
					pq.enqueue(next, cost+heuristic(next, end, world));
				}  
			}
		}
	}
	answer = find_answer(start, end, data);
    return answer;
}


Set<Edge> createMaze(int numRows, int numCols) {
	Set<pair<double,Edge> > edges;
	Set<Edge> used;
	Set<Edge> maze;
	Grid<pair<int,Loc>> dsu(numRows, numCols);
	for(int i=0; i<numRows; i++){
		for(int j=0; j<numCols; j++){
			dsu[i][j].first = 1;
			dsu[i][j].second = makeLoc(i,j);
			create_edge(edges, used, i, j, numRows, numCols);
		}
	}
	while(!edges.isEmpty()){
		pair<double,Edge> now = edges.first();
		edges.remove(now);
		Loc v1 = now.second.start;
		Loc v2 = now.second.end;
		Loc parent1 = get_parent(v1, dsu);
		Loc parent2 = get_parent(v2, dsu);
		if(parent1==parent2) continue;
		if(dsu[parent1.row][parent1.col].first > dsu[parent2.row][parent2.col].first) swap_parents(parent1, parent2);
		dsu[parent1.row][parent1.col].second = parent2;
		dsu[parent1.row][parent1.col].first = dsu[parent1.row][parent1.col].first + dsu[parent2.row][parent2.col].first;
		maze.add(now.second);
	}
    return maze;
}


bool in_bounds(int i, int j, int rows, int cols){
	return min(i,j)>=0 && i<rows && j<cols;
}

Vector<Loc> find_answer(Loc start, Loc end, Grid<pair<Loc, double> > &data){
	Vector<Loc> ans;
	Queue<Loc> route;
	route.enqueue(end);
	while(!route.isEmpty()){
		Loc now = route.peek();
		route.dequeue();
		ans.add(now);
		if(start==now) break;
		route.enqueue(data[now.row][now.col].first);
	}
	ans = reverse(ans);
	return ans;
}

Vector<Loc> reverse(Vector<Loc> &route){
	Vector<Loc> answer;
	for(int i=0; i<route.size(); i++){
		answer.add(route[route.size()-1-i]);
	}
	return answer;
}

void create_edge(Set<pair<double, Edge> > &edges, Set<Edge> &used, int x, int y, int numRows, int numCols){
	for(int i=-1; i<=1; i++){
		for(int j=-1; j<=1; j++){
			Edge now = makeEdge(makeLoc(x, y), makeLoc(x+i, y+j));
			Edge check = makeEdge(makeLoc(x+i, y+j), makeLoc(x, y));
			if((i==0&&j==0) || (i!=0&&j!=0) || used.contains(now) || used.contains(check) || !in_bounds(x+i, y+j, numRows, numCols)) continue;
			double weight = randomReal(0,1);
			pair<double, Edge> edge;
			edge.first = weight;
			edge.second = now;
			edges.add(edge);
		}
	}
}

Loc get_parent(Loc v, Grid<pair<int,Loc>> &dsu){
	if(v==dsu[v.row][v.col].second) return v;
	return dsu[v.row][v.col].second = get_parent(dsu[v.row][v.col].second, dsu);
}

void swap_parents(Loc &v1, Loc &v2){
	Loc temp = v1;
	v1 = v2;
	v2 = temp;
}