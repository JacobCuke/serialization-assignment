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

class SerializerTest {

	@Test
	void testObjectA() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		
		JsonObject json = Serializer.serializeObject(objectA);
		
		String correct = "{\"objects\":[{\"class\":\"serialization.ObjectA\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"1\"},{\"name\":\"y\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"2.0\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}
	
	@Test
	void testObjectB() throws Exception {
		
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		
		JsonObject json = Serializer.serializeObject(objectB1);
		
		String correct = "{\"objects\":[{\"class\":\"serialization.ObjectB\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"z\",\"declaringclass\":\"serialization.ObjectB\",\"value\":\"false\"},{\"name\":\"other\",\"declaringclass\":\"serialization.ObjectB\",\"reference\":\"0\"}]},{\"class\":\"serialization.ObjectB\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"z\",\"declaringclass\":\"serialization.ObjectB\",\"value\":\"true\"},{\"name\":\"other\",\"declaringclass\":\"serialization.ObjectB\",\"reference\":\"1\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}
	
	@Test
	void testObjectC() throws Exception {
		
		int[] a = {0, 0, 0, 3, 0};
		ObjectC objectC = new ObjectC(a);
		
		JsonObject json = Serializer.serializeObject(objectC);
		
		String correct = "{\"objects\":[{\"class\":\"[I\",\"id\":\"1\",\"type\":\"array\",\"length\":\"5\",\"entries\":[{\"value\":\"0\"},{\"value\":\"0\"},{\"value\":\"0\"},{\"value\":\"3\"},{\"value\":\"0\"}]},{\"class\":\"serialization.ObjectC\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"a\",\"declaringclass\":\"serialization.ObjectC\",\"reference\":\"1\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}
	
	@Test
	void testObjectD() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		ObjectA[] b = new ObjectA[5];
		b[3] = objectA;
		
		ObjectD objectD = new ObjectD(b);
		JsonObject json = Serializer.serializeObject(objectD);
		
		String correct = "{\"objects\":[{\"class\":\"serialization.ObjectA\",\"id\":\"2\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"1\"},{\"name\":\"y\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"2.0\"}]},{\"class\":\"[Lserialization.ObjectA;\",\"id\":\"1\",\"type\":\"array\",\"length\":\"5\",\"entries\":[{\"reference\":\"null\"},{\"reference\":\"null\"},{\"reference\":\"null\"},{\"reference\":\"2\"},{\"reference\":\"null\"}]},{\"class\":\"serialization.ObjectD\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"b\",\"declaringclass\":\"serialization.ObjectD\",\"reference\":\"1\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}

}
