#include <stdio.h>

struct TwoDPoint {
	double x, y;
};
// Added line to typedef struct used in line 10
typedef struct TwoDPoint TwoDPoint_t;

int main(void) {
	TwoDPoint_t points[] = {{0.0, 0.0}, {0.0, 0.0}};
	int n = sizeof(points) / sizeof(points[0]);
	
	// The input handling is in a separate block to inrease legibility and understanding of
	// the code. It does not need to be there, but is appreciated by any analysts/debuggers.
	{
		char input [100];
		for (int i = 0; i < n; i++){
			printf("Please enter the x and y coordinates of point no. %d, separated by a comma.\n", i+1);
			fgets(input , sizeof(input), stdin);
			sscanf(input ,"%lf,%lf", &(points[i].x), &(points[i].y));
		}
		
		
		// sscanf - updated to add addresses of both variables in each call, added consistent
		// types of floating point numbers and consistent format
	}

	double x_dist = points[0].x - points[1].x;
	double y_dist = points[0].y - points[1].y;

	printf("The square of the distance between the two points is: %f\n", (x_dist*x_dist)+(y_dist*y_dist));

	return 0;

}

/*

1 mark for correct declaration of structs 
4 marks for using a loop to handle input

5/5

*/


