package xml;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import serialization.ObjectA;

class XMLSerializerTest {

	@Test
	void testObjectA() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		Document xml = XMLSerializer.serializeObject(objectA);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"serialization.ObjectA\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectA\" name=\"x\"><value>1</value></field><field declaringclass=\"serialization.ObjectA\" name=\"y\"><value>2.0</value></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}

}
