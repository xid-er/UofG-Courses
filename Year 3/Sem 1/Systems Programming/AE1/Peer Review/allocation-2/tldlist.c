// SP Exercise 1A
// HAN Ke
// 2431345k
// This is my own work as defined in the Academic Ethics agreement I have signed.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "date.h"
#include "tldlist.h"

//Custom Functions

//create a tldnode and add it to tldlist
TLDNode *tldnode_create(TLDList *tldList, char *tld, TLDNode *parent);
//tld iterator add tldnode
void iterator_add(TLDIterator *iter, TLDNode *root, int *index);

int get_height(TLDNode *node);
TLDNode *rotate_left(TLDNode *node);
TLDNode *rotate_right(TLDNode *node);
//rotate first left then right
TLDNode *rotate_left_right(TLDNode *node);
//rotate right then left
TLDNode *rotate_right_left(TLDNode *node);
void set_balance(TLDNode *node);
void reset_balance(TLDList *tld, TLDNode *node);

void tldnodes_destroy(TLDNode *node);

//Define basic structures
struct tldnode {
  TLDNode *parent;
  TLDNode *left;
  TLDNode *right;
  int height;
  int balance;
  char *tld;
  long count;  
};

struct tldlist {
  TLDNode *root;
  Date *begin;
  Date *end;
  long count;
  long size;
};

struct tlditerator {
  TLDNode **next;
  int index;
  long length;
};


TLDNode *tldnode_create(TLDList *tldList, char *tld, TLDNode *parent) {
  TLDNode *node = (TLDNode *) malloc(sizeof(TLDNode));
  if (node != NULL) {
    node->parent = parent;
    node->left = NULL;
    node->right = NULL;
    node->tld = tld;
    node->height = 0;
    node->balance = 0;
    node->count = 1;
    tldList->size++;  // tldlist plus 1
  }
  return node;
}

void iterator_add(TLDIterator *iter, TLDNode *root, int *index) {
  if (root != NULL) {
    iterator_add(iter, root->left, index);
    *(iter->next + (*index)++) = root; 
    iterator_add(iter, root->right, index);
  }
}

TLDList *tldlist_create(Date *begin, Date *end) {
  TLDList *tldList = (TLDList *) malloc(sizeof(TLDList));
  if (tldList != NULL) {
    tldList->root = NULL;
    tldList->begin = date_duplicate(begin);  //ensure no memory leak
    tldList->end = date_duplicate(end);
    tldList->count = 0;
    tldList->size = 0;
  }
  return tldList;
}


int tldlist_add(TLDList *tld, char *hostname, Date *d) {
  //check the date
  if (date_compare(tld->begin, d) > 0 || date_compare(tld->end, d) < 0) {
    return 0;
  }

  //treatement of hostname
  char *tld_found = strrchr(hostname, '.') + 1;
  char *tldString = (char *) calloc(strlen(tld_found) + 1, sizeof(char));
  strcpy(tldString, tld_found);

  if (tld->root != NULL) {
    TLDNode *currentNode = tld->root;
    while (currentNode != NULL) {
      int temp = strcmp(currentNode->tld, tldString);

      if (temp == 0) {
        free(tldString); 
        currentNode->count++;
        break;
      }

      TLDNode *parentNode = currentNode;

      currentNode = (temp > 0) ? currentNode->left : currentNode->right;

      if (currentNode == NULL) {
        if (temp > 0) {
          parentNode->left = tldnode_create(tld, tldString, parentNode);
        } else if (temp < 0) {
          parentNode->right = tldnode_create(tld, tldString, parentNode);
        }
        reset_balance(tld, parentNode);
      }
    }
  } else {
    tld->root = tldnode_create(tld, tldString, NULL);
  }
  tld->count++;
  return 1;
}

long tldlist_count(TLDList *tld) {
  return (tld != NULL) ? tld->count : 0;
}

TLDIterator *tldlist_iter_create(TLDList *tld) {
  if (tld == NULL)
    return NULL;

  TLDIterator *iter = (TLDIterator *) malloc(sizeof(TLDIterator));
  if (iter != NULL) {
    iter->index = 0;
    iter->length = tld->size;
    iter->next = (TLDNode **) malloc(sizeof(TLDNode *) * iter->length);
    if (iter->next == NULL) {
      free(iter);
      return NULL;
    }

    int index = 0;
    iterator_add(iter, tld->root, &index);
  }
  return iter;
}


TLDNode *tldlist_iter_next(TLDIterator *iter) {
  if (iter->index >= iter->length)
    return NULL;
  
  return *(iter->next + iter->index++); 
}


char *tldnode_tldname(TLDNode *node) {
  if (node != NULL) {
    return node->tld;
   }
  else{
    return "";
  }

}

long tldnode_count(TLDNode *node) {
  if (node != NULL) {
    return node->count;
  } 
  else{
    return 0;
  }
}

int get_height(TLDNode *node) {
  if (node != NULL) {
    return node->height;
  } 
  else{
    return -1;
  }
}


void set_balance(TLDNode *node) {
  if (node != NULL) {
    int left_height = get_height(node->left);
    int right_height = get_height(node->right);
    int max_height = (left_height > right_height) ? left_height : right_height;
    node->height = 1 + max_height;
  }
  node->balance = get_height(node->right) - get_height(node->left);
}

TLDNode *rotate_left(TLDNode *node) {
  TLDNode *right = node->right;
  right->parent = node->parent;

  node->right = right->left;
  if (node->right != NULL) {
    node->right->parent = node;
  }

  right->left = node;
  node->parent = right;

  if (right->parent != NULL) {
    if (right->parent->right == node) {
      right->parent->right = right;
    } else {
      right->parent->left = right;
    }
  }

  set_balance(node);
  set_balance(right);

  return right;
}
 
TLDNode *rotate_right(TLDNode *node) {
  TLDNode *left = node->left;
  left->parent = node->parent;

  node->left = left->right;

  if (node->left != NULL) {
    node->left->parent = node;
  }

  left->right = node;
  node->parent = left;

  if (left->parent != NULL) {
    if (left->parent->right == node) {
      left->parent->right = left;
    } else {
      left->parent->left = left;
    }
  }

  set_balance(node);
  set_balance(left);

  return left;
}
 
TLDNode *rotate_left_right(TLDNode *node) {
  node->left = rotate_left(node->left);
  return rotate_right(node);
}
 
TLDNode *rotate_right_left(TLDNode *node) {
  node->right = rotate_right(node->right);
  return rotate_left(node);
}


void reset_balance(TLDList *tld, TLDNode *node) {
  set_balance(node);

  if (node->balance == -2) {
    if (get_height(node->left->left) >= get_height(node->left->right)) {
      node = rotate_right(node);
    } else {
      node = rotate_left_right(node);
    }
  } else if (node->balance == 2) {
    if (get_height(node->right->right) >= get_height(node->right->left)) {
      node = rotate_left(node);
    } else {
      node = rotate_right_left(node);
    }
  }

  if (node->parent != NULL) {
    reset_balance(tld, node->parent);
  } else {
    tld->root = node;
  }
}

void tldnodes_destroy(TLDNode *node) {
  if (node != NULL) {
    tldnodes_destroy(node->left);
    tldnodes_destroy(node->right);
    free(node->tld);
    free(node);
  }
}

void tldlist_destroy(TLDList *tld) {
  if (tld != NULL) {
    tldnodes_destroy(tld->root); 
    date_destroy(tld->begin); 
    date_destroy(tld->end); 
    free(tld);
  }
}

void tldlist_iter_destroy(TLDIterator *iter) {
  free(iter->next);
  free(iter);
}