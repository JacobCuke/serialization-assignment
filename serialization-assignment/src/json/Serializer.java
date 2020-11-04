package json;

import java.lang.reflect.Field;
import java.util.*;
import javax.json.*;

/**
 * CPSC 501 
 * JSON Serializer
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 * 
 */
public class Serializer {

    public static JsonObject serializeObject(Object object) throws Exception {
    	
    	JsonArrayBuilder objectList = Json.createArrayBuilder();
        serializeHelper(object, objectList, new IdentityHashMap());
        JsonObjectBuilder jsonBaseObject = Json.createObjectBuilder();
        jsonBaseObject.add("objects", objectList);
        return jsonBaseObject.build();
        
    }

    private static void serializeHelper(Object source, JsonArrayBuilder objectList, Map objectTrackingMap) throws Exception {
    	
        String objectID = Integer.toString(objectTrackingMap.size());
        objectTrackingMap.put(source, objectID);
        Class objectClass = source.getClass();
        JsonObjectBuilder objectInfo = Json.createObjectBuilder();
        
        objectInfo.add("class", objectClass.getName());
        objectInfo.add("id", objectID);
        
        if (objectClass.isArray()) {
        	objectInfo.add("type", "array");
        } else {
        	objectInfo.add("type", "object");
        }
        
        // TODO: Fields
        JsonArrayBuilder fieldList = Json.createArrayBuilder();
        Field[] fields = objectClass.getDeclaredFields();
        
        for (Field f : fields) {
        	
        	f.setAccessible(true);
        	JsonObjectBuilder fieldInfo = Json.createObjectBuilder();
        	fieldInfo.add("name", f.getName());
        	fieldInfo.add("declaringclass", f.getDeclaringClass().getName());
        	
        	if (f.getType().isPrimitive()) {
        		
        		fieldInfo.add("value", f.get(source).toString());
        		
        	} else {
        		
        		Object ob = f.get(source);
        		if (objectTrackingMap.containsKey(ob)) {
        			
        			fieldInfo.add("reference", objectTrackingMap.get(ob).toString());
        			
        		} else {
        			
        			fieldInfo.add("reference", Integer.toString(objectTrackingMap.size()));
        			serializeHelper(ob, objectList, objectTrackingMap);
        			
        		}
        		
        	}
        	
        	fieldList.add(fieldInfo);
        	
        }
        
        objectInfo.add("fields", fieldList);
        
        objectList.add(objectInfo);
        
    }

}