/* Author: Karlis Siders, 2467273S
 * Worst PRNG: randu()
 *    Period test is so short that it does not reach the maximum.
 *    Spectral test shows a repeating pattern appearing, which shows that 
 *    it is not truly random.
 *    Roulette test shows that randu() is quite imprecise, with a large dip
 *    deviation around x=2.
 * Average PRNG: rand()
 *    Period test reaches maximum.
 *    Spectral test shows no visible pattern.
 *    Roulette test demonstrates that rand() is usually more precise than
 *    randu(), but it has an even bigger dip/deviation at x=31.
 * Best PRNG: Mersenne Twister (MT)
 *    Period test reaches maximum.
 *    Spectral test shows no visible pattern.
 *    Roulette test demonstrates MT's superior generator, as the deviations 
 *    seem completely random and do not have any large dips due to 
 *    unoptimised mathematics behind the algorithm.
 */



#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include "randu.h"
#include "MT.h"
#include "test.h"

//For setting the max loops we do in the period test
#define LOOPS 1000000000UL

//Test our three generators with period, spectral tests.
int main(void) {

	FILE * file;

	long seed = time(NULL);

	//seed PRNGs here
	// Randu
	srandu(seed);
	// Standard rand()
	srand(seed);
	// Mersenne Twister
	init_genrand(seed);

	//period tests here
	printf("The Period of function randu(), with seed %ld is %ld\n", seed, test_period(randu, LOOPS));
	printf("The Period of function rand(), with seed %ld is %ld\n", seed, test_period(rand, LOOPS));
	printf("The Period of function genrand_int32(), with seed %ld is %ld\n", seed, test_period(genrand_int32, LOOPS));
	


	//spectral tests here
	
	file = fopen("randuspectral.dat", "w");
	test_spectral(randu, 100000, 3, file);
	fclose(file);

	file = fopen("randspectral.dat", "w");
	test_spectral(rand, 100000, 3, file);
	fclose(file);

	file = fopen("MTspectral.dat", "w");
	test_spectral(genrand_int32, 100000, 3, file);
	fclose(file);

	//roulette tests here
	file = fopen("randuroulette.dat", "w");
	test_roulette(randu, 10000000, 36, file);
	fclose(file);

	file = fopen("randroulette.dat", "w");
	test_roulette(rand, 10000000, 36, file);
	fclose(file);

	file = fopen("MTroulette.dat", "w");
	test_roulette(genrand_int32, 10000000, 36, file);
	fclose(file);

	return 0; 
}



/*
2 marks for evidence of running original code 
4 marks for succesful copy of code which runs MT and rand()

3/4 marks for comments 
MT is the best
Rand second best
RandU - short period, horrible spectral issues some significant divergences

9/10


*/

