package json;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import serialization.ObjectA;
import serialization.ObjectB;
import serialization.ObjectC;
import serialization.ObjectD;
import serialization.ObjectE;

class DeserializerTest {

	@Test
	void testObjectA() throws Exception {
		
		ObjectA objectA = new ObjectA(23, 763.2f);
		JsonObject json = Serializer.serializeObject(objectA);
		ObjectA object = (ObjectA) Deserializer.deserializeObject(json);
		
		assertEquals(object.getX(), 23);
		assertEquals(object.getY(), 763.2f);
		
	}
	
	@Test
	void testObjectB() throws Exception {
		
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		JsonObject json = Serializer.serializeObject(objectB1);
		ObjectB object = (ObjectB) Deserializer.deserializeObject(json);
		
		assertTrue(object.isZ());
		assertFalse(object.getOther().isZ());
		
	}
	
	@Test
	void testObjectC() throws Exception {
		
		int[] a = {23, 144, 0, 7, 90};
		ObjectC objectC = new ObjectC(a);
		JsonObject json = Serializer.serializeObject(objectC);
		ObjectC object = (ObjectC) Deserializer.deserializeObject(json);
		
		assertEquals(object.getA()[0], 23);
		assertEquals(object.getA()[1], 144);
		assertEquals(object.getA()[2], 0);
		assertEquals(object.getA()[3], 7);
		assertEquals(object.getA()[4], 90);
		
	}
	
	@Test
	void testObjectD() throws Exception {
		
		ObjectA objectA1 = new ObjectA(65, 4.23f);
		ObjectA objectA2 = new ObjectA(762, 11.6f);
		ObjectA[] b = new ObjectA[5];
		b[2] = objectA1;
		b[4] = objectA2;
		ObjectD objectD = new ObjectD(b);
		JsonObject json = Serializer.serializeObject(objectD);
		ObjectD object = (ObjectD) Deserializer.deserializeObject(json);
		
		assertEquals(object.getB()[2].getX(), 65);
		assertEquals(object.getB()[2].getY(), 4.23f);
		assertEquals(object.getB()[4].getX(), 762);
		assertEquals(object.getB()[4].getY(), 11.6f);
		
	}
	
	@Test
	void testObjectE() throws Exception {
		
		ObjectA objectA1 = new ObjectA(345, 89.21f);
		ObjectA objectA2 = new ObjectA(67, 76.2f);
		ArrayList<ObjectA> c = new ArrayList<ObjectA>();
		c.add(objectA1);
		c.add(objectA2);
		ObjectE objectE = new ObjectE(c);
		JsonObject json = Serializer.serializeObject(objectE);
		ObjectE object = (ObjectE) Deserializer.deserializeObject(json);
		
		assertEquals(object.getC().get(0).getX(), 345);
		assertEquals(object.getC().get(0).getY(), 89.21f);
		assertEquals(object.getC().get(1).getX(), 67);
		assertEquals(object.getC().get(1).getY(), 76.2f);
		
	}

}
