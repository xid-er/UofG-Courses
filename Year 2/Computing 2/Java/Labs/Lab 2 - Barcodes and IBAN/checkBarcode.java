boolean checkBarcode (String barcode){
	int n = barcode.length();
	boolean valid = true;
	if(n != 8 && n != 13){
		valid = false;
	}
	else{
		char[] digits = barcode.toCharArray();
		int i = 0;
		int sum = 0;
		while(valid && i <= n-2){
			int pos = (n-2) - i;
			char digitChar = digits[pos];
			if(Character.isDigit(digitChar)){
				int weight = 1 + ((i + 1) % 2) * 2;
				int digitInt = Character.getNumericValue(digitChar);
				sum += digitInt * weight;
				i++;
			}
			else{
				valid = false;
				break;
			};
		};
		int remainder = sum % 10;
		if( ! Character.isDigit(digits[n-1]) || (10 - remainder) % 10 != Character.getNumericValue(digits[n-1])){
			valid = false;
		};
	};
	return valid;
}