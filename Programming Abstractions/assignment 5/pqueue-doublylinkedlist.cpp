/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	head = new Node;
	head->next = NULL;
	head->prev = NULL;
	length = 0;
}

DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	Node* temp = head;
	while(temp!=NULL){
		Node* ptr = temp->next;
		delete temp;
		temp = ptr;
	}
}

int DoublyLinkedListPriorityQueue::size() {
	return length;
}

bool DoublyLinkedListPriorityQueue::isEmpty() {
	return length==0;
}

void DoublyLinkedListPriorityQueue::enqueue(string value) {
	length++;
	Node* new_node= new Node;
    new_node->val = value;
    new_node->prev = NULL;
    new_node->next = head;
	head->prev = new_node;
    head = new_node;
}

string DoublyLinkedListPriorityQueue::peek() {
	if(length==0) error("it's empty");
	string ans = head->val;
	Node* temp = head->next;
	for(int i=1; i<size(); i++){
		if(temp->val<ans) ans = temp->val;
		temp = temp->next;
	}
	return ans;
}

string DoublyLinkedListPriorityQueue::dequeueMin() {
	string ans = peek();
	length--;
	Node* temp = head;
	for(int i=0; i<size(); i++){
		if(temp->val==ans){
			if(temp->prev==NULL){
				temp = temp->next;
				temp->prev = NULL;
				delete head;
				head = temp;
				break;
			}
			temp->prev->next = temp->next;
			temp->next->prev = temp->prev;
			delete temp;
			break;
		}
		temp = temp->next;
	}
	return ans;
}

