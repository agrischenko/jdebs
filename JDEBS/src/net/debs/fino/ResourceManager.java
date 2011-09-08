package net.debs.fino;

import java.util.Hashtable;

/**
 * Реализация хранилища ресурсов
 * @author AGrischenko
 *
 */
public class ResourceManager {
	
	Hashtable<String, Resource> resources = new Hashtable<String, Resource>();

	public Resource getResource(String query) {
		return null;
	}
	
	public void putResource(String key, Resource resource) {
		resources.put(key, resource);
	}
}
