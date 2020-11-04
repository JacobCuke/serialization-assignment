package json;

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
        
        objectList.add(objectInfo);
        
    }

}