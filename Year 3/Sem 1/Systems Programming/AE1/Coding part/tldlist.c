/* Authorship statement:
 * 	Karlis Siders, 2467273S, SP Exercise 1a
 * 	This is my own work except that I got inspiration for the iterator from
 * 	www.geeksforgeeks.org/inorder-successor-in-binary-search-tree
 * 	and for the insertion into a BST from
 *	www.geeksforgeeks.org/binary-search-tree-insert-parent-pointer and
 *	www.geeksforgeeks.org/how-to-handle-duplicates-in-binary-search-tree
 *	
 */


#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "tldlist.h"
#include <stdio.h>

struct tldlist{
	TLDNode * root;
	long count;
	Date * begin;
	Date * end;
};
struct tldnode{
	Date * date;
	char * name;
	long count;
	struct tldnode * parent;
	struct tldnode * left;
	struct tldnode * right;
};
struct tlditerator{
	int first;
	TLDNode * current;
};

static TLDNode * insert(TLDNode * node, char * domain, Date * d);
static void deallocate_tree(TLDNode * node);

TLDList *tldlist_create(Date *begin, Date *end){
	TLDList * list = malloc(sizeof(TLDList));
	list->count = 0;
	list->root = NULL;
	list->begin = begin;
	list->end = end;
	return list;
};

void tldlist_destroy(TLDList *tld){
	// Free all elements of the BST
	deallocate_tree(tld->root);
	free(tld);
};

static void deallocate_tree(TLDNode * node){
	if (!node) return;
	deallocate_tree(node->left);
	deallocate_tree(node->right);
	free(node->name);
	free(node);
}

int tldlist_add(TLDList *tld, char *hostname, Date *d){
	if (date_compare(tld->begin, d) <= 0 && date_compare(tld->end, d) >= 0) {
		// Get domain name including dot
		char * domain = strrchr(hostname, '.');
		if (!domain){
			fprintf(stderr, "Not a valid hostname - %s\n", hostname);
			exit(EXIT_FAILURE);
		}
		// Skip first character (dot)
		memmove(domain, domain+1, strlen(domain));
		// Make it lowercase
		char * domainLowercase = (char *) malloc(4 * sizeof(domain));
		if (!domainLowercase){
			fprintf(stderr, "Not enough memory in heap\n");
			exit(EXIT_FAILURE);
		}
		int i = 0;
		for(; i < strlen(domain); i++){
			domainLowercase[i] = tolower(domain[i]);
		}
		domainLowercase[i+1] = '\0';
		
		// Finally ready for adding
		TLDNode * node = insert(tld->root, domainLowercase, d);
		if (node) {
			// Add to tree count of successful additions
			(tld->count)++;
		}
		if (!tld->root){
			tld->root = node;
			node->parent = NULL;
		}

		free(domainLowercase);
		return 1;
	} else {
		return 0;
	}
};

static TLDNode * insert(TLDNode * node, char * domain, Date * d){
	// Not found, return new node
	if (node == NULL){
		TLDNode * new = malloc(sizeof(TLDNode));
		
		new->name = (char*) malloc(4 * sizeof(char));
		if (!(new->name)){
			fprintf(stderr, "Not enough memory in heap\n");
			exit(EXIT_FAILURE);
		}

		strcpy(new->name, domain);
		
		new->date = d;
		new->left = NULL;
		new->right = NULL;
		new->count = 1;
		return new;
	}
	// Found, increase count
	if (strcmp(node->name, domain) == 0){
		(node->count)++;
		return node;
	}
	// Search left
	else if (strcmp(domain, node->name) < 0) {
		TLDNode * leftChild = insert(node->left, domain, d);
		node->left = leftChild;
		leftChild->parent = node;
	} else { // Search right
		TLDNode * rightChild = insert(node->right, domain, d);
		node->right = rightChild;
		rightChild->parent = node;
	}
	return node;
}

long tldlist_count(TLDList *tld){
	return tld->count;
};

TLDIterator *tldlist_iter_create(TLDList *tld){
	TLDIterator * it = malloc(sizeof(TLDIterator));
	if (!it){
		fprintf(stderr, "Not enough memory in heap\n");
		exit(EXIT_FAILURE);
	}

	TLDNode * start = tld->root;
	while (start->left) {
		start = start->left;
	}
	it->current = start;
	it->first = 1;
	return it;
};

TLDNode *tldlist_iter_next(TLDIterator *iter){
	TLDNode * node = iter->current;
	if (!node){ return NULL;}
	if (iter->first) {
		iter->first = 0;
		return node;
	}
	// If there's a right subtree, next will be the very left of that subtree
	if (node->right) {
		node = node->right;
		while(node->left){
			node = node->left;
		}
		iter->current = node;
		return node;
	}
	// Else next is parent
	TLDNode * parent = node->parent;
	while (parent && node == parent->right){
		node = parent;
		parent = parent->parent;
	}
	iter->current = parent;
	return parent;
};

void tldlist_iter_destroy(TLDIterator *iter){
	free(iter);
};

char *tldnode_tldname(TLDNode *node){
	return node->name;
};

long tldnode_count(TLDNode *node){
	return node->count;
};
