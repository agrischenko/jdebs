package net.debs.fino;

import java.util.Hashtable;

/**
 * Реализация игрового объекта
 * @author AGrischenko
 *
 */
public class GameObject extends ComplexMapObject {

	/**
	 * видно в AI Script
	 */
	Hashtable<String, Object> scriptProperties = new Hashtable<String, Object>();
	/**
	 * не видно в AI Script
	 */
	Hashtable<String, Object> properties = new Hashtable<String, Object>();

	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public Object setProperty(String key, Object value) {
		Object oldValue = getProperty(key);
		properties.put(key, value);
		return oldValue;
	}
	
	
	
}
