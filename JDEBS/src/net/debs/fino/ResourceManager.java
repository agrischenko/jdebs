package net.debs.fino;

import java.util.Hashtable;

/**
 * Реализация хранилища ресурсов
 * @author AGrischenko
 *
 */
public class ResourceManager {
	
	Hashtable<String, Resource> resources = new Hashtable<String, Resource>();

	protected static ResourceManager me = new ResourceManager();
	
	protected ResourceManager() {
	}
	
	public static ResourceManager me() {
		return me;
	}
	
	public Resource getResource(String query) {
		return null;
	}
	
	public void putResource(String key, Resource resource) {
		resources.put(key, resource);
	}
	
	
}
