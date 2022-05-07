/* A program that draws a regular polygon with the turtle library.
 * Input: Number of sides as command-line argument.
 * Author: Karlis Siders, 2467273S
 */

#include <stdio.h>
#include <string.h>
#include "/usr/share/p2t/lab05/Exe2/turtle.h"

int main(int argc, char *argv[]){
	double n;

	// If the program's name is 'square', draw a square
	if (strcmp(argv[0], "./square") == 0){
		n = 4;

	// Otherwise, read first command-line argument (if given) as the no.
	// of sides
	} else {
		if (argc == 1){
			puts("Please enter no. of sides as argument.");
			return 1;
		}

		int scanned = sscanf(argv[1], "%lf", &n);
		if (scanned == 0 || n < 1){
			puts("The argument should be a positive integer.");
			return 1;
		}
	}

	// Initialise the drawing board
	int dim = 256;
	turtle_init(dim, dim);

	// Choose length of side relative to board
	double length = dim / n * 1.5;

	// Draw polygon by going forward by 'length' and turning by 360/n
	// degrees n times
	int i = 0;
	for (i; i < n; i++){
		turtle_forward(length);
		double degrees = 360 / n;
		turtle_turn_right(degrees);
	}

	// Save drawn polygon as mylogo.png
	turtle_save_png("mylogo.png");
}


/*
1 mark for code that compiles
1 mark for crrect headers
2 marks for using functions imported from turtle.h
2 marks for make file
1 mark for input handling
3 marks for comments and indentation

10/10

*/
