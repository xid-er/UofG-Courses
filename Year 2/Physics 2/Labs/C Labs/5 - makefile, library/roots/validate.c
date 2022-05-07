#include "validate.h"

double validate_input(int argc, char * argv[]) {
	// Check for no. of arguments - should be 1 additional
	// If not, exit with failure
	if (2 != argc) {
		fputs("Too many inputs - just enter one double!\n", stderr );
		exit(1);
	}

	// Parse the input into a double
	double val;
	int parsed = sscanf(argv[1], "%lf", &val);
	
	// If not parsed, exit with failure
	if (0 == parsed) {
		fputs("Could not parse input as a double!\n", stderr);
		exit(1);
	}
	return val;
}
