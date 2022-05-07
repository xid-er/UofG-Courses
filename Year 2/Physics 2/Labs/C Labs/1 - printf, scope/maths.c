#include <stdio.h>

int main(){
	const int i = 20;
	
	printf("20 + 400 = %d\n", i + 400);
	printf("20 - 89 = %d\n", i - 89);
	
	printf("20 * 66.85 = %d\n", (int) (i * 66.85));
	//cast to int because a float is used in operation and result is int
	
	printf("40420 / 20 = %d\n", 40420 / i);
	printf("80085 %% 20 = %d\n", 80085 % i);
}



/*

5 marks for operations

2 marks docked for code that does not compile without warning 

printf("80085 % 20 = %d\n", 80085 % i);
} // need to escap the % symbol by %% for code to work

3 marks for style and comments 

8/10

*/
