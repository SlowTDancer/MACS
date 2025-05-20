#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn){
	assert(elemSize > 0);
	assert(numBuckets > 0);
	assert(hashfn != NULL);
	assert(comparefn != NULL);
	h->num_buckets = numBuckets;
	h->elem_size = elemSize;
	h->HSHashF = hashfn;
	h->size = 0;
	h->HSCompareF = comparefn;
	h->HSFreeF = freefn;
	h->bucket = malloc(numBuckets * sizeof(vector));
	for(int i = 0; i < numBuckets; i++){
        vector v;
        VectorNew(&v, elemSize, freefn, 0);
        memcpy(h->bucket + i, &v, sizeof(vector));
    }
}

void HashSetDispose(hashset *h){
	for(int i = 0; i < h->num_buckets; i++){
		VectorDispose(&h->bucket[i]);
	}
	free(h->bucket);
}

int HashSetCount(const hashset *h){
	return h->size;
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData){
	assert(mapfn != NULL);
	for(int i = 0; i < h->num_buckets; i++){
		vector* curr = &h->bucket[i];
		VectorMap(curr, mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr){
	assert(elemAddr != NULL);
	int index = h->HSHashF(elemAddr, h->num_buckets);
	assert(index >= 0 && index < h->num_buckets);
	vector* curr = &h->bucket[index];
	int find = VectorSearch(curr, elemAddr, h->HSCompareF, 0, true);
	if(find < 0){
		h->size++;
		VectorAppend(curr, elemAddr);
		VectorSort(curr, h->HSCompareF);
	}else{
		VectorReplace(curr, elemAddr, find);
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr){
	assert(elemAddr != NULL);
	int index = h->HSHashF(elemAddr, h->num_buckets);
	assert(index >= 0 && index < h->num_buckets);
	vector* curr = &h->bucket[index];
	int find = VectorSearch(curr, elemAddr, h->HSCompareF, 0, true);
	if(find < 0) return NULL;
	return VectorNth(curr, find);
}
