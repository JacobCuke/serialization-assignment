package serialization;

public class ObjectD {

	private ObjectA[] b;
	
	public ObjectD() {
		this.b = null;
	}
	
	public ObjectD(ObjectA[] b) {
		this.b = b;
	}

	public ObjectA[] getB() {
		return b;
	}
	
}
