#include <string.h>
#include "printpgm.h"

/* A program that makes a PGM chess board-style image.
 * Version: 2.0
 * Author: Karlis Siders
 */

// Fills the array
void fill_img(int width, int height, int a[height][width]){
	// Pixel Metadata
	unsigned int min = 0;
	unsigned int max = 255;

	// Loop for filling chess board-esque image
	int i = 0;
	for(i; i < height; i++){
		int j = 0;
		for(j; j < width; j++){
			if ((i + j) % 2 == 0) a[i][j] = max;
			else a[i][j] = min;
		}
	}
}

int main(int argc, char * argv[]){

	// Check for -h argument
	if (argc == 2 && strcmp(argv[1], "-h") == 0){
		puts("This program makes a PGM chess board-style image.");
	} else {

		// Dimensions, Array
		int width = 20;
		int height = 20;
		int image[width][height];
		
		// Fill array and print image to console
		fill_img(width, height, image);
		print_img(width, height, image);
	}

	return 0;
}


/*
1 mark code compiles
1 mark for suitable make file
2 marks for function in new file
2 marks header file
1 mark for include statemet

3 marks for comments and code aesethics


*/
