package xml;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import json.Serializer;

/**
 * CPSC 501 
 * XML Serializer
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 */
public class XMLSerializer {

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
		
		if (objectClass.isArray()) {
			
			objectInfo.setAttribute("type", "array");
			objectInfo.setAttribute("length", Integer.toString(Array.getLength(objectInstance)));
			
			for (int i = 0; i < Array.getLength(objectInstance); i++) {
				
				Object e = Array.get(objectInstance, i);
				
				if (objectClass.getComponentType().isPrimitive()) {
					
					Element entryInfo = document.createElement("value");
					entryInfo.setTextContent(e.toString());
					objectInfo.appendChild(entryInfo);
					
				} else {
					
					Element referenceInfo = document.createElement("reference");
					
					if (e == null) {
						referenceInfo.setTextContent("null");
						objectInfo.appendChild(referenceInfo);
						continue;
					}
					
					if (objectTrackingMap.containsKey(e)) {

						referenceInfo.setTextContent(objectTrackingMap.get(e).toString());

					} else {

						referenceInfo.setTextContent(Integer.toString(objectTrackingMap.size()));
						serializeHelper(e, document, objectTrackingMap);

					}
					
					objectInfo.appendChild(referenceInfo);
					
				}
				
			}
			
			document.getDocumentElement().appendChild(objectInfo);
			return document;
		}
		
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
	
	public static String xmlToString(Document d, boolean prettyPrint) throws Exception {
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		StringWriter writer = new StringWriter();

		if (prettyPrint) {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		}
		transformer.transform(new DOMSource(d), new StreamResult(writer));

		return writer.getBuffer().toString();
		
	}

}