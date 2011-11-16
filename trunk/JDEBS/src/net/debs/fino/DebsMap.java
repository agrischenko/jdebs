package net.debs.fino;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * ����������� �����
 * @author AGrischenko
 *
 */
public class DebsMap {

	/**
	 *  ������ ����� �� ������ � �������
	 */
	protected Integer width = 25;
	/**
	 *  ������ ����� �� ������ � �������
	 */
	protected Integer height = 25;
	/**
	 * �������, ����������� �� �����
	 */
	Hashtable<MapPoint, MapObject> objects;
	
	Hashtable<String, MapObject> objectsById;

	Iterator<Entry <MapPoint, MapObject>> curIt;

	public DebsMap() {
		objects =  new Hashtable<MapPoint, MapObject>();
		objectsById = new Hashtable<String, MapObject>();
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public MapObject getFirstObject() {
		if (objects==null)
			return null;
		curIt = objects.entrySet().iterator();
		return curIt.next().getValue();
	}

	public synchronized MapObject getNextObject() {
		if (curIt==null || objects==null)
			return null;
		if (curIt.hasNext()) {
			Entry<MapPoint, MapObject> e = curIt.next();
			if (e==null)
				return null;
			return e.getValue();
		}

		return null;
	}

	public boolean hasNextObject() {
		if (curIt==null || objects==null)
			return false;
		return curIt.hasNext();
	}

	public void addObject(int x, int y, MapObject sol) {
		sol.setPivotPoint(new MapPoint(x, y));
		objects.put(sol.getPivotPoint(), sol);
		if (sol instanceof GameObject) {
			GameObject go = (GameObject) sol;
			objectsById.put(go.getId(), go);
		}
	}
		
	public MapObject getMapObject(MapPoint point){
		return this.objects.get(point);
	}
	
	public MapObject getMapObject(String id){
		//TODO ����������� ��������� ������� �� id
		return null;
	}
	
	public GameObject getGameObject(MapPoint point){
		MapObject objectInPoint = this.objects.get(point);
		if (objectInPoint instanceof GameObject) {
			GameObject go = (GameObject) objectInPoint;
			return go;
		}
		return null;
	}
	
	/**
	 * ���������� ������ �� ����� �� ��� id
	 * @param id ������������� �������
	 * @return �������� ������ (GameObject) ��� null ���� ������� � ����� id ��� �� �����
	 */
	public GameObject getGameObject(String id){
		MapObject object = objectsById.get(id);
		if (object == null) return null;
		if (object instanceof GameObject) {
			return (GameObject) object;
		}
		return null;
	}
	
	/**
	 * ���������� ������ � ��������� �������
	 * @param point ������� � ������� ���������� ����������� ������
	 * @param object ������ ������� ���� �����������
	 * @return ���������� ������� �������, ���� �� ������� ����������� ������������ null
	 */
	public MapPoint moveObject(MapPoint point, MapObject object) {
		MapPoint prevMapPoint = object.getMapPoint();
		if (!(objects.containsValue(object))) return null;
		MapObject test = objects.get(prevMapPoint);
		if (test==null) 
			return null;
		objects.remove(prevMapPoint);
		objects.put(point, object);
		object.setPivotPoint(point);
		return prevMapPoint;
	}
	
	/**
	 * ������� ������ � �����
	 * @param object ��������� ������
	 */
	public void removeObject(MapObject object){
		MapPoint point = object.getMapPoint();
		objects.remove(point);
		if ( object instanceof GameObject) {
			GameObject go = (GameObject) object;
			objectsById.remove(go.getId());
		}
	}

	public Collection<MapObject> getAllMapObjects() {
		// TODO Auto-generated method stub
		return objects.values();
	}
	
	/**
	 * ���������� ����� �� ��������� ����� ������ �����
	 * @param point ������� ��� ��������
	 * @return ture - ������ ����������; false - �� ����������
	 */
	public Boolean passable(MapPoint point){
		if (point.getX() < 0 | point.getX() >= width | point.getY() < 0 | point.getY() >= height) return false;
		GameObject go = getGameObject(point);
		if (go == null) return true; 
		Boolean passable = (Boolean) go.getProperty("passable");
		if (passable == null) return true; 
		return passable;
	}
	
	/**
	 * ���������� ����� �� ������ ����� ������ �����
	 * @param point ������� ��� ��������
	 * @return ture - ����� ������ ����� ������; false - ������ ����������� ���������
	 */
	public Boolean seeable(MapPoint point){
		GameObject go = getGameObject(point);
		if (go == null) return true; 
		Boolean seeable = (Boolean) go.getProperty("seeable");
		if (seeable == null) return true; 
		return seeable;
	}
	
}
