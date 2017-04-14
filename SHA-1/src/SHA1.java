public class SHA1{
	
	private String h0, h1, h2, h3, h4;
	private String A, B, C, D, E;
		
	private String string;
	private String[] msgDigest; 
	private String tempString;
	private String[] ascii;
	private static String[] chunks;
	private static String[] words;
	private String binary;
	private int lengthOfBinary;	
	
	
	public SHA1(String[] string){
		
		msgDigest = new String[string.length];
		
		for(int j = 0; j < string.length; j++){
						
			init();
			prepString(string[j]);
			appendLength();			
			chunkTo512(binary);		
			
			for(int i = 0; i < chunks.length; i++){
				chunkToWords(chunks[i]);
				extendTo80();
				generateBinaryDigest();
			}
			
			msgDigest[j] = convertBinaryDigestToHex();
			displayMsgDigest(string, j);
			
		}
	}
	
	public SHA1(String str){
		
		String msgDigest = ""; 
		
		init();
		string = str;
		prepString(string);
		appendLength();			
		chunkTo512(binary);		
		
		for(int i = 0; i < chunks.length; i++){
			chunkToWords(chunks[i]);
			extendTo80();
			generateBinaryDigest();
		}
		
		msgDigest = convertBinaryDigestToHex();
		displayMsgDigest(msgDigest);
	}
	
	private void init(){
		
		h0 = "01100111010001010010001100000001";
		h1 = "11101111110011011010101110001001";
		h2 = "10011000101110101101110011111110";
		h3 = "00010000001100100101010001110110";
		h4 = "11000011110100101110000111110000";
		
		string = "";		
		tempString = "";					
		binary = "";					
	}
	
	private void prepString(String string){
		ascii = new String[string.length()];
		
		for(int i = 0; i < string.length(); i++){
			int val = (int)string.charAt(i);			
			
			ascii[i] = "";

			while(val > 0){
				tempString +=  val % 2;
				val /= 2;
			}

			int val1 = 8 - tempString.length();			
			if((val1) > 0){			
				for(int k = 0; k < val1; k++)
					tempString += "0";
			}				
						
			ascii[i] = reverseString(tempString);				
			tempString = "";
		}					

		for(int i = 0; i < ascii.length; i++)
			binary += ascii[i];
		
		lengthOfBinary = binary.length();
		
		int initLength = 448;		
		while(lengthOfBinary >= initLength){
			initLength += 512;
		}
		
		binary += "1";						
		int zeroesToAdd = initLength-binary.length();
		
		for(int i = 0; i < zeroesToAdd; i++)
			binary += "0";
	}
	
	private String convertBinaryDigestToHex(){
		return convertToHex(h0) + convertToHex(h1) + convertToHex(h2) + convertToHex(h3) + convertToHex(h4);
	}
	
	private void generateBinaryDigest(){
		A = h0; B = h1; C = h2; D = h3; E = h4;

		String F = "", K = "";
		String temp = "";
		
		for(int i = 0 ; i < 80; i++){
			
			if(i >= 0 && i <= 19){											
				F = function1(B,C,D);				
				K = "01011010100000100111100110011001";	
					 
			}						
			if(i >= 20 && i <= 39){
				F = function2(B, C, D);
				K = "01101110110110011110101110100001";					 
			}
			
			if(i >= 40 && i <= 59){
				F = function3(B, C, D);
				K = "10001111000110111011110011011100";
			}
			
			if(i >= 60 && i <= 79){
				F = function2(B,C,D);
				K = "11001010011000101100000111010110";					 
			}			

			temp = addBinary(addBinary(addBinary(addBinary(leftRot(5, A), F), E ), K ), words[i]);						
			
			if(temp.length() - 32 > 0)
				temp = temp.substring(temp.length()-32, temp.length());				
			
			E = D;
			D = C;
			C = leftRot(30, B);
			B = A;
			A = temp;

		}
					
		h0 = addBinary(h0, A);		
		h1 = addBinary(h1, B);
		h2 = addBinary(h2, C);	
		h3 = addBinary(h3, D);	
		h4 = addBinary(h4, E);
		
		if(h0.length() > 32)
			h0 = h0.substring(h0.length()-32, h0.length());
		if(h1.length() > 32)
			h1 = h1.substring(h1.length()-32, h1.length());
		if(h2.length() > 32)
			h2 = h2.substring(h2.length()-32, h2.length());
		if(h3.length() > 32)
			h3 = h3.substring(h3.length()-32, h3.length());
		if(h4.length() > 32)
			h4 = h4.substring(h4.length()-32, h4.length());			
		
	}

	private String function1(String B, String C, String D){				
		return OR(AND(B, C), AND(NOT(B), D));
	}
	
	private String function2(String B, String C, String D){
		return XOR(XOR(B, C), D);
	}
	
	private String function3(String B, String C, String D){	
		return OR(OR(AND(B, C), AND(B, D)), AND(C, D));
	}
	
	private String OR(String A, String B){
		
		String AorB = "";
		
		for(int i = 0 ; i < A.length(); i++){
			if(A.toCharArray()[i] == '0'){
				if(B.toCharArray()[i] == '0')
					AorB += 0;
				if(B.toCharArray()[i] == '1')
					AorB += 1;
			}else{				
					AorB += 1;				
			}
						
		}
		
		return AorB;		
	}
	
	private String NOT(String A){
		
		String notA = "";		
		
		for(int j = 0; j < A.length(); j++){
			if(A.toCharArray()[j] == '0')
				notA += 1;
			else
				notA += 0;
		}
		
		return notA;
	}
	
	private String AND(String A, String B){
	
		String AandB = "";
		
		for(int i = 0; i < A.length(); i++){
			if(A.toCharArray()[i] == '1'){
				if(B.toCharArray()[i] == '0')
					AandB += 0;
				if(B.toCharArray()[i] == '1')
					AandB += 1;
			}else				
				AandB += 0;									
		}
		
		return AandB;
	}
	
	private String XOR(String word1, String word2){
		
		String result = "";
		
		for(int i = 0; i < 32; i++){
			
			if(word1.toCharArray()[i] == '1'){
				if(word2.toCharArray()[i] == '1')
					result += 0;
				if(word2.toCharArray()[i] == '0')
					result += 1;				
			}
			
			if(word1.toCharArray()[i] == '0'){
				if(word2.toCharArray()[i] == '1')
					result += 1;
				if(word2.toCharArray()[i] == '0')
					result += 0;
			}
		}
		
		return result;
	}
	
	private String leftRot(int val, String A){		
		return A.substring(val, A.length()) + A.substring(0, val);
	}
	
	private String leftRotation(String result){		
		return result.substring(1, result.length()) + result.toCharArray()[0];
	}
	
	
	private String addBinary(String sum1, String sum2){
		
		long num1 = Long.parseLong(sum1, 2);
		long num2 =  Long.parseLong(sum2, 2);
		long sum = (num1 + num2);				
		
		String temp = toBinaryString(sum);		
		
		if(temp.length() < sum1.length()){
			
			int length = sum1.length()-temp.length();
			
			for(int i = 0; i < length; i++){
				temp = "0" + temp;
			}
		}
		
		temp = "1" + temp;
		
		return temp;
	}
	
	private String toBinaryString(long number){						
		
		String str = "";
		
		while(number > 0){
			str += number % 2;
			number /= 2;			
		}
		
		str = reverseString(str);
		
		return str;
	}
	
	private String reverseString(String str){
		String reversedString = "";
		
		for(int i = str.length()-1; i >= 0; i--){
			reversedString += str.charAt(i);
		}

		return reversedString;
	}
	
	private String convertToHex(String binaryStr){				
		return Long.toString(Long.parseLong(binaryStr, 2), 16);
	}
	
	
	private void appendLength(){
		
		String temp = "";
		
		while(lengthOfBinary > 0){
			
			temp += lengthOfBinary % 2;
			lengthOfBinary /= 2;
			
		}		
		
		if(temp.length() < 64){
			int val = 64-temp.length();			
			
			for(int i = 0; i < val; i++)
				temp += "0";
		}
				
		binary += reverseString(temp);

	}
	
	private void chunkTo512(String binary){
		
		chunks = new String[binary.length()/512];
		
		int index = 0;
		int index2 = index + 512;
		
		for(int i = 0; i < binary.length()/512; i++){
			
			chunks[i] = binary.substring(index, index2);
			
			index = index2;
			index2 = index2 + 512;
		}
	}
	
	private void chunkToWords(String binary){
		
		words = new String[80];
				
		int index = 0;
		int index2 = index + 32;
		
		for(int i = 0; i < 16; i++){
			
			words[i] = binary.substring(index, index2);
			
			index = index2;
			index2 = index2 + 32;
		}			
			
	}
	
	private void extendTo80(){
		
		String result;
		
		for(int i = 16; i < 80; i++){
			
			result = XOR(XOR(XOR(words[i-3], words[i-8]), words[i-14]), words[i-16]);
			result = leftRotation(result);
			words[i] = result;					
		}
		
	}	
	
	private void displayMsgDigest(String msgDigest){		
		System.out.println("\n\t\t  String Digest:\n\t\t     " + msgDigest + "\n\n");		
	}
	
	private void displayMsgDigest(String[] string, int j){		
		System.out.println("\n\t\t   String Digest for \"" + string[j] + "\":\n\t\t     " + msgDigest[j] + "\n");		
	}
	
}
