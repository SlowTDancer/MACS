/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"

HeapPriorityQueue::HeapPriorityQueue() {
	logical_length = 0;
	allocated_length = 1;
	queue = new string[allocated_length];
}

HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] queue;
}

int HeapPriorityQueue::size() {
	return logical_length;
}

bool HeapPriorityQueue::isEmpty() {
	return logical_length==0;
}

void HeapPriorityQueue::enqueue(string value) {
	if(logical_length==allocated_length){
		resize();
	}
	logical_length++;
	queue[logical_length-1] = value;
	bubble_up(logical_length-1);
}

string HeapPriorityQueue::peek() {
	if(logical_length==0) error("it's emtpy");
	return queue[0];
}

string HeapPriorityQueue::dequeueMin() {
	string ans = peek();
	string last = queue[logical_length-1];
	queue[0] = last;
	logical_length--;
	bubble_down(0);
	return ans;
}

void HeapPriorityQueue::resize(){
	allocated_length = allocated_length*2;
	string* temp = new string[allocated_length];
	for(int i =0; i<allocated_length/2; i++){
		temp[i] = queue[i];
	}
	delete[] queue;
	queue = temp;
}

void HeapPriorityQueue::bubble_up(int x){
	if(x==0) return;
	if(queue[x]<queue[(x-1)/2]){
		string temp = queue[x];
		queue[x] = queue[(x-1)/2];
		queue[(x-1)/2] = temp;
		bubble_up((x-1)/2);
	}
}

void HeapPriorityQueue::bubble_down(int x){
	if(2*x+1>=logical_length) return;
	int min = 2*x+1;
	if(2*x+1<logical_length && 2*x+2>=logical_length){
		if(queue[min]<queue[x]){
			string temp = queue[x];
			queue[x] = queue[min];
			queue[min] = temp;
			return;
		}	
	}
	if(queue[2*x+1]>queue[2*x+2]) min =2*x+2; 
	if(queue[min]<queue[x]){
		string temp = queue[x];
		queue[x] = queue[min];
		queue[min] = temp;
		bubble_down(min);
	}
}
