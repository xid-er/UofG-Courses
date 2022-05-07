#include <stdio.h>
#include <string.h>

int main(){
	puts("Let's sing a song!");
	int bottles;
	int scanned;
	// Gets a number between 1 and 99 from input
	do{
		puts("Enter a number between 1 and 99: ");
		char buf[100];
		fgets(buf, sizeof(buf), stdin);
		scanned = sscanf(buf,"%d",&bottles);
		if (!scanned || bottles < 1 || bottles > 99){
			puts("Naughty!");
		}
	} while (!scanned || bottles < 1 || bottles > 99);

	// Song loop
	for(bottles; bottles > 0; bottles--){
		if (bottles == 1){
			printf("%d Green Bottle, sitting on the wall,\n", bottles);
			printf("%d Green Bottle, sitting on the wall,\n", bottles);
		}
		else{
			printf("%d Green Bottles, sitting on the wall,\n", bottles);
			printf("%d Green Bottles, sitting on the wall,\n", bottles);
		}
		puts("and if 1 Green Bottle should accidentally fall...");
		if (bottles == 2){
			puts("... there'll be 1 Green Bottle, sitting on the wall.");
		}
		else if (bottles == 1){
			puts("... there'll be no Green Bottles, sitting on the wall.");
		}
		else{
			printf("... there'll be %d Green Bottles, sitting on the wall.\n", bottles - 1);
		}
		puts("------------------------");
	}
}

*/

/* Nice intro :)
1 mark - interior of loop is correct (song lyrics + number)
2 marks - working loop
2 marks - appropriate input handling with bounds checking
1 mark - working handling of special case for 1 Green Bottle (no plural)
1 mark for code compiling with no errors

3 marks for comments and nicely written code


10/10

*/
