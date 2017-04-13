import java.math.BigInteger;

public class Sha1{
	
	private static String h0 = "01100111010001010010001100000001";
	private static String h1 = "11101111110011011010101110001001";
	private static String h2 = "10011000101110101101110011111110";
	private static String h3 = "00010000001100100101010001110110";
	private static String h4 = "11000011110100101110000111110000";
	
	private static String string = "A Test";
	private static String tempString = "";
	private static String[] ascii;
	private static String[] words = new String[80];
	private static String binary = "";
	private static int lengthOfBinary;
	
	
	public Sha1(){
					
		ascii = new String[string.length()];
		
		for(int i = 0; i < string.length(); i++){
			int val = (int)string.charAt(i);			
			
			ascii[i] = "";
			
			//	decimal ascii of each character to binary ascii
			while(val > 0){
				tempString +=  val%2;
				val /= 2;
			}
			
			// make sure binary digits are of length 8
			int val1 = 8 - tempString.length();			
			if((val1) > 0){			
				for(int k = 0; k < val1; k++)
					tempString += "0";
			}				
						
			ascii[i] = reverseString(tempString);				

			tempString = "";
		}
					
		//	put the values together
		for(int i = 0; i < ascii.length; i++)
			binary += ascii[i];
		
		lengthOfBinary = binary.length();		
		binary += "1";		
		
		int zeroesToAdd = 448-binary.length();
		
		for(int i = 0; i < zeroesToAdd; i++)
			binary += "0";				
		
		appendLength();		
		chunkToWords(binary);
		extendTo80();		
		
		String msgDigest = generateDigest();
		System.out.println("msgDigest = " + msgDigest);
	}
	

	private String generateDigest(){
		String A = h0, B = h1, C = h2, D = h3, E = h4;
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
		
		System.out.println("h0: " + h0);
		System.out.println("A: " + A);		

		h0 = addBinary(h0, A);
		System.out.println("h0: " + h0);
		
		System.out.println("h1: " + h1);
		System.out.println("B: " + B);
		
		h1 = addBinary(h1, B);
		System.out.println("h1: " + h1);
		
		System.out.println("h2: " + h2);
		System.out.println("C: " + C);
		
		h2 = addBinary(h2, C);
		System.out.println("h2: " + h2);
		
		System.out.println("h3: " + h3);
		System.out.println("D: " + D);
		
		h3 = addBinary(h3, D);
		System.out.println("h3: " + h3);
		
		System.out.println("h4: " + h4);
		System.out.println("E: " + E);
		
		h4 = addBinary(h4, E);
		System.out.println("h4: " + h4);
		
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
		
		System.out.println("\n\nh0: " + h0 + "\nh1: " + h1 + "\nh2: " + h2 + "\nh3: " + h3 + "\nh4: " + h4);
		
		System.out.println("Invoke convertToHex()");
		String msgDigest = convertToHex(h0) + convertToHex(h1) + convertToHex(h2) + convertToHex(h3) + convertToHex(h4);
		
		return msgDigest;
	}
	
	private String leftRot(int val, String A){
		//System.out.println("Left rotation " + val + ": " + (A.substring(val, A.length()) + A.substring(0, val)));
		//System.out.println("VAAAAAAAAAAAL HEEERRRRRRE: " + val);
		//System.out.println("LEngth of String A: " +  A.length());
		String temp = A.substring(0, val);
		return A.substring(val, A.length()) + temp;
	}
		
	private String addBinary(String sum1, String sum2){
		
		//System.out.println("	||Inside addBinary");
		//System.out.println("	||sum1: " + sum1);
		//System.out.println("	||sum2: " + sum2);		
		
		long num1 = Long.parseLong(sum1, 2);
		long num2 =  Long.parseLong(sum2, 2);
		
		//System.out.println("	||num1: " + num1);
		//System.out.println("	||num2: " + num2);
		
		long sum = (num1 + num2);		

		return toBinaryString(sum);
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
		
		//System.out.println("reversedString: " + reversedString);
		return reversedString;
	}
	
	private String convertToHex(String binaryStr){				
		return Long.toString(Long.parseLong(binaryStr, 2), 16);
	}
	
	private String function1(String B, String C, String D){		
		System.out.println("---------------------------");
		
		System.out.println("!B: " + NOT(B) + "\nD: " + D);
		System.out.println("!B AND D: " + AND(NOT(B), D));
		
		System.out.println("B: " + B + "\nC: " + C);
		System.out.println("B AND C: " + AND(B, C));
		
		System.out.println("(!B AND D) or (B AND C): " + OR(AND(B, C), AND(NOT(B), D)));
		
		System.out.println("---------------------------");
		
		return OR(AND(B, C), AND(NOT(B), D));
	}
	
	private String function2(String B, String C, String D){
		System.out.println("---------------------------");
		
		System.out.println("B: " + B + "\nC: " + C);
		System.out.println("B XOR C: " + XOR(B, C));
		System.out.println("B XOR C XOR D: " + XOR(XOR(B, C), D));
		
		System.out.println("---------------------------");		
		
		return XOR(XOR(B, C), D);
	}
	
	private String function3(String B, String C, String D){
		
		System.out.println("---------------------------");
		
		System.out.println("B: " + B + "\nC: " + C);
		System.out.println("B AND C: " + AND(B, C));			
		
		System.out.println("B: " + B + "\nD: " + D);
		System.out.println("B AND D: " + AND(B, D));
				
		System.out.println("(B AND C) OR (B AND D): " + OR(AND(B, C), AND(B, D)));
		
		System.out.println("C: " + C + "\nD: " + D);
		System.out.println("C AND D: " + AND(C, D));
				
		System.out.println("(B AND C) OR (B AND D) OR (C AND D): " + OR(OR(AND(B, C), AND(B, D)), AND(C, D)));
				
		System.out.println("---------------------------");
		
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
	
	private void appendLength(){
		String temp = "";
		while(lengthOfBinary > 0){
			temp += lengthOfBinary%2;
			lengthOfBinary /= 2;
		}		
		
		if(temp.length() < 64){
			int val = 64-temp.length();			
			
			for(int i = 0; i < val; i++)
				temp += "0";
		}
		
		for(int i = temp.length()-1; i >= 0; i--)
			binary += temp.charAt(i);
	}
	
	private static void chunkToWords(String binary){
		
		int index = 0;
		int index2 = index + 32;
		for(int i = 0; i < 16; i++){
			
			words[i] = binary.substring(index, index2);
			
			index = index2;
			index2 = index2+32;
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
	
	private String leftRotation(String result){
		char c = result.toCharArray()[0];		
		
		return result.substring(1, result.length()) + c;
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
	
	public static void main(String[] args){		
		new Sha1();
	}
	
	
			
	
}