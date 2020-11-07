package serialization;

import java.util.ArrayList;

public class ObjectE {

	private ArrayList<ObjectA> c;
	
	public ObjectE() {
		this.c = null;
	}
	
	public ObjectE(ArrayList<ObjectA> c) {
		this.c = c;
	}

	public ArrayList<ObjectA> getC() {
		return c;
	}
	
}
