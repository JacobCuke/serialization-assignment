package xml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import serialization.ObjectA;
import serialization.ObjectB;
import serialization.ObjectC;
import serialization.ObjectD;
import serialization.ObjectE;

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
	
	@Test
	void testObjectC() throws Exception {
		
		int[] a = {0, 0, 0, 3, 0};
		ObjectC objectC = new ObjectC(a);
		
		Document xml = XMLSerializer.serializeObject(objectC);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"[I\" id=\"1\" length=\"5\" type=\"array\"><value>0</value><value>0</value><value>0</value><value>3</value><value>0</value></object><object class=\"serialization.ObjectC\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectC\" name=\"a\"><reference>1</reference></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}
	
	@Test
	void testObjectD() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		ObjectA[] b = new ObjectA[5];
		b[3] = objectA;
		ObjectD objectD = new ObjectD(b);
		
		Document xml = XMLSerializer.serializeObject(objectD);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"serialization.ObjectA\" id=\"2\" type=\"object\"><field declaringclass=\"serialization.ObjectA\" name=\"x\"><value>1</value></field><field declaringclass=\"serialization.ObjectA\" name=\"y\"><value>2.0</value></field></object><object class=\"[Lserialization.ObjectA;\" id=\"1\" length=\"5\" type=\"array\"><reference>null</reference><reference>null</reference><reference>null</reference><reference>2</reference><reference>null</reference></object><object class=\"serialization.ObjectD\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectD\" name=\"b\"><reference>1</reference></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}
	
	@Test
	void testObjectE() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		ArrayList<ObjectA> c = new ArrayList<ObjectA>();
		c.add(objectA);
		ObjectE objectE = new ObjectE(c);
		
		Document xml = XMLSerializer.serializeObject(objectE);
		
		String correct = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><serialized><object class=\"serialization.ObjectA\" id=\"3\" type=\"object\"><field declaringclass=\"serialization.ObjectA\" name=\"x\"><value>1</value></field><field declaringclass=\"serialization.ObjectA\" name=\"y\"><value>2.0</value></field></object><object class=\"[Ljava.lang.Object;\" id=\"2\" length=\"10\" type=\"array\"><reference>3</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference><reference>null</reference></object><object class=\"java.util.ArrayList\" id=\"1\" type=\"object\"><field declaringclass=\"java.util.ArrayList\" name=\"elementData\"><reference>2</reference></field><field declaringclass=\"java.util.ArrayList\" name=\"size\"><value>1</value></field><field declaringclass=\"java.util.AbstractList\" name=\"modCount\"><value>1</value></field></object><object class=\"serialization.ObjectE\" id=\"0\" type=\"object\"><field declaringclass=\"serialization.ObjectE\" name=\"c\"><reference>1</reference></field></object></serialized>";
		assertEquals(XMLSerializer.xmlToString(xml, false), correct);
		
	}

}
