#include <stdio.h>

int main(void) {
	int i,*myPtr , **myPtrPtr;
	int a[2] = {1,2};
	i = 6;
	myPtr = &i;
	myPtrPtr = &myPtr;

	{
		int i = 7;
		*myPtr = 45; // Changes value of bigger scope's i 
		// Prints value of i (changed in previous line) and its location
		printf("The value %d is stored in location %p\n", *myPtr, myPtr);
		// Prints value of local i and its address
		printf("The value %d is stored in location %p\n", i, &i );
	}

	// Prints dereferenced value of myPtrPtr, which is the address of myPtr
	// Modified to add root value of i (with %d and **myPtrPtr)
	printf("The value %p is stored in the location %p, the root value of which is %d\n", *myPtrPtr, myPtrPtr, **myPtrPtr);
	// Prints value of bigger scope's i and its address (to which myPtr points)
	printf("The value %d is stored in location %p\n", i, &i);

	// a - pointer to the first element
	// &a - address of the first element
	// *a - dereferenced address of first element, its value
	// a[0] - value of first element
	// a[1] - value of second element
	printf("%p, %p, %d, %d, %d\n", a, &a, *a, a[0], a[1]);

	return 0;

}


// 2 marks for correctly placed * and &
// 2 marks for printf descriptions
// 1 mark for myPtrPtr is a pointer which points to another pointer
// 1 mark for **myPtrPtr
// 1 mark for %d
// 1 mark for priting out array correctly
// 2 marks for noticing a = &a = pointer to 0th element of array a 

// 10/10
