package serialization;

import java.util.ArrayList;
import java.util.Scanner;

import javax.json.JsonObject;

import json.Serializer;

public class ObjectCreator {
	
	private Scanner scan = new Scanner(System.in);
	
	private Scanner getScanner() {
		return scan;
	}
	
	public static void main(String[] args) {
		
		ObjectCreator oc = new ObjectCreator();
		oc.runMenu();
	}
	
	private void runMenu() {
		
		Scanner scan = getScanner();
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
				System.out.println();
				
				try {
					
					switch (choice) {
					case 1:
						sendObjectA();
						break;
					case 2:
						sendObjectB();
						break;
					case 3:
						sendObjectC();
						break;
					case 4:
						sendObjectD();
						break;
					case 5:
						sendObjectE();
						break;
					case 6:
						quit = true;
						break;
						
					}
					
				} catch (Exception e) {
					System.err.println("Error Occured");
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Quitting");
		scan.close();
		
	}
	
	private void sendObjectA() throws Exception {
		
		Scanner scan = getScanner();
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
		
		ObjectA objectA = new ObjectA(x, y);
		
		System.out.println("Object Created");
		System.out.println();
		
		// TODO: Serialize
		System.out.println("Serializing...");
		JsonObject json = null;
		json = Serializer.serializeObject(objectA);
		System.out.println(json);
		
		// TODO: sendObject(JsonObject json)
		
	}
	
	private void sendObjectB() {
		
		Scanner scan = getScanner();
		System.out.println("Creating Object...");
		
		System.out.print("Please enter a boolean value: ");
		while(!scan.hasNextBoolean()) {
			scan.next();
			System.out.print("Please enter true or false: ");
		}
		
		boolean z1 = scan.nextBoolean();
		scan.nextLine();
		
		System.out.print("Please enter another boolean value: ");
		while(!scan.hasNextBoolean()) {
			scan.next();
			System.out.print("Please enter true or false: ");
		}
		
		boolean z2 = scan.nextBoolean();
		scan.nextLine();
		
		ObjectB objectB1 = new ObjectB(z1);
		ObjectB objectB2 = new ObjectB(z2);
		
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		
		System.out.println("Object Created");
		System.out.println();
		
		// TODO: Serialize
		System.out.println("Serializing...");
		
		// TODO: sendObject(JsonObject json)
		
	}
	
	private void sendObjectC() {
		
		Scanner scan = getScanner();
		System.out.println("Creating Object...");
		
		int a[] = new int[5];
		
		for (int i = 0; i < 5; i++) {
			
			System.out.println("Please enter integer " + (i+1) + ": ");
			while (!scan.hasNextInt()) {
				scan.next();
				System.out.println("Value must be an integer: ");
			}
			
			a[i] = scan.nextInt();
			scan.nextLine();
		}
		
		ObjectC objectC = new ObjectC(a);
		
		System.out.println("Object Created");
		System.out.println();
		
		// TODO: Serialize
		System.out.println("Serializing...");
		
		// TODO: sendObject(JsonObject json)
		
	}
	
	private void sendObjectD() {
		
		Scanner scan = getScanner();
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
		
		ObjectA objectA = new ObjectA(x, y);
		ObjectA[] b = new ObjectA[5];
		b[3] = objectA;
		
		ObjectD objectD = new ObjectD(b);
		
		System.out.println("Object Created");
		System.out.println();
		
		// TODO: Serialize
		System.out.println("Serializing...");
		
		// TODO: sendObject(JsonObject json)
		
	}
	
	private void sendObjectE() {
		
		Scanner scan = getScanner();
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
		
		ObjectA objectA = new ObjectA(x, y);
		ArrayList<ObjectA> c = new ArrayList<ObjectA>();
		c.add(objectA);
		
		ObjectE objectE = new ObjectE(c);
		
		System.out.println("Object Created");
		System.out.println();
		
		// TODO: Serialize
		System.out.println("Serializing...");
		
		// TODO: sendObject(JsonObject json)
		
	}

}
