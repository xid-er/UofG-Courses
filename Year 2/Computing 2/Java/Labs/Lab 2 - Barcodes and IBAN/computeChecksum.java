int computeChecksum (String iban){
	boolean valid = false;
	iban = iban.replaceAll(" ", "").toUpperCase();
	int len = iban.length();
	if (len < 2){
		System.out.println("Too short to be a valid IBAN.");
	} else {
		String country = iban.substring(0,2);
		switch(country){
			case "GB":
				if (len == 22){
					valid = true;
				} else{
					System.out.println("Invalid IBAN length: "+len);
				};
				break;
			case "GR":
				if (len == 27){
					valid = true;
				} else{
					System.out.println("Invalid IBAN length: "+len);
				};
				break;
			case "SA":
				if (len == 24){
					valid = true;
				} else{
					System.out.println("Invalid IBAN length: "+len);
				};
				break;
			case "CH":
				if (len == 21){
					valid = true;
				} else{
					System.out.println("Invalid IBAN length: "+len);
				};
				break;
			case "TR":
				if (len == 26){
					valid = true;
				} else{
					System.out.println("Invalid IBAN length: "+len);
				};
				break;
			default:
				System.out.println("Unknown country code: "+country);
		};
	};
	if (valid){
		String rearranged = iban.substring(4) + iban.substring(0,2) + "00";
		String digits = "";
		for (int i = 0; i < rearranged.length(); i++){
			char c = rearranged.charAt(i);        
			if(Character.isDigit(c) || Character.isLetter(c)){
				digits += Character.getNumericValue(c);
			} else{
				System.out.println("Invalid character in IBAN: "+c);
				return -1;
			}
		}
		int mod = new BigInteger(digits).mod(BigInteger.valueOf(97)).intValue();
		return 98 - mod;
	}else{
		return -1;
	}
}