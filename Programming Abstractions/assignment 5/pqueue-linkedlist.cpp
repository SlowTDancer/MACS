/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"

LinkedListPriorityQueue::LinkedListPriorityQueue() {
	head = new Node;
	head->next = NULL;
	length = 0;
}

LinkedListPriorityQueue::~LinkedListPriorityQueue() {
	Node* temp = head;
	while(temp!=NULL){
		Node* ptr = temp->next;
		delete temp;
		temp = ptr;
	}
}

int LinkedListPriorityQueue::size() {
	return length;
}

bool LinkedListPriorityQueue::isEmpty() {
	return length==0;
}

void LinkedListPriorityQueue::enqueue(string value) {
	length++;
	Node* insert = new Node();
	insert->val = value;
	if(head->next==NULL){
		head->next = insert;
		insert->next = NULL;
		return;
	}
	if(head->next->val > value){
		insert->next = head->next;
		head->next = insert;
		return;
	}
	Node* temp = head;
	for(Node* ptr = head->next; ptr!=NULL; ptr=ptr->next){
		if(ptr->val > value){
			insert->next = ptr;
			temp->next = insert;
			return;
		}
		temp = ptr;
	}
	while(temp->next!=NULL){
		temp = temp->next;
	}
	temp->next = insert;
	insert->next = NULL;
}

string LinkedListPriorityQueue::peek() {
	if(length==0) error("it's empty");
	string ans = head->next->val;
	return ans;
}

string LinkedListPriorityQueue::dequeueMin() {
	string ans = peek();
	length--;
	Node* temp = head->next;
	head->next = head->next->next;
	delete temp;
	return ans;
}

