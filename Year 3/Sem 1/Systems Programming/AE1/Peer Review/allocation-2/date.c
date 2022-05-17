// SP Exercise 1A
// HAN Ke
// 2431345k
// This is my own work as defined in the Academic Ethics agreement I have signed.

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <regex.h>
#include "date.h"

struct date
{
	int day;
	int month;
	int year;
};

Date *date_create(char *datestr){
	
	Date *newdate = (Date *) malloc (sizeof(Date));
	
    if (newdate != NULL)
    {
        sscanf(datestr, "%2d/%2d/%4d", &newdate->day, &newdate->month, &newdate->year);
    }
    else
    {
        date_destroy(newdate);
    }
    
    return newdate;
}


Date *date_duplicate(Date *d)
{
    Date *newdate = (Date *) malloc (sizeof(d));
    if (newdate != NULL)
    {
        return memcpy(newdate, d, sizeof(*newdate));
    }
    else
    {
        date_destroy(newdate);
        return NULL;
    }
}

int date_compare(Date *date1, Date *date2)
{
    if (date1 != NULL && date2 != NULL)
    {
        int res = 0;
        
        if (date1->year == date2->year)
        {
            
            if(date1->month == date2->month)
            {
                
                if(date1->day == date2->day)
                {
                    res = 0;
                } else if (date1->day > date2->day)
                {
                    res = 1;
                } else if (date1->day < date2->day)
                {
                    res = -1;
                }
            } else if (date1->month > date2->month)
            {
                res = 1;
            } else if (date1->month < date2->month)
            {
                res = -1;
            }
        } else if (date1->year > date2->year)
        {
            res = 1;
        } else if (date1->year < date2->year)
        {
            res = -1;
        }
        return res;
    }
    else
    {
        return 1;
    }
}

void date_destroy(Date *d)
{
    free(d);
}
