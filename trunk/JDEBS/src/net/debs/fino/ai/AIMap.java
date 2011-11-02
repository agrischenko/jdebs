package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.dnd.MapDistance;
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
	
	//кэш противников по расстоянию
	private Hashtable<Integer, Vector<AIEnemy>> enemysByDistanceCache = null;
	
	//кэш всех противников
	private Vector<AIEnemy> enemysAll = null;
	
	//кэш союзников
	private Hashtable<MapPoint, Vector<AIAlly>> allysCache = new Hashtable<MapPoint, Vector<AIAlly>>();
	
	//кэш союзников по расстоянию
	private Hashtable<Integer, Vector<AIAlly>> allysByDistanceCache = null;
	
	//кэш всех союзников
	private Vector<AIAlly> allysAll = null;
	
	public AIMap(DebsMap map, GameObject object)
	{
		this.map = map;
		this.curObject = object;
	}
	
	/**
	 * Возвращает текущий объект (для которого выполняется скрипт)
	 * @return текущий объект AIMe
	 */
	public AIMe getMe(){
		return AICore.gAiMe;
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
			AIGameObject aiGameObject = new AIGameObject(object);
			aiObjects.add(aiGameObject);
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
					enemies.add(new AIEnemy(object));
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
					allys.add(new AIAlly(object));
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
					enemies.add(new AIEnemy(object));
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
		
			// Отбор объектов у которых такая же фракция
			Vector<AIAlly> allys = new Vector<AIAlly>();
			for (GameObject object : objects) {
				String faction = (String) object.getProperty("faction");
				if (faction != null && !faction.equalsIgnoreCase(curObjectFaction)) {
					allys.add(new AIAlly(object));
				}
			}
			
			allysCache.put(point, allys);
			
		}
		
		if (allysCache.get(point).isEmpty()) return null;
		
		return allysCache.get(point).firstElement();
	}
	
	/**
	 * Возвращает коллекцию всех видимых противников (AIEnemy)
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIEnemy> getEnemys(){
		cacheEnemysByDistance();
		if (enemysAll.isEmpty()) return null; 
		return enemysAll;
	}
	
	/**
	 * Возвращает коллекцию всех видимых союзников (AIAlly)
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIAlly> getAllys(){
		cacheAllysByDistance();
		if (allysAll.isEmpty()) return null; 
		return allysAll;
	}
	
	/**
	 * Возвращает ближайшего врага (AIEnemy)
	 * @return объект AIEnemy, если видимых врагов нет - возвращает null
	 */
	public AIEnemy getNearestEnemy(){
		
		int range = (Integer) this.curObject.getProperty("rangeOfVisibility");
		
		cacheEnemysByDistance();
		Vector<AIEnemy> enemys = null;
		
		for (int d = 1; d <= range; d++) {
			enemys = enemysByDistanceCache.get(d);
			if ((enemys != null) && (!enemys.isEmpty())) return enemys.firstElement();
		}
		
		return null;
	}
	
	/**
	 * Возвращает ближайшего союзника (AIAlly)
	 * @return объект AIAlly, если видимых союзников нет - возвращает null
	 */
	public AIAlly getNearestAlly(){
		
		int range = (Integer) this.curObject.getProperty("rangeOfVisibility");
		
		cacheAllysByDistance();
		Vector<AIAlly> allys = null;
		
		for (int d = 1; d <= range; d++) {
			allys = allysByDistanceCache.get(d);
			if ((allys != null) && (!allys.isEmpty())) return allys.firstElement();
		}
		
		return null;
	}
	
	/**
	 * Возвращает коллекцию всех видимых противников в указаном радиусе
	 * @param range радиус для поиска противников
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIEnemy> getEnemysInRange(Integer range){
		
		cacheEnemysByDistance();
		
		Vector<AIEnemy> enemysInRange = new Vector<AIEnemy>();
		
		for (int i = 1; i <= range; i++) {
			Vector<AIEnemy> enemys = enemysByDistanceCache.get(i);
			if (enemys != null) enemysInRange.addAll(enemys);
		}
		
		if (enemysInRange.isEmpty()) return null;
		
		return enemysInRange;
	}
	
	/**
	 * Возвращает коллекцию всех видимых союзников в указаном радиусе
	 * @param range радиус для поиска союзников
	 * @return вектор объектов, если объектов нет возвращается null
	 */
	public Vector<AIAlly> getAllysInRange(Integer range){
		
		cacheEnemysByDistance();
		
		Vector<AIAlly> allysInRange = new Vector<AIAlly>();
		
		for (int i = 1; i <= range; i++) {
			Vector<AIAlly> allys = allysByDistanceCache.get(i);
			if (allys != null) allysInRange.addAll(allys);
		}
		
		if (allysInRange.isEmpty()) return null;
		
		return allysInRange;
	}
	
	/**
	 * кэширует противников по расстояниям, и всех противников
	 */
	private void cacheEnemysByDistance(){
		
		if (enemysByDistanceCache != null) return;
		
		enemysByDistanceCache = new Hashtable<Integer, Vector<AIEnemy>>();
		
		int range = (Integer) this.curObject.getProperty("rangeOfVisibility");
		
		int x1 = curObject.getMapPoint().getX() - range;
		if (x1 < 0) x1 = 0;
		int x2 = curObject.getMapPoint().getX() + range;
		if (x2 >= map.getWidth()) x2 = map.getWidth() - 1;
		int y1 = curObject.getMapPoint().getY() - range;
		if (y1 < 0) y1 = 0;
		int y2 = curObject.getMapPoint().getY() + range;
		if (y2 >= map.getHeight()) y2 = map.getHeight() - 1;
		
		MapPoint point = null;
		MapPoint curObjectPoint = curObject.getMapPoint();
		Integer distance;
		
		enemysAll = new Vector<AIEnemy>();
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				point = new MapPoint(x, y);
				if (canSee(point)) {
					distance = MapDistance.distance(map, point, curObjectPoint);
					Vector<AIEnemy> enemys = enemysByDistanceCache.get(distance);
					if (enemys == null) enemys = getEnemys(point);
					else enemys.addAll(getEnemys(point));
					enemysByDistanceCache.put(distance, enemys);
					enemysAll.addAll(enemys);
				}
			}
		}
		
	}
	
	/**
	 * кэширует союзников по расстояниям, и всех союзников
	 */
	private void cacheAllysByDistance(){
		
		if (allysByDistanceCache != null) return;
		
		allysByDistanceCache = new Hashtable<Integer, Vector<AIAlly>>();
		
		int range = (Integer) this.curObject.getProperty("rangeOfVisibility");
		
		int x1 = curObject.getMapPoint().getX() - range;
		if (x1 < 0) x1 = 0;
		int x2 = curObject.getMapPoint().getX() + range;
		if (x2 >= map.getWidth()) x2 = map.getWidth() - 1;
		int y1 = curObject.getMapPoint().getY() - range;
		if (y1 < 0) y1 = 0;
		int y2 = curObject.getMapPoint().getY() + range;
		if (y2 >= map.getHeight()) y2 = map.getHeight() - 1;
		
		MapPoint point = null;
		MapPoint curObjectPoint = curObject.getMapPoint();
		Integer distance;
		
		allysAll = new Vector<AIAlly>();
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				point = new MapPoint(x, y);
				if (canSee(point)) {
					distance = MapDistance.distance(map, point, curObjectPoint);
					Vector<AIAlly> allys = allysByDistanceCache.get(distance);
					if (allys == null) allys = getAllys(point);
					else allys.addAll(getAllys(point));
					allysByDistanceCache.put(distance, allys);
					allysAll.addAll(allys);
				}
			}
		}
		
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
			
			// Проверка не преграждена ли линия видимости к запрошенной координате
			else visibilityCache.put(point, MapVisibility.see(map, curObject.getMapPoint(), point));
			
		}
		
		return visibilityCache.get(point);
	}
	
}
