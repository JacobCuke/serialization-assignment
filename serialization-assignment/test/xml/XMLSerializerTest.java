package xml;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import serialization.ObjectA;
import serialization.ObjectB;

class XMLSerializerTest {

	@Test
	void testObjectA() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		
		Document xml = XMLSerializer.serializeObject(objectA);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"serialization.ObjectA\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectA\" name=\"x\"><value>1</value></field><field declaringclass=\"serialization.ObjectA\" name=\"y\"><value>2.0</value></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}
	
	@Test 
	void testObjectB() throws Exception {
		
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		
		Document xml = XMLSerializer.serializeObject(objectB1);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"serialization.ObjectB\" id=\"1\" type=\"object\"><field declaringclass=\"serialization.ObjectB\" name=\"z\"><value>false</value></field><field declaringclass=\"serialization.ObjectB\" name=\"other\"><reference>0</reference></field></object><object class=\"serialization.ObjectB\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectB\" name=\"z\"><value>true</value></field><field declaringclass=\"serialization.ObjectB\" name=\"other\"><reference>1</reference></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}

}
