/* Reads in a data file consisting of 2 columns of 100 integer numbers
* Author - 2467273
* Assumes columns are separated by a comma.
*/

#include <stdio.h>

int main(void){
	FP * fp = fopen("data.dat", "r");
	int columns = 2
	int rows = 100;
	int data[rows][columns];
	
	int i = 0;
	for (i; i < rows; i++){
		char line[100];
		fgets(line, sizeof(line), stdin);
		sscanf(line, "%d,%d", &data[i][0], &data[i][1]);
	}
	fclose(fp);
}