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

#include "date.h"

struct date{
    int day;
    int month;
    int year;
};

Date *date_create(char *datestr){
    Date *d = (Date *)malloc(sizeof(Date));
    char arr[2];
    sscanf(datestr, "%2d%c%2d%c%4d", &d->day, &arr[0], &d->month, &arr[1], &d->year);
    if (arr[0]!=arr[1] || arr[1]!='/'){
        free(d);
        return NULL;
    }
    return d;    
}

Date *date_duplicate(Date *d){
    Date *newDate = (Date *)malloc(sizeof(Date));
    newDate = d;
    return newDate;
}

int date_compare(Date *date1, Date *date2){
    if ((date1->year)<(date2->year)){
        return -1;
    }else if ((date1->year)==(date2->year)){
        if ((date1->month)<(date2->month)){
            return -1;
        } else if ((date1->month)==(date2->month)){
            if ((date1->day)<(date2->day)){
                return -1;
            } else if ((date1->day)==(date2->day)){
                return 0;
            }else {
                return 1;
            }
        }else {
            return 1;
        }
    } else {
        return 1;
    }
}

void date_destroy(Date *d){
    free(d);
}