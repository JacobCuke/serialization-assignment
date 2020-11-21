package xml;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import json.Serializer;
import serialization.ObjectA;
import serialization.ObjectB;

/**
 * CPSC 501 
 * XML Serializer
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 */
public class XMLSerializer {

	public static void main(String[] args) throws Exception {

		//ObjectA objectA = new ObjectA(1, 2.0f);
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		Document d = serializeObject(objectB1);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		StringWriter writer = new StringWriter();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(new DOMSource(d), new StreamResult(writer));

		String xmlString = writer.getBuffer().toString();
		System.out.println(xmlString);

	}

	public static Document serializeObject(Object objectInstance) throws Exception {

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		document.appendChild(document.createElement("serialized"));
		return serializeHelper(objectInstance, document, new IdentityHashMap<Object, String>());

	}

	private static Document serializeHelper(Object objectInstance, Document document, Map<Object, String> objectTrackingMap) throws Exception {

		String objectID = Integer.toString(objectTrackingMap.size());
		objectTrackingMap.put(objectInstance, objectID);
		Class<?> objectClass = objectInstance.getClass();
		Element objectInfo = document.createElement("object");

		objectInfo.setAttribute("class", objectClass.getName());
		objectInfo.setAttribute("id", objectID);
		objectInfo.setAttribute("type", "object");

		// Fields
		ArrayList<Field> fields = Serializer.getAllFields(objectClass);

		for (Field f : fields) {

			f.setAccessible(true);
			Element fieldInfo = document.createElement("field");

			fieldInfo.setAttribute("declaringclass", f.getDeclaringClass().getName());
			fieldInfo.setAttribute("name", f.getName());

			if (f.getType().isPrimitive()) {
				
				Element valueInfo = document.createElement("value");
				valueInfo.setTextContent(f.get(objectInstance).toString());
				fieldInfo.appendChild(valueInfo);
				
			} else {
				
				Object ob = f.get(objectInstance);
				Element referenceInfo = document.createElement("reference");
				
				if (ob == null) {
					referenceInfo.setTextContent("null");
					fieldInfo.appendChild(referenceInfo);
					continue;
				}
				
				if (objectTrackingMap.containsKey(ob)) {
					
					referenceInfo.setTextContent(objectTrackingMap.get(ob).toString());
					
				} else {
					
					referenceInfo.setTextContent(Integer.toString(objectTrackingMap.size()));
					serializeHelper(ob, document, objectTrackingMap);
					
				}
				
				fieldInfo.appendChild(referenceInfo);
				
			}
			
			objectInfo.appendChild(fieldInfo);

		}

		document.getDocumentElement().appendChild(objectInfo);
		return document;

	}

}