#include <stdio.h>
#include <string.h>

/* A program that makes a PGM chess board-style image.
 * Version: 2.0
 * Author: Karlis Siders
 */

// Fills the array
void fill_img(int width, int height, int a[height][width]){
	// Pixel Metadata
	unsigned int min = 0;
	unsigned int max = 255;

	// PGM Header
	printf("P2 %d %d %d\n", width, height, max);

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

// Prints the image to console
void print_img(int width, int height, int a[height][width]){
	// Loop for printing image to console
	int i = 0;
	for(i; i < height; i++){
		int j = 0;
		for(j; j < width; j++){
			// Print each row
			printf("%d ", a[i][j]);
		}
		// New line after row ends
		puts("");
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


// 1 mark for code the compiles with no warnings

// 5 marks for two correct functions

// 1 mark for additional -h option works 

// 3 marks for comments and methodology 

// 10/10 

// Well done. Full marks on all Clab  4 exercises 
