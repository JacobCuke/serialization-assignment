package serialization;

import java.util.Scanner;

public class ObjectCreator {
	
	public static void main(String[] args) {
		
		ObjectCreator oc = new ObjectCreator();
		oc.runMenu();
	}
	
	private void runMenu() {
		
		Scanner scan = new Scanner(System.in);
		boolean quit = false;
		
		while(!quit) {
			
			System.out.println("1. Object A");
			System.out.println("2. Object B");
			System.out.println("3. Object C");
			System.out.println("4. Object D");
			System.out.println("5. Object E");
			System.out.println("6. Quit");
			
			boolean isChoiceValid = false;
			
			while(!isChoiceValid) {
				
				System.out.print("Please choose an option: ");
				
				while(!scan.hasNextInt()) {
					scan.next();
					System.out.println("Choice must be an integer");
					System.out.print("Please choose an option: ");
				}
				
				int choice = scan.nextInt();
				scan.nextLine();
				
				if (choice < 1 || choice > 6) {
					System.out.println("Choice out of range");
					continue;
				}
				
				isChoiceValid = true;
				System.out.println("You chose: " + choice);
				System.out.println();
				
				// TODO: Create objects accordingly
				
				if (choice == 6) quit = true;
			
			}
		}
		
		System.out.println("Quitting");
		scan.close();
		
	}

}
