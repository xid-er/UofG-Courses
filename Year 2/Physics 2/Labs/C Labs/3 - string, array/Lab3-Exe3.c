#include <stdio.h>

struct TwoDPoint {
	double x, y;
};
// Added line to typedef struct used in line 10
typedef struct TwoDPoint TwoDPoint_t;

int main(void) {
	TwoDPoint_t a, b = {0.0 ,0.0};
	
	// The input handling is in a separate block to inrease legibility and understanding of
	// the code. It does not need to be there, but is appreciated by any analysts/debuggers.
	{
		char input [100];
		puts("Please enter the x and y coordinates of the 1st point, separated by a comma.");
		fgets(input , sizeof(input), stdin);
		sscanf(input ,"%lf,%lf", &(a.x), &(a.y));
		puts("Please enter the x and y coordinates of the 2nd point, separated by a comma.");
		fgets(input , sizeof(input), stdin);
		sscanf(input ,"%lf,%lf", &(b.x), &(b.y));
		
		// sscanf - updated to add addresses of both variables in each call, added consistent
		// types of floating point numbers and consistent format
	}

	double x_dist = (a.x - b.x), y_dist = (a.y - b.y);

	printf("The square of the distance between the two points is: %f\n", (x_dist*x_dist)+(y_dist*y_dist));

	return 0;
}

/*
1 mark for inlcuding type def to fix bug
2 marks for using %lf 
2 marks for putting input handling in seperate block 

5/5
*/
