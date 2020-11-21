package xml;

import java.lang.reflect.*;
import java.util.*;
import org.w3c.dom.*;

import serialization.ObjectA;
import serialization.ObjectB;
import serialization.ObjectC;
import serialization.ObjectD;
import serialization.ObjectE;
import serialization.Visualizer;

/**
 * CPSC 501 
 * Incomplete JSON deserializer
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 * 
 */
public class XMLDeserializer {
	
	public static void main(String[] args) throws Exception {
		
//		ObjectA objectA = new ObjectA(1, 2.0f);
//		ObjectB objectB1 = new ObjectB(true);
//		ObjectB objectB2 = new ObjectB(false);
//		objectB1.setOther(objectB2);
//		objectB2.setOther(objectB1);
//		int[] a = {23, 144, 0, 7, 90};
//		ObjectC objectC = new ObjectC(a);
//		ObjectA objectA1 = new ObjectA(65, 4.23f);
//		ObjectA objectA2 = new ObjectA(762, 11.6f);
//		ObjectA[] b = new ObjectA[5];
//		b[2] = objectA1;
//		b[4] = objectA2;
//		ObjectD objectD = new ObjectD(b);
		ObjectA objectA = new ObjectA(1, 2.0f);
		ArrayList<ObjectA> c = new ArrayList<ObjectA>();
		c.add(objectA);
		ObjectE objectE = new ObjectE(c);
		Document d = XMLSerializer.serializeObject(objectE);
		Object object = deserializeObject(d);
		
		Visualizer vis = new Visualizer();
		vis.inspect(object);
		
	}

	public static Object deserializeObject(Document document) throws Exception {
		NodeList objectNodeList = document.getDocumentElement().getChildNodes();
		Map<String, Object> objectTrackingMap = new HashMap<String, Object>();
		createInstances(objectTrackingMap, objectNodeList);
		assignFieldValues(objectTrackingMap, objectNodeList);
		return objectTrackingMap.get("0");
	}

	private static void createInstances(Map<String, Object> objectTrackingMap, NodeList objectNodeList) throws Exception {
		
		for (int i = 0; i < objectNodeList.getLength(); i++) {
			
			Node objectNode = objectNodeList.item(i);
			if (objectNode instanceof Element) {
				
				Element objectInfo = (Element) objectNode;
				Class<?> objectClass = Class.forName(objectInfo.getAttribute("class"));
				
				// Object is an array
				if (objectInfo.getAttribute("type").equals("array")) {
					
					int length = Integer.parseInt(objectInfo.getAttribute("length"));
					Class<?> componentType = objectClass.getComponentType();
					Object objectInstance = Array.newInstance(componentType, length);
					
					objectTrackingMap.put(objectInfo.getAttribute("id"), objectInstance);
					continue;
					
				}
				
				// Object is not an array
				Constructor<?> constructor = objectClass.getDeclaredConstructor();
				if (!Modifier.isPublic(constructor.getModifiers())) {
					constructor.setAccessible(true);
				}
				
				Object objectInstance = constructor.newInstance();
				objectTrackingMap.put(objectInfo.getAttribute("id"), objectInstance);
				
			}
		}
	}

	private static void assignFieldValues(Map<String, Object> objectTrackingMap, NodeList objectNodeList) throws Exception {

		for (int i = 0; i < objectNodeList.getLength(); i++) {
			
			Node objectNode = objectNodeList.item(i);
			if (!(objectNode instanceof Element)) continue;
				
			Element objectInfo = (Element) objectNode;
			
			if (objectInfo.getAttribute("type").equals("array")) {
				
				assignArrayValues(objectTrackingMap, objectInfo);
				continue;
				
			}
			
			Object objectInstance = objectTrackingMap.get(objectInfo.getAttribute("id"));
			
			// Fields
			NodeList fieldNodeList = objectInfo.getChildNodes();
			for (int j = 0; j < fieldNodeList.getLength(); j++) {
				
				Node fieldNode = fieldNodeList.item(j);
				if (!(fieldNode instanceof Element)) continue;
				
				Element fieldInfo = (Element) fieldNode;
				Class<?> declaringClass = Class.forName(fieldInfo.getAttribute("declaringclass"));
				Field field = declaringClass.getDeclaredField(fieldInfo.getAttribute("name"));
				field.setAccessible(true);
				
				Element dataInfo = (Element) fieldInfo.getFirstChild();
				
				if (dataInfo.getTagName().equals("value")) {
					
					Class<?> fieldType = field.getType();
					
					// TODO: Handle more primitive types
					if (fieldType.equals(Integer.TYPE)) {
						
						field.set(objectInstance, Integer.parseInt(dataInfo.getTextContent()));
						
					} else if (fieldType.equals(Float.TYPE)) {
						
						field.set(objectInstance, Float.parseFloat(dataInfo.getTextContent()));
						
					} else if (fieldType.equals(Boolean.TYPE)) {
						
						field.set(objectInstance, Boolean.parseBoolean(dataInfo.getTextContent()));
						
					}
					
				} else {
					
					String referenceID = dataInfo.getTextContent();
					Object referenceObject = null;
					
					if (!referenceID.equals("null")) {
						referenceObject = objectTrackingMap.get(referenceID);
					}
					
					field.set(objectInstance, referenceObject);
					
				}
				
			}
		}
	}
	
	private static void assignArrayValues(Map<String, Object> objectTrackingMap, Element objectInfo) {
		
		Object objectInstance = objectTrackingMap.get(objectInfo.getAttribute("id"));
		Class<?> componentType = objectInstance.getClass().getComponentType();
		
		NodeList entryNodeList = objectInfo.getChildNodes();
		for (int i = 0; i < entryNodeList.getLength(); i++) {
			
			Node entryNode = entryNodeList.item(i);
			if (!(entryNode instanceof Element)) continue;
			Element entryInfo = (Element) entryNode;
			
			if (entryInfo.getTagName().equals("value")) {
				
				// TODO: Handle more primitive types
				if (componentType.equals(Integer.TYPE)) {
					
					Array.set(objectInstance, i, Integer.parseInt(entryInfo.getTextContent()));
					
				} else if (componentType.equals(Float.TYPE)) {
					
					Array.set(objectInstance, i, Float.parseFloat(entryInfo.getTextContent()));
					
				} else if (componentType.equals(Boolean.TYPE)) {
					
					Array.set(objectInstance, i, Boolean.parseBoolean(entryInfo.getTextContent()));
					
				}
				
			} else {
				
				String referenceID = entryInfo.getTextContent();
				Object referenceObject = null;
				
				if (!referenceID.equals("null")) {
					referenceObject = objectTrackingMap.get(referenceID);
				}
				
				Array.set(objectInstance, i, referenceObject);
				
			}
		}
	}

}
