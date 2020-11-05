package json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        serializeHelper(object, objectList, new IdentityHashMap<Object, String>());
        JsonObjectBuilder jsonBaseObject = Json.createObjectBuilder();
        jsonBaseObject.add("objects", objectList);
        return jsonBaseObject.build();
        
    }

    private static void serializeHelper(Object source, JsonArrayBuilder objectList, Map<Object, String> objectTrackingMap) throws Exception {
    	
        String objectID = Integer.toString(objectTrackingMap.size());
        objectTrackingMap.put(source, objectID);
        Class<?> objectClass = source.getClass();
        JsonObjectBuilder objectInfo = Json.createObjectBuilder();
        
        objectInfo.add("class", objectClass.getName());
        objectInfo.add("id", objectID);
        
        if (objectClass.isArray()) {
        	
        	objectInfo.add("type", "array");
        	objectInfo.add("length", Integer.toString(Array.getLength(source)));
        	
        	JsonArrayBuilder entryList = Json.createArrayBuilder();
        	for (int i = 0; i < Array.getLength(source); i++) {
        		
        		Object e = Array.get(source, i);
        		JsonObjectBuilder entryInfo = Json.createObjectBuilder();
        		
        		if (objectClass.getComponentType().isPrimitive()) {
        			
        			entryInfo.add("value", e.toString());
        			
        		} else {

        			if (e == null) {
        				entryInfo.add("reference", "null");
        				entryList.add(entryInfo);
        				continue;
        			}
        			
        			if (objectTrackingMap.containsKey(e)) {
            			
            			entryInfo.add("reference", objectTrackingMap.get(e).toString());
            			
            		} else {
            			
            			entryInfo.add("reference", Integer.toString(objectTrackingMap.size()));
            			serializeHelper(e, objectList, objectTrackingMap);
            			
            		}
        			
        		}
        		
        		entryList.add(entryInfo);
        		
        	}
        	
        	objectInfo.add("entries", entryList);
        	objectList.add(objectInfo);
        	return;
        	
        }
        
        // Object is not an array	
        objectInfo.add("type", "object");
        
        JsonArrayBuilder fieldList = Json.createArrayBuilder();
        ArrayList<Field> fields = getAllFields(objectClass);
        
        for (Field f : fields) {
        	
        	f.setAccessible(true);
        	JsonObjectBuilder fieldInfo = Json.createObjectBuilder();
        	fieldInfo.add("name", f.getName());
        	fieldInfo.add("declaringclass", f.getDeclaringClass().getName());
        	
        	if (f.getType().isPrimitive() && !f.getClass().isArray()) {
        		
        		fieldInfo.add("value", f.get(source).toString());
        		
        	} else {
        		
        		Object ob = f.get(source);
        		
        		if (ob == null) {
        			fieldInfo.add("reference", "null");
        			fieldList.add(fieldInfo);
        			continue;
        		}
        		
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
    
    protected static ArrayList<Field> getAllFields(Class<?> objectClass) {
    	
    	ArrayList<Field> fields = new ArrayList<Field>();
    	
    	Field[] childFields = objectClass.getDeclaredFields();
    	for (Field f : childFields) {
    		
    		if (!Modifier.isStatic(f.getModifiers())) {
    			fields.add(f);
    		}
    		
    	}
    	
    	Class<?> superClass = objectClass.getSuperclass();
    	
    	if (superClass != null) {
    		
    		fields.addAll(getAllFields(superClass));
    		
    	}
    	
    	return fields;
    	
    }

}