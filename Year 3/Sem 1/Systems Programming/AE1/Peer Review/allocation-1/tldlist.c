/*
* Name: Eva Kupcova
* GUID: 2479253k
* Assignment: SP Exercise 1a
*
* This is my own work as defined in the Academic Ethics agreement I have signed.
* 
*/

#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "date.h"
#include "tldlist.h"

struct tldnode {
    char *domain;
    long count;
    struct tldnode *left;
    struct tldnode *right;
};

struct tldlist {
    TLDNode *root;
    Date *begin;
    Date *end;
    long count;
};

typedef struct stack {
    long MAXSIZE;           
    long top;  
    TLDNode *arr[LONG_MAX/sizeof(TLDNode)];
} Stack; 


struct tlditerator {
    Stack *s;
};

Stack *init_stack(long max){
    Stack *st = (Stack *)malloc(sizeof(Stack));
    st->MAXSIZE = max;
    st->top = -1;
    return st;
}

TLDNode *node_create(char *hostname){
    TLDNode *n = (TLDNode *)malloc(sizeof(TLDNode));
    n->domain=malloc(strlen(hostname)+1);
    strcpy(n->domain,hostname);
    n->count = 1;
    n->left= n->right=NULL;
    return n;
} 

int isempty(Stack *s) {

   if(s->top == -1)
      return 1;
   else
      return 0;
}
   
int isfull(Stack *s) {

   if(s->top == s->MAXSIZE)
      return 1;
   else
      return 0;
}

TLDNode *pop(Stack *s) {
    TLDNode *data;
    if(!isempty(s)) {
        data = s->arr[s->top];
        s->top--;   
        return data;
    } 
    return NULL;
}

void push(TLDNode *data, Stack *s) {
    if(!isfull(s)) {
        s->top++;   
        s->arr[s->top] = data;
    }
}

TLDList *tldlist_create(Date *begin, Date *end){
    TLDList *l = (TLDList *)malloc(sizeof(TLDList));
    l->begin = begin;
    l->end = end;
    l->root = NULL;
    l->count = 0;
    return l;
}

void tldlist_destroy(TLDList *tld){
    free(tld);
}

char *tldnode_tldname(TLDNode *node){
    return node->domain;
}

long tldnode_count(TLDNode *node){
    return node->count;
}

int tldlist_add(TLDList *tld, char *hostname, Date *d){
    if (date_compare(tld->begin, d)>0 || date_compare(d, tld->end)>0){
        return 0;
    }
    char *domain = strtok(strrchr(hostname, '.'), ".");
    if (tld->root == NULL){
        tld->root = node_create(domain);
        tld->count+=1;
        return 1;
    } else{
        TLDNode *parent = NULL;
        TLDNode *current = tld->root;
        while (current!=NULL){
            if (strcmp(domain,current->domain)==0){
                current->count+=1;
                tld->count+=1;
                return 1;
            }else if (strcmp(domain,current->domain)<0){
                parent = current;
                current = current->left;
                if (!current) parent->left=node_create(domain);
            }else {
                parent = current;
                current = current->right;
                if (!current) parent->right=node_create(domain);
            }
        }
        tld->count+=1;
    }
    
    return 1;
}

long tldlist_count(TLDList *tld){
    return tld->count;
}

void preorder(TLDNode *root, TLDIterator *i) 
{ 
    if (root != NULL) 
    { 
        push(root, i->s);
        preorder(root->left, i); 
    } 
} 

TLDIterator *tldlist_iter_create(TLDList *tld){
    TLDIterator *i = (TLDIterator *)malloc(sizeof(TLDIterator));
    i->s = init_stack(tld->count);
    TLDNode *current = tld->root;
    preorder(current, i); 
    return i;
}


TLDNode *tldlist_iter_next(TLDIterator *iter){
    TLDNode *next = pop(iter->s);
    if (!next) return NULL;
    if (next->right) preorder(next->right, iter);
    return next;
}


void tldlist_iter_destroy(TLDIterator *iter){
    free(iter);
};


