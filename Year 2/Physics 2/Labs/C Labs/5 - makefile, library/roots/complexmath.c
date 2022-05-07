#include "complexmath.h"

double re_complexroots(double z) {
	return -1.0 * z / 2.0;
}

double im_complexroots(double z) { 
	return sqrt(3.0) * (z / 2.0); 
}
