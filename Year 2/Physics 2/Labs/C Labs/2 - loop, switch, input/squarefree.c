#include <stdio.h>
#include <string.h>
#include <math.h>

int main(){
	puts("Let's check for squarefree numbers!");
	int n;
	int scanned = 0;
	// Gets an integer from input
	do{
		puts("Enter an integer (will be read as one): ");
		char buf[100];
		fgets(buf, sizeof(buf), stdin);
		scanned = sscanf(buf,"%d",&n);
		if (!scanned){
			puts("Naughty!");
		}
		
		if (scanned){
			// Squarefree test loop
			int i = 2;
			int sqrFree = 1;
			for(i; (double)(i*i) <= sqrt((double) n); i++){
				if (n % (i*i) == 0){
					printf("%d is not squarefree.\n", n);
					sqrFree = 0;
					break;
				}
			}
			if (sqrFree) {
				printf("%d is squarefree.\n", n);
			}

			// Do again?
			int answered = 0;
			do{
				puts("Try another number? (y/n)");
				char buf[100];
				fgets(buf, sizeof(buf), stdin);
				switch(buf[0]){
					case 'y':
						scanned = 0;
						answered = 1;
						break;
					case 'n':
						puts("Goodbye!");
						answered = 1;
						break;
					default:
						puts("Please answer with 'y' or 'n'.");
				}
			} while (!answered);
		}
		
	} while (!scanned);
}


/*

1 mark - code compiles without warnings (without -Wall)
1 mark - correct input handling 
2 marks - correct test implementation from flowchart  1 mark docked because it is not exactly the same as the flow chart
2 marks - correct loop handling 

3 marks for comments and nicely written code 

9/10 

*/
