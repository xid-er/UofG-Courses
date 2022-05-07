#include <stdio.h>

/* A program that outputs the factorial of all
 * integer-based command-line arguments.
 * Author: Karlis Siders
 */

// Iterative factorial
int factorial(int n){
	int res = 1;
	for (n; n > 1; n--){
		res *= n;
	}
	return res;
}

// Recursive factorial
int rfactorial(int n){
	// Base case
	if (n == 1) return 1;
	// Recursive case
	else {
		return n * rfactorial(n-1);
	}
}

int main(int argc, char * argv[]){
	
	// Check if there are arguments given
	if (argc == 1){
		puts("There should be at least 1 argument.");
		return 1;
	}
	
	// Main loop, start from 2nd argument
	int i = 1;
	for(i; i < argc; i++){
		// Cast into integers
		int num;
		int scanned = sscanf(argv[i], "%d", &num);
		if (scanned == 0 || num < 1){
			puts("All arguments should be positive integers.");
			return 1;
		}
		// Call recursve function
		// CHANGE AS APPROPRIATE
		int fact = rfactorial(num);
		printf("Factorial of %d is %d\n", num, fact);
	}

	return 0;
}


// 1 mark for code compiles without warning

// 2 marks for working factorial function

// 3 marks for CLI handling

// 1 mark for working recursive factorial function

// 3 marks for comments

// 10/10
