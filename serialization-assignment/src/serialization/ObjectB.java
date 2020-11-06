package serialization;

public class ObjectB {

	private boolean z;
	private ObjectB other;
	
	public ObjectB() {
		this.z = false;
		this.other = null;
	}
	
	public ObjectB(boolean z) {
		this.z = z;
		this.other = null;
	}
	
	public void setOther(ObjectB other) {
		
		this.other = other;
		
	}
	
}
