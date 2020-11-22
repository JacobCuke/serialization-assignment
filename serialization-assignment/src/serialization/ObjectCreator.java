package serialization;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.w3c.dom.Document;

import json.Serializer;
import xml.XMLSerializer;

public class ObjectCreator {

	private Scanner scan = new Scanner(System.in);
	private ServerSocket serverSocket;
	private Socket clientSocket;

	public static void main(String[] args) {

		ObjectCreator oc = new ObjectCreator();
		oc.runMenu();
	}

	private void runMenu() {

		try {
			startConnection();
		} catch (IOException e1) {
			System.err.println("Error starting socket connection");
			e1.printStackTrace();
			return;
		}

		Scanner scan = getScanner();
		boolean quit = false;

		while(!quit) {

			System.out.println("Send an object with...");
			System.out.println("1. Primitive fields");
			System.out.println("2. References to other objects");
			System.out.println("3. An array of primitives");
			System.out.println("4. An array of object references");
			System.out.println("5. A Java Collection (ArrayList) of other objects");
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
						sendObject(createObjectA());
						break;
					case 2:
						sendObject(createObjectB());
						break;
					case 3:
						sendObject(createObjectC());
						break;
					case 4:
						sendObject(createObjectD());
						break;
					case 5:
						sendObject(createObjectE());
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

		scan.close();

		// Tell client to quit
		System.out.println("Closing connection...");
		try {
			closeClient();
			closeConnection();
		} catch (IOException e) {
			System.err.println("Error closing socket connection");
			e.printStackTrace();
		}
		System.out.println("Connection closed");
		System.out.println("Quitting");

	}

	private ObjectA createObjectA() throws Exception {

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
		return objectA;
	}

	private ObjectB createObjectB() throws Exception {

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

		return objectB1;
	}

	private ObjectC createObjectC() throws Exception {

		Scanner scan = getScanner();
		System.out.println("Creating Object...");

		int a[] = new int[5];

		for (int i = 0; i < 5; i++) {

			System.out.print("Please enter integer " + (i+1) + ": ");
			while (!scan.hasNextInt()) {
				scan.next();
				System.out.println("Value must be an integer: ");
			}

			a[i] = scan.nextInt();
			scan.nextLine();
		}

		ObjectC objectC = new ObjectC(a);
		return objectC;
	}

	private ObjectD createObjectD() throws Exception {

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
		return objectD;
	}

	private ObjectE createObjectE() throws Exception {

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
		return objectE;
	}

	private void sendObject(Object object) throws Exception {

		System.out.println();
		System.out.println("Choose Serialization Method...");
		System.out.println("1. JSON");
		System.out.println("2. XML");

		int choice = 0;
		boolean isChoiceValid = false;

		while(!isChoiceValid) {

			System.out.print("Please choose serialization method: ");

			while(!scan.hasNextInt()) {
				scan.next();
				System.out.println("Choice must be an integer");
				System.out.print("Please choose an option: ");
			}

			choice = scan.nextInt();
			scan.nextLine();

			if (choice < 1 || choice > 2) {
				System.out.println("Choice out of range");
				continue;
			}

			isChoiceValid = true;
			System.out.println();
		}

		if (choice == 1) {

			System.out.println("Serializing");
			JsonObject json = Serializer.serializeObject(object);

			System.out.println("Sending JSON object");
			System.out.println(Serializer.jsonToString(json, true));
			
			JsonWriter jsonWriter = Json.createWriter(clientSocket.getOutputStream());
			jsonWriter.writeObject(json);
			System.out.println();

		} else {

			System.out.println("Serializing");
			Document xml = XMLSerializer.serializeObject(object);

			System.out.println("Sending XML document");
			System.out.println(XMLSerializer.xmlToString(xml, true));
			
			JsonWriter jsonWriter = Json.createWriter(clientSocket.getOutputStream());
			JsonObjectBuilder baseObject = Json.createObjectBuilder();
			baseObject.add("xml", XMLSerializer.xmlToString(xml, false));
			JsonObject xmlPackage = baseObject.build();
			jsonWriter.writeObject(xmlPackage);
			
			System.out.println();

		}

	}

	private void closeClient() throws IOException {

		JsonWriter jsonWriter = Json.createWriter(clientSocket.getOutputStream());

		JsonObjectBuilder baseObject = Json.createObjectBuilder();
		baseObject.add("quit", "");
		JsonObject jsonQuit = baseObject.build();
		jsonWriter.writeObject(jsonQuit);

	}

	private void startConnection() throws IOException {

		serverSocket = new ServerSocket(6666);
		System.out.println("Waiting for client...");
		clientSocket = serverSocket.accept();
		System.out.println("Connected");
		System.out.println();

	}

	private void closeConnection() throws IOException {

		serverSocket.close();
		clientSocket.close();

	}

	private Scanner getScanner() {
		return scan;
	}

}
