package net.debs.fino.ai;

import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

/**
 * Реализация объекта карты доступной в скрипте
 * @author AAntonenko
 */
public class AIMap {
	
	private DebsMap map;
	private GameObject curObject;
	
	public AIMap(DebsMap map, GameObject object)
	{
		this.map = map;
		this.curObject = object;
	}
	
	/**
	 * Возвращает коллекцию объектов находящихся по переданным координатам
	 * @param point координаты
	 * @return вектор объектов
	 */
	public Vector<AIGameObject> geGameObject(MapPoint point)
	{
		
		// Если запрошенная координата находится не в области видимости текущего объекта - то возвращаем null
		if (point.getDistanceTo(this.curObject.getMapPoint()) > (Integer) curObject.getProperty("rangeOfVisibility").get()) return null;
		
		// Массив объектов которые расположены на карте
		Vector<MapObject> objects = map.getMapObjects(this.curObject.getMapPoint());
		
		// Массив объектов которые будут доступны из скрипта (необходимо преобразовать из MapObject -> AIGameObject)
		Vector<AIGameObject> aiObjects = new Vector<AIGameObject>();
		
		// Отбор только объектов которые реализуют GameObject
		for (MapObject object : objects) {
			if (object instanceof GameObject) {
				GameObject gameObject = (GameObject) object;
				aiObjects.add(new AIGameObject(gameObject, map));
			}
		}
		
		return aiObjects;
		
	}
	
}
