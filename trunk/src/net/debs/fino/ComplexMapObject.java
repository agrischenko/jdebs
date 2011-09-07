package net.debs.fino;

import java.util.Hashtable;

/**
 * Реализация сложного объекта
 * @author AGrischenko
 *
 */
public abstract class ComplexMapObject extends MapObject {
	/**
	 * Объекты по локальным координатам
	 */
	Hashtable<MapPoint, MapObject> objects;
	
}
