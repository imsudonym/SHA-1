import java.util.InputMismatchException;
import java.util.Scanner;

public class SHA1Main{

	public static void main(String[] args){
		
		int choice;				
		
		do{
			System.out.println("\t\t---------------------------------------------------------");
			System.out.println("\t\t|\t\tCryptographic Hash Function\t\t|");
			System.out.println("\t\t---------------------------------------------------------");
			System.out.println("\t\t [1] Enter a string");
			System.out.println("\t\t [2] Enter a set of strings");
			System.out.print("\t\t  Enter choice: ");
			
			Scanner scan = new Scanner(System.in);
			try{
				
				choice = scan.nextInt();
				scan.nextLine();
				
				if(choice == 1){
				
					System.out.println();
					System.out.println("\t\t---------------------Hash A String---------------------");
					System.out.print("\t\t  Enter String: ");
					String str = scan.nextLine();
					new SHA1(str);
					
				}				
				else if(choice == 2){
					
					System.out.println();
					System.out.println("\t\t------------------Hash A Set of String------------------");
					System.out.print("\t\t  Enter how many strings: ");
					int num = scan.nextInt();
					scan.nextLine();
					
					String[] setOfStr = new String[num];
					for(int i = 0; i < num; i++){
						System.out.print("\t\t  Enter String " + (i + 1) + ": ");
						setOfStr[i] = scan.nextLine();
					}			
					
					new SHA1(setOfStr);
					
				}else
					System.out.println("\n\t\t Invalid Input. Please try again.\n");
				
				
			}catch(InputMismatchException e){
				System.out.println("\n\t\t Invalid Input. Please try again.\n");				
			}

		}while(true);
			
	}
}