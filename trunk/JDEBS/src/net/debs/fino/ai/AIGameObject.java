package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;

/**
 * Реализация игрового объекта доступного в скрипте
 * @author AAntonenko
 *
 */
public class AIGameObject {

	protected GameObject object;

	//кэш противников по расстоянию
	private Hashtable<Integer, Vector<AIEnemy>> enemiesByDistanceCache = null;
		
	//кэш союзников по расстоянию
	private Hashtable<Integer, Vector<AIAlly>> allysByDistanceCache = null;
	
	public AIGameObject(GameObject object) {
		this.object = object;
	}

	public String getId() {
		return object.getId();
	}

	public MapPoint getMapPoint(){
		return object.getMapPoint();
	}
	
	public Integer getDistanceTo(AIGameObject object){
		return this.object.getMapPoint().getDistanceTo(object.getMapPoint());
	}
	
	public Integer getDistanceTo(MapPoint point){
		return this.object.getMapPoint().getDistanceTo(point);
	}
	
	public Boolean isEnemy() {
		String faction = (String) object.getProperty("faction");
		String curObjectFaction = (String) object.getProperty("faction");
		if ((faction != null) && (!faction.equalsIgnoreCase(curObjectFaction))) return true;
		return false;
	}
	
	public Boolean isAlly() {
		String faction = (String) object.getProperty("faction");
		String curObjectFaction = (String) object.getProperty("faction");
		if ((faction != null) && (faction.equalsIgnoreCase(curObjectFaction))) return true;
		return false;
	}
	
	/**
	 * Возвращает ближайшего противника (AIEnemy)
	 * @return объект AIEnemy, если видимых противников нет - возвращает null
	 */
	public AIEnemy getNearestEnemy(){
		
		cacheEnemysByDistance();
		
		int range = 2 * ((Integer) AICore.gObject.getProperty("rangeOfVisibility")) + 1;

		Vector<AIEnemy> enemys = null;
		
		for (int d = 1; d <= range; d++) {
			enemys = enemiesByDistanceCache.get(d);
			if ((enemys != null) && (!enemys.isEmpty())) return enemys.firstElement();
		}
		
		return null;
	}
	
	/**
	 * Возвращает ближайшего союзника (AIAlly)
	 * @return объект AIAlly, если видимых союзников нет - возвращает null
	 */
	public AIAlly getNearestAlly(){
		
		cacheAllysByDistance();
		
		int range = 2 * ((Integer) AICore.gObject.getProperty("rangeOfVisibility")) + 1;
		
		Vector<AIAlly> allys = null;
		
		for (int d = 1; d <= range; d++) {
			allys = allysByDistanceCache.get(d);
			if ((allys != null) && (!allys.isEmpty())) return allys.firstElement();
		}
		
		return null;
	}
	
	/**
	 * Возвращает коллекцию всех видимых противников в указаном радиусе от объекта
	 * @param range радиус для поиска противников
	 * @return вектор объектов, если объектов нет возвращается пустой вектор
	 */
	public Vector<AIEnemy> getEnemiesInRange(Integer range){
		
		cacheEnemysByDistance();
		
		Vector<AIEnemy> enemysInRange = new Vector<AIEnemy>();
		
		for (int i = 1; i <= range; i++) {
			Vector<AIEnemy> enemys = enemiesByDistanceCache.get(i);
			if (enemys != null) enemysInRange.addAll(enemys);
		}
		
		return enemysInRange;
	}
	
	/**
	 * Возвращает коллекцию всех видимых союзников в указаном радиусе от объекта
	 * @param range радиус для поиска союзников
	 * @return вектор объектов, если объектов нет возвращается пустой вектор
	 */
	public Vector<AIAlly> getAllysInRange(Integer range){
		
		cacheEnemysByDistance();
		
		Vector<AIAlly> allysInRange = new Vector<AIAlly>();
		
		for (int i = 1; i <= range; i++) {
			Vector<AIAlly> allys = allysByDistanceCache.get(i);
			if (allys != null) allysInRange.addAll(allys);
		}
		
		return allysInRange;
	}
	
	/**
	 * кэширует противников по расстояниям
	 */
	private void cacheEnemysByDistance(){
		
		if (enemiesByDistanceCache != null) return;
		
		Vector<AIEnemy> allEnemys = AICore.gAiMap.getEnemys();
		
		MapPoint curObjectPoint = object.getMapPoint();
		Integer distance;
		
		enemiesByDistanceCache = new Hashtable<Integer, Vector<AIEnemy>>();
		
		for (AIEnemy enemy : allEnemys) {
			distance = enemy.getDistanceTo(curObjectPoint);
			Vector<AIEnemy> enemys = enemiesByDistanceCache.get(distance);
			if (enemys == null) enemys = new Vector<AIEnemy>();
			enemys.add(enemy);
			enemiesByDistanceCache.put(distance, enemys);
		}
	}
	
	/**
	 * кэширует союзников по расстояниям
	 */
	private void cacheAllysByDistance(){
		
		if (allysByDistanceCache != null) return;
		
		Vector<AIAlly> allAllys = AICore.gAiMap.getAllys();
		
		MapPoint curObjectPoint = object.getMapPoint();
		Integer distance;
		
		allysByDistanceCache = new Hashtable<Integer, Vector<AIAlly>>();
		
		for (AIAlly ally : allAllys) {
			distance = ally.getDistanceTo(curObjectPoint);
			Vector<AIAlly> allys = allysByDistanceCache.get(distance);
			if (allys == null) allys = new Vector<AIAlly>();
			allys.add(ally);
			allysByDistanceCache.put(distance, allys);
		}
	}
	
}
