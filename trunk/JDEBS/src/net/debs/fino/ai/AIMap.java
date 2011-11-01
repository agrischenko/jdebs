package net.debs.fino.ai;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
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
	
	//��� ���������
	private Hashtable<MapPoint, Vector<AIAlly>> allysCache = new Hashtable<MapPoint, Vector<AIAlly>>();
	
	public AIMap(DebsMap map, GameObject object)
	{
		this.map = map;
		this.curObject = object;
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
			aiObjects.add(new AIGameObject(object, map));
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
					enemies.add(new AIEnemy(object, this.map));
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
					allys.add(new AIAlly(object, this.map));
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
					enemies.add(new AIEnemy(object, this.map));
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
		
			// ����� �������� � ������� ������ �������
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
	 * ���������� ����� �� ������� ������ �������� ������� (���������� ���)
	 * @param point ���������� ��������
	 * @return true - �����; false - �� �����
	 */
	private Boolean canSee(MapPoint point) {
		
		// ���� ��������� �� ������������, �� ���������� ��������� � �������� ��
		if (visibilityCache.get(point) == null) {
			
			// �������� ��������� �� ����������� ���������� � ������� ��������� (�������� ����������)
			if (point.getDistanceTo(this.curObject.getMapPoint()) > (Integer) curObject.getProperty("rangeOfVisibility")) visibilityCache.put(point, false);
			
			//�������� �� ����������� �� ����� ��������� � ����������� ����������
			else visibilityCache.put(point, MapVisibility.see(map, curObject.getMapPoint(), point));
			
		}
		
		return visibilityCache.get(point);
	}
	
}
