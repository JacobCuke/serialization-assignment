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

	public static Object deserializeObject(JsonObject json_object) throws Exception {
		JsonArray objectList = json_object.getJsonArray("objects");
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
	}

}