package serialization;

import java.util.Scanner;

public class ObjectCreator {
	
	public static void main(String[] args) {
		
		runMenu();
	}
	
	private static void runMenu() {
		
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
				switch (choice) {
				case 1:
					sendObjectA();
					break;
				case 2:
				case 3:
				case 4:
				case 5:
					break;
				case 6:
					quit = true;
					break;
					
				}
			}
		}
		
		System.out.println("Quitting");
		scan.close();
		
	}
	
	private static void sendObjectA() {
		
		Scanner scan = new Scanner(System.in);
		
		// Ask for user input
		System.out.println("Creating Object...");
		
		System.out.print("Please enter an integer value: ");
		while (!scan.hasNextInt()) {
			scan.next();
			System.out.print("Value must be an integer: ");
		}
		
		int x = scan.nextInt();
		scan.nextLine();
		
		System.out.print("Please enter a float: ");
		while (!scan.hasNextFloat()) {
			scan.next();
			System.out.print("Value must be a float: ");
		}
		
		float y = scan.nextFloat();
		scan.nextLine();
		
		scan.close();
		
		// Create object
		ObjectA objectA = new ObjectA(x, y);
		
		// TODO: Serialize
		System.out.println("Object Created");
		System.out.println("Serializing...");
		
	}

}
