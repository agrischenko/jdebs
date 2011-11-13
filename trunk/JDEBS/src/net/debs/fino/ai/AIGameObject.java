package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;

/**
 * ���������� �������� ������� ���������� � �������
 * @author AAntonenko
 *
 */
public class AIGameObject {

	protected GameObject object;

	//��� ����������� �� ����������
	private Hashtable<Integer, Vector<AIEnemy>> enemiesByDistanceCache = null;
		
	//��� ��������� �� ����������
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
	 * ���������� ���������� ���������� (AIEnemy)
	 * @return ������ AIEnemy, ���� ������� ����������� ��� - ���������� null
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
	 * ���������� ���������� �������� (AIAlly)
	 * @return ������ AIAlly, ���� ������� ��������� ��� - ���������� null
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
	 * ���������� ��������� ���� ������� ����������� � �������� ������� �� �������
	 * @param range ������ ��� ������ �����������
	 * @return ������ ��������, ���� �������� ��� ������������ ������ ������
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
	 * ���������� ��������� ���� ������� ��������� � �������� ������� �� �������
	 * @param range ������ ��� ������ ���������
	 * @return ������ ��������, ���� �������� ��� ������������ ������ ������
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
	 * �������� ����������� �� �����������
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
	 * �������� ��������� �� �����������
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
