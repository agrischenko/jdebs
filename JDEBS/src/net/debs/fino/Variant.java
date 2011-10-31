package net.debs.fino;

public class Variant {
	protected Object value;
	
	public Variant(Object value) {
		this.value = value;
	}
	
	public Variant() {
	}

	public Object get() {
		return value;
	}

	public String getString() {
		return value.toString();
	}

	public boolean isNull() {
		return value==null;
	}
}
