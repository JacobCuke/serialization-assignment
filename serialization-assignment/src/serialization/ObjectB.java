package serialization;

public class ObjectB {

	public boolean z;
	public ObjectB other;
	
	public ObjectB(boolean z) {
		this.z = z;
		this.other = null;
	}
	
	public void setOther(ObjectB other) {
		
		this.other = other;
		
	}
	
}
