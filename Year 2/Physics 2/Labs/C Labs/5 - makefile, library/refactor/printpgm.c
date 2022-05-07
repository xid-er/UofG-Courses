#include "printpgm.h"

// Prints the image to console
void print_img(int width, int height, int a[height][width]){
	// PGM Header
	printf("P2 %d %d %d\n", width, height, 255);

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
