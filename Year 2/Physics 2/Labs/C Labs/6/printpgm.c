#include "printpgm.h"

// Makes image file
void make_img(int width, int height, int a[height][width]){

	// Open file in text mode
	FILE * fp = fopen("./image.pgm", "w");

	// PGM Header
	fprintf(fp, "P2 %d %d %d\n", width, height, 255);

	// Loop for adding to image file
	int i = 0;
	for(i; i < height; i++){
		int j = 0;
		for(j; j < width; j++){
			// Print each row
			fprintf(fp, "%d ", a[i][j]);
		}
		// New line after row ends
		fputs("\n", fp);
	}

	fclose(fp);
	fp = NULL;
}

void make_bin_img(int width, int height, int a[height][width]){

	// Open file in binary mode
	FILE * fp = fopen("./bin_image.pgm", "wb");

	// PGM Header
	fprintf(fp, "P5 %d %d %d\n", width, height, 255);

	// Loop for adding to image file
	int i = 0;
	for(i; i < height; i++){
		int j = 0;
		for(j; j < width; j++){
			// Print each row
			fwrite(&(a[i][j]), 1, 1, fp);
		}
	}
	fclose(fp);
	fp = NULL;
}


/*

1 mark for code compiles without warnings

1st function
2 marks for correctly opening output with 'w' mode
1 mark: fprintfs replace printfs appropriately
1 mark: correctly close file

2nd binary function
2 mark: correctly open file for output with 'wb' mode

3 marks: either solution for writing a good binary file is implemented
1 mark: correctly close file.

comments & modifications
1 marks:  modify code to output both text and binary files

3 marks: any insightful comments on the two files and their differences (the binary file is smaller - because the text version represents 1 byte values as up to 3 bytes (3 digits in ASCII); the text version is human readable, whilst only the header for the binary version is; etc) Be generous here.

5 marks for comments.

20/20

*/
