package net.debs.fino;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Абстрактная карта
 * @author AGrischenko
 *
 */
public class DebsMap {

	/**
	 *  Размер карты по ширине в клетках
	 */
	protected Integer width = 256;
	/**
	 *  Размер карты по высоте в клетках
	 */
	protected Integer height = 256;
	/**
	 * Объекты, размещенные на карте
	 */
	Hashtable<MapPoint, MapObject> objects;

	Iterator<Entry <MapPoint, MapObject>> curIt;

	public DebsMap() {
		objects =  new Hashtable<MapPoint, MapObject>();
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

	public MapObject getNextObject() {
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
	}
	
	public Vector<MapObject> getMapObjects(MapPoint point){
		//TODO: Необходимо вернуть вектор объектов MapObject содержащихся в указанной точке 
		return null;
	}
	
	public Vector<GameObject> getGameObjects(MapPoint point){
		//TODO: Необходимо вернуть вектор объектов GameObject содержащихся в указанной точке 
		return null;
	}
	
	/**
	 * Перемещает объект в указанную позицию
	 * @param point позиция в которую необходимо переместить объект
	 * @param object объект который надо переместить
	 * @return предыдущая позиция объекта, если не удалось переместить возвращается null
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

	public Collection<MapObject> getAllMapObjects() {
		// TODO Auto-generated method stub
		return objects.values();
	}
	
}
