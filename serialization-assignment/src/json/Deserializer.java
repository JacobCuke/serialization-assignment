package json;

import java.lang.reflect.*;
import java.util.*;
import javax.json.*;

/**
 * CPSC 501 
 * JSON Deserializer
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 * 
 */
public class Deserializer {

	public static Object deserializeObject(JsonObject jsonObject) throws Exception {
		JsonArray objectList = jsonObject.getJsonArray("objects");
		Map<String, Object> objectTrackingMap = new HashMap<String, Object>();
		createInstances(objectTrackingMap, objectList);
		assignFieldValues(objectTrackingMap, objectList);
		return objectTrackingMap.get("0");
	}
	//

	private static void createInstances(Map<String, Object> objectTrackingMap, JsonArray objectList) throws Exception {
		for (int i = 0; i < objectList.size(); i++) {
			JsonObject objectInfo = objectList.getJsonObject(i);
			Class<?> objectClass = Class.forName(objectInfo.getString("class"));
			
			// Determine if object is an array
			if (objectInfo.getString("type").equals("array")) {
				
				int length = Integer.parseInt(objectInfo.getString("length"));
				Class<?> componentType = objectClass.getComponentType();
				Object objectInstance = Array.newInstance(componentType, length);
				
				objectTrackingMap.put(objectInfo.getString("id"), objectInstance);
				
			} else {
				
				Constructor<?> constructor = objectClass.getDeclaredConstructor();
				if (!Modifier.isPublic(constructor.getModifiers())) {
					constructor.setAccessible(true);
				}
				Object objectInstance = constructor.newInstance();
				
				objectTrackingMap.put(objectInfo.getString("id"), objectInstance);
				
			}
			
		}
	}

	private static void assignFieldValues(Map<String, Object> objectTrackingMap, JsonArray objectList) throws Exception {
		
		for (int i = 0; i < objectList.size(); i++) {
			
			JsonObject objectInfo = objectList.getJsonObject(i);
			
			if (objectInfo.getString("type").equals("array")) {
				
				assignArrayValues(objectTrackingMap, objectInfo);
				continue;
				
			}
			
			Object objectInstance = objectTrackingMap.get(objectInfo.getString("id"));
			
			// Fields
			JsonArray fieldList = objectInfo.getJsonArray("fields");
			
			// Loop through each field, setting each one
			for (int j = 0; j < fieldList.size(); j++) {
				
				JsonObject fieldInfo = fieldList.getJsonObject(j);
				
				Class<?> declaringClass = Class.forName(fieldInfo.getString("declaringclass"));
				Field field = declaringClass.getDeclaredField(fieldInfo.getString("name"));
				field.setAccessible(true);
				
				if (fieldInfo.containsKey("value")) {
					
					Class<?> fieldType = field.getType();
					
					// TODO: Handle more primitive types
					if (fieldType.equals(Integer.TYPE)) {
						
						field.set(objectInstance, Integer.parseInt(fieldInfo.getString("value")));
						
					} else if (fieldType.equals(Float.TYPE)) {
						
						field.set(objectInstance, Float.parseFloat(fieldInfo.getString("value")));
						
					} else if (fieldType.equals(Boolean.TYPE)) {
						
						field.set(objectInstance, Boolean.parseBoolean(fieldInfo.getString("value")));
						
					}
					
				} else {
					
					String referenceID = fieldInfo.getString("reference");
					Object referenceObject = null;
					
					if (!referenceID.equals("null")) {
						referenceObject = objectTrackingMap.get(referenceID);
					}
					
					field.set(objectInstance, referenceObject);
					
				}
			}
		}
	}
	
	private static void assignArrayValues(Map<String, Object> objectTrackingMap, JsonObject objectInfo) {
		
		Object objectInstance = objectTrackingMap.get(objectInfo.getString("id"));
		Class<?> componentType = objectInstance.getClass().getComponentType();
		
		JsonArray entryList = objectInfo.getJsonArray("entries");
		
		for (int i = 0; i < entryList.size(); i++) {
			
			JsonObject entryInfo = entryList.getJsonObject(i);
			
			if (entryInfo.containsKey("value")) {
				
				// TODO: Handle more primitive types
				if (componentType.equals(Integer.TYPE)) {
					
					Array.set(objectInstance, i, Integer.parseInt(entryInfo.getString("value")));
					
				} else if (componentType.equals(Float.TYPE)) {
					
					Array.set(objectInstance, i, Float.parseFloat(entryInfo.getString("value")));
					
				} else if (componentType.equals(Boolean.TYPE)) {
					
					Array.set(objectInstance, i, Boolean.parseBoolean(entryInfo.getString("value")));
					
				}
				
			} else {
			
				String referenceID = entryInfo.getString("reference");
				Object referenceObject = null;
				
				if (!referenceID.equals("null")) {
					referenceObject = objectTrackingMap.get(referenceID);
				}
				
				Array.set(objectInstance, i, referenceObject);
				
			}
		}
	}

}