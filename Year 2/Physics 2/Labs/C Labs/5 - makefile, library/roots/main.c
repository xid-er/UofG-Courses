/* A program that prints out all of the cube roots of a command-line
 * argument.
 */

#include "main.h"

int main(int argc, char * argv[]) {
	double x, cub_x, re_cub_x, im_cub_x;
	// Input double - x
	x = validate_input(argc, argv);

	// Get cube root, real, and imaginary parts
	cub_x = cbrt(x);
	re_cub_x = re_complexroots(cub_x);
	im_cub_x = im_complexroots(cub_x);

	// Print everything out
	printf("The cube roots of %lf are %lf, %lf + %lfi and %lf - %lfi.\n", x, cub_x, re_cub_x, im_cub_x, re_cub_x, im_cub_x );

	// Return successfully
	return 0;
}

/*

1 mark for readme file
3 marks for 3 .c files
2 marks for 2 header files and including in main 
2 marks for -lm in make file and including <math.h> and <stdio.h>
2 marks for comments 

10/10

*/

