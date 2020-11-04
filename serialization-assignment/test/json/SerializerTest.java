package json;

import static org.junit.jupiter.api.Assertions.*;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import serialization.ObjectA;
import serialization.ObjectB;

class SerializerTest {

	@Test
	void testObjectA() throws Exception {
		
		ObjectA objectA = new ObjectA(1, 2.0f);
		
		JsonObject json = null;
		json = Serializer.serializeObject(objectA);
		
		String correct = "{\"objects\":[{\"class\":\"serialization.ObjectA\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"1\"},{\"name\":\"y\",\"declaringclass\":\"serialization.ObjectA\",\"value\":\"2.0\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}
	
	@Test
	void testObjectB() throws Exception {
		
		ObjectB objectB1 = new ObjectB(true);
		ObjectB objectB2 = new ObjectB(false);
		objectB1.setOther(objectB2);
		objectB2.setOther(objectB1);
		
		JsonObject json = null;
		json = Serializer.serializeObject(objectB1);
		
		String correct = "{\"objects\":[{\"class\":\"serialization.ObjectB\",\"id\":\"1\",\"type\":\"object\",\"fields\":[{\"name\":\"z\",\"declaringclass\":\"serialization.ObjectB\",\"value\":\"false\"},{\"name\":\"other\",\"declaringclass\":\"serialization.ObjectB\",\"reference\":\"0\"}]},{\"class\":\"serialization.ObjectB\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"z\",\"declaringclass\":\"serialization.ObjectB\",\"value\":\"true\"},{\"name\":\"other\",\"declaringclass\":\"serialization.ObjectB\",\"reference\":\"1\"}]}]}";
		assertEquals(json.toString(), correct);
		
	}

}
