#include <stdio.h> //included stdio.h for printf function

int k = 4;
int main(void){
	printf("outer k = %d\n", k); // scope - outside main(), global
	int i = 50;
	unsigned int j = i * 2;
	printf("main j = %d\n", j); // scope - main()
	double k = 1.0; //inserted semicolon
	{
		float i = 5.0;
		printf("The value of i is %3f\n", i); //i is 5.000000
		k = i * j;
		printf("inner k = %.2f\n", k); // scope - main(), changed in inner block
		i *= 6;
		printf("inner i = %.2f\n", i); // scope - inner block in main()
	}
	// deleted "double j;" because 
	i = k + i; //or i += k
	printf("Now , the value of i is %d\n", i); //i is 550, scope - main()
	return 0;
}


/*

3 marks for bug fixes

3 marks for correct print f statements with precision 

3 marks for correct comments for variables and scope 

1 for print f statements

10/10 

Well done!



*/
