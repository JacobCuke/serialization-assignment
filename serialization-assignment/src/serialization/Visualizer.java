package serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.json.JsonObject;

import json.Deserializer;
import json.Serializer;

public class Visualizer {
	
	private int depth = 0;
	
	public static void main(String args[]) throws Exception {
		
		// TODO: Read in JSON object from socket
		
		// TODO: Deserialize
//		ObjectA objectA = new ObjectA(1, 2.0f);
//		JsonObject json = Serializer.serializeObject(objectA);
//		
//		Object object = Deserializer.deserializeObject(json);
		
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		
		JsonObject json = Serializer.serializeObject(objectB1);
		Object object = Deserializer.deserializeObject(json);
		
		// TODO: Print out
		Visualizer vis = new Visualizer();
		vis.inspect(object);
		
	}

    public void inspect(Object obj) {
        Class<?> c = obj.getClass();
        inspectClass(c, obj, 0);
    }

    private void inspectClass(Class<?> c, Object obj, int depth) {
    	
    	this.depth = depth;
    	
    	// Class Name
    	tabPrintln("CLASS");
    	tabPrintln("Class: " + c.getName());
    	
    	// Deal with array objects being passed in directly
    	if (c.isArray()) {
			inspectArray(c, obj, depth);
			return;
    	}
    	
    	// Fields
    	inspectFields(c, obj, depth);
    	
    }
    
    private void inspectArray(Class<?> c, Object obj, int depth) {
    	tabPrintln(" Component Type: " + c.getComponentType());
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
				

				tabPrintln("    -> Recursively inspect");
				inspectClass(entry.getClass(), entry, depth+1);
				this.depth = depth;

				continue;
			}
			
			tabPrintln("  Value: " + entry);
		}
    }
    
    private void inspectFields(Class<?> c, Object obj, int depth) {
    	tabPrintln("FIELDS ( " + c.getName() + " )");
    	tabPrint("Fields-> ");
    	Field[] fields = c.getDeclaredFields();
    	if (fields.length == 0) {
    		System.out.println("NONE");
    	} 
    	else {
    		System.out.println();
    		for (Field f : fields) {
    			tabPrintln(" FIELD");
    			tabPrintln("  Name: " + f.getName());
    			tabPrintln("  Type: " + f.getType());
    			
    			// Modifiers
    			tabPrint("  Modifiers: ");
    			int modifiers = f.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    			
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
						tabPrintln("  Component Type: " + valueClass.getComponentType());
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
								

								tabPrintln("    -> Recursively inspect");
								inspectClass(entry.getClass(), entry, depth+1);
								this.depth = depth;

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
						

						tabPrintln("    -> Recursively inspect");
						inspectClass(valueClass, value, depth+1);
						this.depth = depth;

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

}