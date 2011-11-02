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
	private Hashtable<Integer, Vector<AIEnemy>> enemysByDistanceCache = null;
		
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
			enemys = enemysByDistanceCache.get(d);
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
	 * @return ������ ��������, ���� �������� ��� ������������ null
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
	 * ���������� ��������� ���� ������� ��������� � �������� ������� �� �������
	 * @param range ������ ��� ������ ���������
	 * @return ������ ��������, ���� �������� ��� ������������ null
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
	 * �������� ����������� �� �����������
	 */
	private void cacheEnemysByDistance(){
		
		if (enemysByDistanceCache != null) return;
		
		Vector<AIEnemy> allEnemys = AICore.gAiMap.getEnemys();
		
		MapPoint point = null;
		MapPoint curObjectPoint = object.getMapPoint();
		Integer distance;
		
		for (AIEnemy enemy : allEnemys) {
			distance = enemy.getDistanceTo(curObjectPoint);
			Vector<AIEnemy> enemys = enemysByDistanceCache.get(distance);
			if (enemys == null) enemys = AICore.gAiMap.getEnemys(point);
			else enemys.addAll(AICore.gAiMap.getEnemys(point));
			enemysByDistanceCache.put(distance, enemys);
		}
	}
	
	/**
	 * �������� ��������� �� �����������
	 */
	private void cacheAllysByDistance(){
		
		if (allysByDistanceCache != null) return;
		
		Vector<AIAlly> allAllys = AICore.gAiMap.getAllys();
		
		MapPoint point = null;
		MapPoint curObjectPoint = object.getMapPoint();
		Integer distance;
		
		for (AIAlly ally : allAllys) {
			distance = ally.getDistanceTo(curObjectPoint);
			Vector<AIAlly> allys = allysByDistanceCache.get(distance);
			if (allys == null) allys = AICore.gAiMap.getAllys(point);
			else allys.addAll(AICore.gAiMap.getAllys(point));
			allysByDistanceCache.put(distance, allys);
		}
	}
	
}
