package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.dnd.MapDistance;
import net.debs.fino.dnd.MapVisibility;

/**
 * ���������� ������� ����� ��������� � �������
 * @author AAntonenko
 */
public class AIMap {
	
	private DebsMap map;
	private GameObject curObject;
	
	//��� ��������� ��������� �������� 
	private Hashtable<MapPoint, Boolean> visibilityCache = new Hashtable<MapPoint, Boolean>();
	
	//��� �����������
	private Hashtable<MapPoint, Vector<AIEnemy>> enemysCache = new Hashtable<MapPoint, Vector<AIEnemy>>();
	
	//��� ����������� �� ����������
	private Hashtable<Integer, Vector<AIEnemy>> enemysByDistanceCache = null;
	
	//��� ���� �����������
	private Vector<AIEnemy> enemysAll = null;
	
	//��� ���������
	private Hashtable<MapPoint, Vector<AIAlly>> allysCache = new Hashtable<MapPoint, Vector<AIAlly>>();
	
	//��� ��������� �� ����������
	private Hashtable<Integer, Vector<AIAlly>> allysByDistanceCache = null;
	
	//��� ���� ���������
	private Vector<AIAlly> allysAll = null;
	
	public AIMap(DebsMap map, GameObject object)
	{
		this.map = map;
		this.curObject = object;
	}
	
	/**
	 * ���������� ������� ������ (��� �������� ����������� ������)
	 * @return ������� ������ AIMe
	 */
	public AIMe getMe(){
		return AICore.gAiMe;
	}
	
	/**
	 * ���������� ��������� �������� AIGameObject ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������ ��������, ���� �������� ��� ������������ null
	 */
	public Vector<AIGameObject> getGameObjects(MapPoint point) {
		
		// ���� ����������� ���������� �� ����� �������� ������� - �� ���������� null
		if (!canSee(point)) return null;
		
		// ������ �������� ������� ����������� �� �����
		Vector<GameObject> objects = this.map.getGameObjects(point);
		
		// ������ �������� ������� ����� �������� �� ������� (���������� ������������� �� GameObject -> AIGameObject)
		Vector<AIGameObject> aiObjects = null;
		
		// �������������� �������� �� GameObject -> AIGameObject
		for (GameObject object : objects) {
			if (aiObjects == null) aiObjects = new Vector<AIGameObject>();
			AIGameObject aiGameObject = new AIGameObject(object);
			aiObjects.add(aiGameObject);
		}
		
		return aiObjects;
		
	}
	
	/**
	 * ���������� ��������� �������� AIEnemy ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������ ��������, ���� �������� ��� ������������ null
	 */
	public Vector<AIEnemy> getEnemys(MapPoint point){
		
		if (enemysCache.get(point) == null) {
		
			// ��������� ���� �������� ������� �� ��������� ������
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// ����� �������� � ������� ������ �������
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
	 * ���������� ��������� �������� AIAlly ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������ ��������, ���� �������� ��� ������������ null
	 */
	public Vector<AIAlly> getAllys(MapPoint point){
		
		if (allysCache.get(point) == null) {
			
			// ��������� ���� �������� ������� �� ��������� ������
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// ����� �������� � ������� ������ �������
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
	 * ���������� ������ AIEnemy ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������, ���� ������� ��� ������������ null
	 */
	public AIEnemy getEnemy(MapPoint point){
		
		if (enemysCache.get(point) == null) {
		
			// ��������� ���� �������� ������� �� ��������� ������
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// ����� �������� � ������� ������ �������
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
	 * ���������� ������ AIAlly ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������, ���� ������� ��� ������������ null
	 */
	public AIAlly getAlly(MapPoint point){
		
		if (allysCache.get(point) == null) {
			
			// ��������� ���� �������� ������� �� ��������� ������
			Vector<GameObject> objects = this.map.getGameObjects(point);
		
			String curObjectFaction = (String) this.curObject.getProperty("faction");
		
			// ����� �������� � ������� ����� �� �������
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
	 * ���������� ��������� ���� ������� ����������� (AIEnemy)
	 * @return ������ ��������, ���� �������� ��� ������������ null
	 */
	public Vector<AIEnemy> getEnemys(){
		cacheEnemysByDistance();
		if (enemysAll.isEmpty()) return null; 
		return enemysAll;
	}
	
	/**
	 * ���������� ��������� ���� ������� ��������� (AIAlly)
	 * @return ������ ��������, ���� �������� ��� ������������ null
	 */
	public Vector<AIAlly> getAllys(){
		cacheAllysByDistance();
		if (allysAll.isEmpty()) return null; 
		return allysAll;
	}
	
	/**
	 * ���������� ���������� ����� (AIEnemy)
	 * @return ������ AIEnemy, ���� ������� ������ ��� - ���������� null
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
	 * ���������� ���������� �������� (AIAlly)
	 * @return ������ AIAlly, ���� ������� ��������� ��� - ���������� null
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
	 * ���������� ��������� ���� ������� ����������� � �������� �������
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
	 * ���������� ��������� ���� ������� ��������� � �������� �������
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
	 * �������� ����������� �� �����������, � ���� �����������
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
	 * �������� ��������� �� �����������, � ���� ���������
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
	 * ���������� ����� �� ������� ������ �������� ������� (���������� ���)
	 * @param point ���������� ��������
	 * @return true - �����; false - �� �����
	 */
	private Boolean canSee(MapPoint point) {
		
		// ���� ��������� �� ������������, �� ���������� ��������� � �������� ��
		if (visibilityCache.get(point) == null) {
			
			// �������� ��������� �� ����������� ���������� � ������� ��������� (�������� ����������)
			if (point.getDistanceTo(this.curObject.getMapPoint()) > (Integer) curObject.getProperty("rangeOfVisibility")) visibilityCache.put(point, false);
			
			// �������� �� ����������� �� ����� ��������� � ����������� ����������
			else visibilityCache.put(point, MapVisibility.see(map, curObject.getMapPoint(), point));
			
		}
		
		return visibilityCache.get(point);
	}
	
}
