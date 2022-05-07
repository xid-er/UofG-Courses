#include <stdio.h>

/* A program that makes a PGM chess board-style image.
 * Author: Karlis Siders
*/

int main(){
	// Dimensions, Array, Pixel Metadata
	int width = 255;
	int height = 255;
	int image[width][height];
	unsigned int min = 0;
	unsigned int max = 255;
	
	// PGM Header
	printf("P2 %d %d %d\n", width, height, max);
	
	// Loop for chess board-esque image
	int i = 0;
	for(i; i < height; i++){
		int j = 0;
		for(j; j < width; j++){
			if((i + j) % 2 == 0) image[i][j] = max;
			else image[i][j] = min;
			printf("%d ", image[i][j]);
		}
		puts("");
	}
	
	return 0;
}

/*

1 mark - code compiles without warnings (with -Wall)
1 mark - correct array declaration
2 marks - correct nested for loops to write data to array 
2 marks - correct nested for loops to read data from array to file
1 mark - output is a valid PGM file which can be displayed

3 marks for comments and indentation

10/10

*/


