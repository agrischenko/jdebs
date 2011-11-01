package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.dnd.MapVisibility;

/**
 * Реализация объекта карты доступной в скрипте
 * @author AAntonenko
 */
public class AIMap {
	
	private DebsMap map;
	private GameObject curObject;
	
	//кэш видимости видимости объектов 
	private Hashtable<MapPoint, Boolean> visibilityCache = new Hashtable<MapPoint, Boolean>();
	
	//кэш противников
	private Hashtable<MapPoint, Vector<AIEnemy>> enemysCache = new Hashtable<MapPoint, Vector<AIEnemy>>();
	
	//кэш союзников
	private Hashtable<MapPoint, Vector<AIAlly>> allysCache = new Hashtable<MapPoint, Vector<AIAlly>>();
	
	public AIMap(DebsMap map, GameObject object)
	{
		this.map = map;
		this.curObject = object;
	}
	
	/**
	 * Возвращает коллекцию объектов AIGameObject находящихся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIGameObject> getGameObjects(MapPoint point) {
		
		// Если запрошенная координата не видна текущему объекту - то возвращаем null
		if (!canSee(point)) return null;
		
		// Массив объектов которые расположены на карте
		Vector<GameObject> objects = this.map.getGameObjects(point);
		
		// Массив объектов которые будут доступны из скрипта (необходимо преобразовать из GameObject -> AIGameObject)
		Vector<AIGameObject> aiObjects = null;
		
		// Преобразование объектов из GameObject -> AIGameObject
		for (GameObject object : objects) {
			if (aiObjects == null) aiObjects = new Vector<AIGameObject>();
			aiObjects.add(new AIGameObject(object, map));
		}
		
		return aiObjects;
		
	}
	
	/**
	 * Возвращает коллекцию объектов AIEnemy находящихся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIEnemy> getEnemys(MapPoint point){
		
		if (enemysCache.get(point) == null) {
		
			// Получение всех объектов стоящих на указанной клетке
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// Отбор объектов у которых другая фракция
			Vector<AIEnemy> enemies	= new Vector<AIEnemy>();
			for (GameObject object : objects) {
				String faction = (String) object.getProperty("faction");
				if (faction != null && !faction.equalsIgnoreCase(curObjectFaction)) {
					enemies.add(new AIEnemy(object, this.map));
				}
			}
			
			enemysCache.put(point, enemies);
			
		}
		
		if (enemysCache.get(point).isEmpty()) return null;
		
		return enemysCache.get(point);
	}
	
	/**
	 * Возвращает коллекцию объектов AIAlly находящихся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIAlly> getAllys(MapPoint point){
		
		if (allysCache.get(point) == null) {
			
			// Получение всех объектов стоящих на указанной клетке
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// Отбор объектов у которых другая фракция
			Vector<AIAlly> allys = new Vector<AIAlly>();
			for (GameObject object : objects) {
				String faction = (String) object.getProperty("faction");
				if (faction != null && !faction.equalsIgnoreCase(curObjectFaction)) {
					allys.add(new AIAlly(object, this.map));
				}
			}
			
			allysCache.put(point, allys);
			
		}
		
		if (allysCache.get(point).isEmpty()) return null;
		
		return allysCache.get(point);
	}
	
	/**
	 * Возвращает объект AIEnemy находящийся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return объект, если объекта нет возвращается null
	 */
	public AIEnemy getEnemy(MapPoint point){
		
		if (enemysCache.get(point) == null) {
		
			// Получение всех объектов стоящих на указанной клетке
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// Отбор объектов у которых другая фракция
			Vector<AIEnemy> enemies	= new Vector<AIEnemy>();
			for (GameObject object : objects) {
				String faction = (String) object.getProperty("faction");
				if (faction != null && !faction.equalsIgnoreCase(curObjectFaction)) {
					enemies.add(new AIEnemy(object, this.map));
				}
			}
			
			enemysCache.put(point, enemies);
			
		}
		
		if (enemysCache.get(point).isEmpty()) return null;
		
		return enemysCache.get(point).firstElement();
	}
	
	/**
	 * Возвращает объект AIAlly находящийся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return объект, если объекта нет возвращается null
	 */
	public AIAlly getAlly(MapPoint point){
		
		if (allysCache.get(point) == null) {
			
			// Получение всех объектов стоящих на указанной клетке
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// Отбор объектов у которых другая фракция
			Vector<AIAlly> allys = new Vector<AIAlly>();
			for (GameObject object : objects) {
				String faction = (String) object.getProperty("faction");
				if (faction != null && !faction.equalsIgnoreCase(curObjectFaction)) {
					allys.add(new AIAlly(object, this.map));
				}
			}
			
			allysCache.put(point, allys);
			
		}
		
		if (allysCache.get(point).isEmpty()) return null;
		
		return allysCache.get(point).firstElement();
	}
	
	/**
	 * Определяет видит ли текущий объект указаный квадрат (использует кэш)
	 * @param point координаты квадрата
	 * @return true - видит; false - не видит
	 */
	private Boolean canSee(MapPoint point) {
		
		// Если видимость не кэшировалась, то определяем видимость и кэшируем ее
		if (visibilityCache.get(point) == null) {
			
			// Проверка находится ли запрошенная координата в области видимости (проверка расстояния)
			if (point.getDistanceTo(this.curObject.getMapPoint()) > (Integer) curObject.getProperty("rangeOfVisibility")) visibilityCache.put(point, false);
			
			//Проверка не преграждена ли линия видимости к запрошенной координате
			else visibilityCache.put(point, MapVisibility.see(map, curObject.getMapPoint(), point));
			
		}
		
		return visibilityCache.get(point);
	}
	
}
