package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.dnd.MapMask;
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
	private Hashtable<MapPoint, Boolean> isEnemyCache = new Hashtable<MapPoint, Boolean>();
	private Hashtable<MapPoint, AIEnemy> enemysCache = new Hashtable<MapPoint, AIEnemy>();
	
	//кэш противников по расстоянию
	private Hashtable<Integer, Vector<AIEnemy>> enemysByDistanceCache = null;
	
	//кэш всех противников
	private Vector<AIEnemy> enemiesAll = null;
	
	//кэш союзников
	private Hashtable<MapPoint, Boolean> isAllyCache = new Hashtable<MapPoint, Boolean>();
	private Hashtable<MapPoint, AIAlly> allysCache = new Hashtable<MapPoint, AIAlly>();
	
	//кэш союзников по расстоянию
	private Hashtable<Integer, Vector<AIAlly>> allysByDistanceCache = null;
	
	//кэш всех союзников
	private Vector<AIAlly> allysAll = null;

	//Тест видимости
	public static void main(String [] args){
		
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);
		
		GameObject me = new GameObject();
		me.setProperty("rangeOfVisibility", 20);
		me.setProperty("seeable", true);
		me.setProperty("passable", false);
		map.addObject(0, 0, me);
		
		GameObject object1 = new GameObject();
		object1.setProperty("seeable", false);
		object1.setProperty("passable", false);

		map.addObject(5, 5, object1);
		map.addObject(4, 5, object1);
		map.addObject(5, 4, object1);
		
		System.out.println(map.seeable(new MapPoint(6, 6)));
		
	}
	
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
	 * Возвращает объект AIGameObject находящихся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return объект, если объекта по координатам нет нет возвращается null
	 */
	public AIGameObject getGameObject(MapPoint point) {
		
		// Если запрошенная координата не видна текущему объекту - то возвращаем null
		if (!canSee(point)) return null;
		
		GameObject object = this.map.getGameObject(point);
		if (object == null) return null; 
		
		return new AIGameObject(object);
		
	}
	
	/**
	 * Возвращает объект AIEnemy находящийся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return объект, если объекта нет возвращается null
	 */
	public AIEnemy getEnemy(MapPoint point){
		
		//Если объекта нет в кэше
		if (isEnemyCache.get(point) == null){
			
			// Получение объекта стоящего на указанной клетке
			GameObject object = this.map.getGameObject(point);
			AIEnemy enemy = null;
			
			if (object != null){
			
				String objectFaction = (String) object.getProperty("faction");
			
				//Если у объекта задана фракция
				if (objectFaction != null){
				
					String curObjectFaction = (String) this.curObject.getProperty("faction");
			
					//Если фракция объекта не та же что и у текущего
					if (!objectFaction.equalsIgnoreCase(curObjectFaction)) {
						enemy = new AIEnemy(object);
						enemysCache.put(point, enemy);
					}
				}
			}
			
			isEnemyCache.put(point, true);
			return enemy;
			
		}
		
		return enemysCache.get(point);
	}
	
	/**
	 * Возвращает объект AIAlly находящийся по переданным координатам (доступен из скрипта)
	 * @param point координаты
	 * @return объект, если объекта нет возвращается null
	 */
	public AIAlly getAlly(MapPoint point){
		
		//Если объекта нет в кэше
		if (isAllyCache.get(point) == null){
			
			// Получение объекта стоящего на указанной клетке
			GameObject object = this.map.getGameObject(point);
			AIAlly ally = null;
			
			if (object != null){
			
				String objectFaction = (String) object.getProperty("faction");
			
				//Если у объекта задана фракция
				if (objectFaction != null){
				
					String curObjectFaction = (String) this.curObject.getProperty("faction");
			
					//Если фракция объекта та же что и у текущего
					if (objectFaction.equalsIgnoreCase(curObjectFaction)) {
						ally = new AIAlly(object);
						allysCache.put(point, ally);
					}
				}
			}
			
			isAllyCache.put(point, true);
			return ally;
			
		}
		
		return allysCache.get(point);
	}
	
	/**
	 * Возвращает коллекцию всех видимых противников (AIEnemy)
	 * @return вектор объектов, если объектов нет возвращается пустой вектор
	 */
	public Vector<AIEnemy> getEnemys(){
		cacheEnemysByDistance();
		return enemiesAll;
	}
	
	/**
	 * Возвращает коллекцию всех видимых союзников (AIAlly)
	 * @return вектор объектов, если объектов нет возвращается пустой вектор
	 */
	public Vector<AIAlly> getAllys(){
		cacheAllysByDistance();
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
		
		MapPoint curObjectPoint = curObject.getMapPoint();
		
		for (int distance = 1; distance <= range; distance++) {
			
			Vector<MapPoint> points = MapMask.getMapPointsByDistance(map, curObjectPoint, distance);
			
			Vector<AIEnemy> enemies= new Vector<AIEnemy>();
			
			for (MapPoint point : points) {
				
				AIEnemy enemy = getEnemy(point);

				enemies.add(enemy);
				enemiesAll.add(enemy);

			}
			
			enemysByDistanceCache.put(distance, enemies);
			
		}
	}
	
	/**
	 * кэширует союзников по расстояниям, и всех союзников
	 */
	private void cacheAllysByDistance(){
		
		if (allysByDistanceCache != null) return;
		
		allysByDistanceCache = new Hashtable<Integer, Vector<AIAlly>>();
		
		int range = (Integer) this.curObject.getProperty("rangeOfVisibility");
		
		MapPoint curObjectPoint = curObject.getMapPoint();
		
		for (int distance = 1; distance <= range; distance++) {
			
			Vector<MapPoint> points = MapMask.getMapPointsByDistance(map, curObjectPoint, distance);
			
			Vector<AIAlly> allys = new Vector<AIAlly>();
			
			for (MapPoint point : points) {
				
				AIAlly ally = getAlly(point);

				allys.add(ally);
				allysAll.add(ally);

			}
			
			allysByDistanceCache.put(distance, allys);
			
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
	
	/**
	 * Определяет проходимый ли квадрат
	 * @param point координаты квадрата
	 * @return true - проходимый; false - не проходимый; null - квадрат не виден для текущего объекта
	 */
	public Boolean passable(MapPoint point){
		if (!canSee(point)) return null;
		return map.passable(point);
	}
	
	/**
	 * Определяет не приграждает ли квадрат линию видимости
	 * @param point координаты квадрата
	 * @return true - не преграждает; false - преграждает; null - квадрат не виден для текущего объекта
	 */
	public Boolean seeable(MapPoint point){
		if (!canSee(point)) return null;
		return map.seeable(point);
	}

	public int getWidth() {
		return this.map.getWidth();
	}

	public int getHeight() {
		return this.map.getHeight();
	}
	
}
