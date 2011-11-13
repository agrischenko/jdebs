package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.dnd.MapMask;
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
	private Hashtable<MapPoint, Boolean> isEnemyCache = new Hashtable<MapPoint, Boolean>();
	private Hashtable<MapPoint, AIEnemy> enemysCache = new Hashtable<MapPoint, AIEnemy>();
	
	//��� ����������� �� ����������
	private Hashtable<Integer, Vector<AIEnemy>> enemysByDistanceCache = null;
	
	//��� ���� �����������
	private Vector<AIEnemy> enemiesAll = null;
	
	//��� ���������
	private Hashtable<MapPoint, Boolean> isAllyCache = new Hashtable<MapPoint, Boolean>();
	private Hashtable<MapPoint, AIAlly> allysCache = new Hashtable<MapPoint, AIAlly>();
	
	//��� ��������� �� ����������
	private Hashtable<Integer, Vector<AIAlly>> allysByDistanceCache = null;
	
	//��� ���� ���������
	private Vector<AIAlly> allysAll = null;

	//���� ���������
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
	 * ���������� ������� ������ (��� �������� ����������� ������)
	 * @return ������� ������ AIMe
	 */
	public AIMe getMe(){
		return AICore.gAiMe;
	}
	
	/**
	 * ���������� ������ AIGameObject ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������, ���� ������� �� ����������� ��� ��� ������������ null
	 */
	public AIGameObject getGameObject(MapPoint point) {
		
		// ���� ����������� ���������� �� ����� �������� ������� - �� ���������� null
		if (!canSee(point)) return null;
		
		GameObject object = this.map.getGameObject(point);
		if (object == null) return null; 
		
		return new AIGameObject(object);
		
	}
	
	/**
	 * ���������� ������ AIEnemy ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������, ���� ������� ��� ������������ null
	 */
	public AIEnemy getEnemy(MapPoint point){
		
		//���� ������� ��� � ����
		if (isEnemyCache.get(point) == null){
			
			// ��������� ������� �������� �� ��������� ������
			GameObject object = this.map.getGameObject(point);
			AIEnemy enemy = null;
			
			if (object != null){
			
				String objectFaction = (String) object.getProperty("faction");
			
				//���� � ������� ������ �������
				if (objectFaction != null){
				
					String curObjectFaction = (String) this.curObject.getProperty("faction");
			
					//���� ������� ������� �� �� �� ��� � � ��������
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
	 * ���������� ������ AIAlly ����������� �� ���������� ����������� (�������� �� �������)
	 * @param point ����������
	 * @return ������, ���� ������� ��� ������������ null
	 */
	public AIAlly getAlly(MapPoint point){
		
		//���� ������� ��� � ����
		if (isAllyCache.get(point) == null){
			
			// ��������� ������� �������� �� ��������� ������
			GameObject object = this.map.getGameObject(point);
			AIAlly ally = null;
			
			if (object != null){
			
				String objectFaction = (String) object.getProperty("faction");
			
				//���� � ������� ������ �������
				if (objectFaction != null){
				
					String curObjectFaction = (String) this.curObject.getProperty("faction");
			
					//���� ������� ������� �� �� ��� � � ��������
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
	 * ���������� ��������� ���� ������� ����������� (AIEnemy)
	 * @return ������ ��������, ���� �������� ��� ������������ ������ ������
	 */
	public Vector<AIEnemy> getEnemys(){
		cacheEnemysByDistance();
		return enemiesAll;
	}
	
	/**
	 * ���������� ��������� ���� ������� ��������� (AIAlly)
	 * @return ������ ��������, ���� �������� ��� ������������ ������ ������
	 */
	public Vector<AIAlly> getAllys(){
		cacheAllysByDistance();
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
	 * �������� ��������� �� �����������, � ���� ���������
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
	
	/**
	 * ���������� ���������� �� �������
	 * @param point ���������� ��������
	 * @return true - ����������; false - �� ����������; null - ������� �� ����� ��� �������� �������
	 */
	public Boolean passable(MapPoint point){
		if (!canSee(point)) return null;
		return map.passable(point);
	}
	
	/**
	 * ���������� �� ����������� �� ������� ����� ���������
	 * @param point ���������� ��������
	 * @return true - �� �����������; false - �����������; null - ������� �� ����� ��� �������� �������
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
