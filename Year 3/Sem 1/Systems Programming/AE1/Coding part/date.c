/* Authorship statement:
 * 	Karlis Siders, 2467273S, SP Exercise 1a
 * 	This is my own work except that I got inspiration of string splitting from
 * 	https://stackoverflow.com/questions/15961253/c-correct-usage-of-strok-r
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "date.h"

struct date {
	int day;
	int month;
	int year; 
};

Date *date_create(char *datestr){
	if (strlen(datestr) < 10){
		fprintf(stderr, "Not a valid date - %s\n", datestr);
		exit(EXIT_FAILURE);
	}
	Date * d = malloc(sizeof(Date));
	if (!d){
		fprintf(stderr, "Not enough memory in heap\n");
		exit(EXIT_FAILURE);
	}

	char *number;
	char *rest = datestr;
	int numbers[3];
	
	int i = 0;
	while ((number = strtok_r(rest, "/", &rest))){
		if (atoi(number) == 0){
			fprintf(stderr, "Invalid date - %s, ensure it has only digits and '/'", number);
		}
		numbers[i] = atoi(number);
		i++;
	}
	d->day = numbers[0];
	d->month = numbers[1];
	d->year = numbers[2];
	return d;
}

Date *date_duplicate(Date *d){
	Date * p = malloc(sizeof (Date));
	if (!p){
		fprintf(stderr, "Not enough memory in heap\n");
		exit(EXIT_FAILURE);
	}

	p->day = d->day;
	p->month = d->month;
	p->year = d->year;
	return p;
}

int date_compare(Date *date1, Date *date2){
	if (date1->year < date2->year) return -1;
	else if (date1->year > date2->year) return 1;
	else {
		if (date1->month < date2->month) return -1;
		else if (date1->month > date2->month) return 1;
		else {
			if (date1->day < date2->day) return -1;
			else if (date1->day > date2->day) return 1;
			else return 0;
		}	
	}
}

void date_destroy(Date * d){
	free(d);
}
