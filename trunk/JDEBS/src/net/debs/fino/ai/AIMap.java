package net.debs.fino.ai;

import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

/**
 * ���������� ������� ����� ��������� � �������
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
	 * ���������� ��������� �������� ����������� �� ���������� �����������
	 * @param point ����������
	 * @return ������ ��������
	 */
	public Vector<AIGameObject> geGameObject(MapPoint point)
	{
		
		// ���� ����������� ���������� ��������� �� � ������� ��������� �������� ������� - �� ���������� null
		if (point.getLengthTo(map.getMapPoint(this.curObject)) > (Integer) curObject.getProperty("rangeOfVisibility")) return null;
		
		// ������ �������� ������� ����������� �� �����
		Vector<MapObject> objects = map.getMapObjects(map.getMapPoint(this.curObject));
		
		// ������ �������� ������� ����� �������� �� ������� (���������� ������������� �� MapObject -> AIGameObject)
		Vector<AIGameObject> aiObjects = new Vector<AIGameObject>();
		
		// ����� ������ �������� ������� ��������� GameObject
		for (MapObject object : objects) {
			if (object instanceof GameObject) {
				GameObject gameObject = (GameObject) object;
				aiObjects.add(new AIGameObject(gameObject));
			}
		}
		
		return aiObjects;
		
	}
	
}
