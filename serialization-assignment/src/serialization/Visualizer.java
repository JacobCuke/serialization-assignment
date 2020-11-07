package serialization;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import json.Deserializer;
import json.Serializer;

public class Visualizer {

	private int depth = 0;
	private Socket clientSocket;

	public static void main(String args[]) throws Exception {

		// TODO: Read in JSON object from socket
		Visualizer vis = new Visualizer();
		vis.startConnection();
		
		while (true) {
			
			// Read
			JsonObject json = vis.readObject();
			if (json.containsKey("quit")) {
				break;
			}
			
			// Deserialize
			Object object = Deserializer.deserializeObject(json);
			// Visualize
			vis.inspect(object);
			
		}
		
		vis.closeConnection();

	}

	public void inspect(Object obj) {
		ArrayList<Object> objectTrackingList = new ArrayList<Object>();
		objectTrackingList.add(obj);

		Class<?> c = obj.getClass();
		inspectClass(c, obj, 0, objectTrackingList);

	}

	private void inspectClass(Class<?> c, Object obj, int depth, ArrayList<Object> objectTrackingList) {

		this.depth = depth;

		// Class Name
		tabPrintln("CLASS");
		tabPrintln("Class: " + c.getName());

		// Deal with array objects being passed in directly
		if (c.isArray()) {
			inspectArray(c, obj, depth, objectTrackingList);
			return;
		}

		// Fields
		inspectFields(c, obj, depth, objectTrackingList);

	}

	private void inspectArray(Class<?> c, Object obj, int depth, ArrayList<Object> objectTrackingList) {
		tabPrintln(" Component Type: " + c.getComponentType().getName());
		tabPrintln(" Length: " + Array.getLength(obj));
		tabPrint(" Entries-> ");
		if (Array.getLength(obj) == 0) {
			System.out.println("NONE");
			return;
		}

		System.out.println();

		for (int i = 0; i < Array.getLength(obj); i++) {
			Object entry = Array.get(obj, i);

			if (entry == null) {
				tabPrintln("  Value: " + entry);
				continue;
			}

			if (!c.getComponentType().isPrimitive()) {
				tabPrint("  Value (ref): ");
				System.out.println(entry.getClass().getName() + "@" + Integer.toHexString(entry.hashCode()));

				if (objectTrackingList.contains(entry)) {

					tabPrintln("    -> Already recursively inspected");

				} else {

					tabPrintln("    -> Recursively inspect");
					objectTrackingList.add(entry);
					inspectClass(entry.getClass(), entry, depth+1, objectTrackingList);
					this.depth = depth;

				}

				continue;
			}

			tabPrintln("  Value: " + entry);
		}
	}

	private void inspectFields(Class<?> c, Object obj, int depth, ArrayList<Object> objectTrackingList) {
		tabPrintln("FIELDS ( " + c.getName() + " )");
		tabPrint("Fields-> ");
		ArrayList<Field> fields = Serializer.getAllFields(c);
		if (fields.size() == 0) {
			System.out.println("NONE");
		} 
		else {
			System.out.println();
			for (Field f : fields) {
				tabPrintln(" FIELD");
				tabPrint("  Info: ");
				System.out.print(Modifier.toString(f.getModifiers()) + " ");
				System.out.print(f.getType().getTypeName() + " ");
				System.out.println(f.getDeclaringClass().getName() + "." + f.getName());

				// Value
				f.setAccessible(true);
				try {
					Object value = f.get(obj);

					// Handle null objects
					if (value == null) {
						tabPrintln("  Value: " + value);
						continue;
					}

					Class<?> valueClass = value.getClass();

					// Check if value is an array
					if (valueClass.isArray()) {
						// Component type
						tabPrintln("  Component Type: " + valueClass.getComponentType().getName());
						// Length
						tabPrintln("  Length: " + Array.getLength(value));
						// Entries
						tabPrint("  Entries-> ");
						if (Array.getLength(value) == 0) {
							System.out.println("NONE");
							continue;
						}
						System.out.println();

						for (int i = 0; i < Array.getLength(value); i++) {
							Object entry = Array.get(value, i);

							if (entry == null) {
								tabPrintln("   Value: " + entry);
								continue;
							}

							if (!valueClass.getComponentType().isPrimitive()) {
								tabPrint("   Value (ref): ");
								System.out.println(entry.getClass().getName() + "@" + Integer.toHexString(entry.hashCode()));


								if (objectTrackingList.contains(entry)) {

									tabPrintln("    -> Already recursively inspected");

								} else {

									tabPrintln("    -> Recursively inspect");
									objectTrackingList.add(entry);
									inspectClass(entry.getClass(), entry, depth+1, objectTrackingList);
									this.depth = depth;

								}

								continue;
							}

							tabPrintln("   Value: " + entry);
						}
						continue;
					}

					// Check if value is a reference to an object and recurse
					if (!f.getType().isPrimitive()) {

						tabPrint("  Value (ref): ");
						System.out.println(value.getClass().getName() + "@" + Integer.toHexString(value.hashCode()));


						if (objectTrackingList.contains(value)) {

							tabPrintln("    -> Already recursively inspected");

						} else {

							tabPrintln("    -> Recursively inspect");
							objectTrackingList.add(value);
							inspectClass(value.getClass(), value, depth+1, objectTrackingList);
							this.depth = depth;

						}

						continue;
					}

					tabPrint("  Value: ");
					System.out.println(value);

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void tabPrintln(String string) {
		for (int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
		System.out.println(string);
	}

	private void tabPrint(String string) {
		for (int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
		System.out.print(string);
	}
	
	private void startConnection() throws UnknownHostException, IOException {
		
		clientSocket = new Socket("127.0.0.1", 6666);
		
	}
	
	private void closeConnection() throws IOException {
		
		clientSocket.close();
		
	}
	
	private JsonObject readObject() throws IOException {
		
		JsonReader jsonReader = Json.createReader(clientSocket.getInputStream());
		return jsonReader.readObject();
	
	}

}
