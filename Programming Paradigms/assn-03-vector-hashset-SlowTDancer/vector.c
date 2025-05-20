#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation){
    assert(elemSize > 0);
    assert(initialAllocation >= 0);
    assert(v != NULL);
    if(initialAllocation == 0) initialAllocation = 6;
    v->base = malloc(initialAllocation * elemSize);
    assert(v->base != NULL);
    v->VFF = freeFn;
    v->log_len = 0;
    v->alloc_len = initialAllocation;
    v->elem_size = elemSize;
}

void VectorDispose(vector *v){
    void* curr = v->base;
    for(int i = 0; i < v->log_len; i++){
        if(v->VFF != NULL) v->VFF(curr);
        curr = (void*)((char*)curr + v->elem_size);    
    }
    free(v->base);
}

int VectorLength(const vector *v){
    return v->log_len;
}

void *VectorNth(const vector *v, int position){
    assert(position >= 0 && position < v->log_len);
    void* ans = (void*)((char*)v->base + position * v->elem_size);
    return ans;
}

void VectorReplace(vector *v, const void *elemAddr, int position){
    assert(position >= 0 && position < v->log_len);
    void* curr = (void*)((char*)v->base + position * v->elem_size);
    if(v->VFF != NULL) v->VFF(curr);
    memcpy(curr, elemAddr, v->elem_size);
}

void grow(vector *v){
    int new_alloc_len = 2 * v->alloc_len;
    void* new_base = realloc(v->base, new_alloc_len * v->elem_size);
    assert(new_base != NULL);
    v->alloc_len = new_alloc_len;
    v->base = new_base;
}

void VectorInsert(vector *v, const void *elemAddr, int position){
    assert(position >= 0 && position <= v->log_len);
    assert(elemAddr != NULL);
    if(v->log_len == v->alloc_len) grow(v);
    void* curr = (void*)((char*)v->base + position*v->elem_size);
    void* new = (void*)((char*)curr + v->elem_size);
    memmove(new, curr, (v->log_len - position) * v->elem_size);
    memcpy(curr, elemAddr, v->elem_size);
    v->log_len++;
}

void VectorAppend(vector *v, const void *elemAddr){
    assert(elemAddr != NULL);
    if(v->log_len == v->alloc_len) grow(v);
    void* curr = (void*)((char*)v->base + v->elem_size * v->log_len);
    memcpy(curr, elemAddr, v->elem_size);
    v->log_len++;
}

void VectorDelete(vector *v, int position){
    assert(position >=0 && position < v->log_len);
    void* curr = (void*)((char*)v->base + position * v->elem_size);
    if(v->VFF != NULL) v->VFF(curr);
    if(position != v->log_len - 1){
        void* new = (void*)((char*)curr + v->elem_size);
        memmove(curr, new, (v->log_len - position - 1) * v->elem_size);
    }
    v->log_len--;
}

void VectorSort(vector *v, VectorCompareFunction compare){
    assert(compare != NULL);
    void* base = v->base;
    qsort(base, v->log_len, v->elem_size, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData){
    for(int i = 0; i < v->log_len; i++){
        void* curr = (void*)((char*)v->base + i * v->elem_size);
        mapFn(curr, auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted){
    assert(key != NULL);
    assert(searchFn != NULL);
    assert(startIndex >= 0 && startIndex <= v->log_len);
    char* wanted = NULL;
    if(isSorted){
        wanted = bsearch(key, v->base, v->log_len, v->elem_size, searchFn);
        if(wanted == NULL) return kNotFound;
    }
    if(!isSorted){
        for(int i = 0; i < v->log_len; i++){
            void* curr = (void*)((char*)v->base + i * v->elem_size);
            if(searchFn(key, curr) == 0) {
                wanted = curr;
                break;
            }
        }
        if(wanted == NULL) return kNotFound;
    } 
    int answer = (wanted - (char*)v->base)/v->elem_size;
    return answer;
} 
