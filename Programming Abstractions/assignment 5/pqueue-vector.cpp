/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"

VectorPriorityQueue::VectorPriorityQueue() {
	length = 0;
}

VectorPriorityQueue::~VectorPriorityQueue() {
}

int VectorPriorityQueue::size() {
	return length;
}

bool VectorPriorityQueue::isEmpty() {
	return length==0;
}

void VectorPriorityQueue::enqueue(string value) {
	length++;
	queue.add(value);
}

string VectorPriorityQueue::peek() {
	string ans = queue[0];
	if(length==0) error("it's empty");
	for(int i=1; i<size(); i++){
		if(queue[i]<ans) ans = queue[i];
	}
	return ans;
}

string VectorPriorityQueue::dequeueMin() {
	string ans = peek();
	for(int i=0; i<size(); i++){
		if(queue[i]==ans){
			queue.remove(i);
			break;
		}
	}
	length--;
	return ans;
}

