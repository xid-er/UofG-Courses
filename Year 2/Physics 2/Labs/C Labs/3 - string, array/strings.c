#include <stdio.h>
#include <ctype.h>
#include <string.h>

/* A program that gets a string as input,
 * returns its length and changes it all to lowercase,
 * if applicable.
 * Author: Karlis Siders
*/

int main(){
	// Input
	puts("Enter something!");
	char str[100];
	fgets(str, sizeof(str), stdin);
	
	// Length
	int len = strlen(str) - 1;
	printf("The length is: %d\n", len);
	
	// Turn to lowercase (and keep track)
	int low = 1;
	int i = 0;
	for(i; i < len; i++){
		if(isupper(str[i])) low = 0;
		str[i] = tolower(str[i]);
	}
	
	// Return lowercase string (if it wasn't before)
	if(low) puts("This string was all lowercase!");
	else printf("All lowercase: %.100s", str);
	
	return 0;
}

/*
1 mark - code compiles without warnings (with -Wall)
-1 mark - correct char array / string declaration with correct dimensions (101 for the null)
1 mark - correct input handling 
1 mark - correct includes stdio.h ctype.h
2 marks - correct loop over characters with test and return
1 mark - correct output

9/10

*/
