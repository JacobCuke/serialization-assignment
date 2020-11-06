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
			
			// Instatiate object
			Constructor<?> constructor = objectClass.getDeclaredConstructor();
			if (!Modifier.isPublic(constructor.getModifiers())) {
				constructor.setAccessible(true);
			}
			Object objectInstance = constructor.newInstance();
			
			//Make object
			objectTrackingMap.put(objectInfo.getString("id"), objectInstance);
		}
	}

	private static void assignFieldValues(Map<String, Object> objectTrackingMap, JsonArray objectList) throws Exception {
		
		for (int i = 0; i < objectList.size(); i++) {
			JsonObject objectInfo = objectList.getJsonObject(i);
			Class<?> objectClass = Class.forName(objectInfo.getString("class"));
			
			Object objectInstance = objectTrackingMap.get(objectInfo.getString("id"));
			
			// Fields
			JsonArray fieldList = objectInfo.getJsonArray("fields");
			
			// Loop through each field, setting each one
			for (int j = 0; j < fieldList.size(); j++) {
				
				JsonObject fieldInfo = fieldList.getJsonObject(j);
				Field field = objectClass.getDeclaredField(fieldInfo.getString("name"));
				field.setAccessible(true);
				
				// TODO: Determine if field is a primitive or reference to object
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
					
					// TODO: Handle fields that contain references
					String referenceID = fieldInfo.getString("reference");
					Object referenceObject = objectTrackingMap.get(referenceID);
					
					field.set(objectInstance, referenceObject);
					
				}
				
			}
			
		}
		
	}

}