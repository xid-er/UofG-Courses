#include <string.h>
#include <stdio.h>

int main(){
	puts("Welcome to the Calculator program!");
	double first,second,res;
	int scanned;
	// Gets two numbers as input
	do{
		puts("Enter 2 comma-separated numbers: ");
		char buf[100];
		fgets(buf, sizeof(buf), stdin);
		scanned = sscanf(buf,"%lf,%lf",&first,&second);
		if (scanned != 2){
			puts("Did not successfully interpret both values!");
		}
	} while(scanned != 2);

	// Gets operation and calculates result
	do{
		puts("Enter operation: ");
		char buf[100];
		fgets(buf, sizeof(buf), stdin);
		scanned = 1;
		switch(buf[0]){
			case '+':
				res = first + second;
				break;
			case '-':
				res = first - second;
				break;
			case '*':
				res = first * second;
				break;
			case '/':
				res = first / second;
				break;
			default:
				puts("Did not recognise operation.");
				scanned = 0;
		}
	} while (!scanned);
	
	// Prints result
	printf("Result: %.2f", res);
}

/*

2 mark - correct prompt and input handling
3 marks - working flow control structure 
1 mark - working printf for output
1 mark - code compiles without warnings (without -Wall)
1 mark of above for handling "default" or unexpected input (like invalid operations)
3 mark for comments and correctly indented code




10/10

*/
