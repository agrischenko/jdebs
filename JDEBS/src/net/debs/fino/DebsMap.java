package net.debs.fino;

import java.util.Collection;
import java.util.Hashtable;
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
	protected Integer width = 25;
	/**
	 *  Размер карты по высоте в клетках
	 */
	protected Integer height = 25;
	/**
	 * Объекты, размещенные на карте
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
		//TODO реализовать получение объекта по id
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
	 * Возвращает объект на карте по его id
	 * @param id идентификатор объекта
	 * @return найденый объект (GameObject) или null елси объекта с таким id нет на карте
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
	
	/**
	 * Удаляет объект с карты
	 * @param object удаляемый объект
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
	 * Определяет можно ли проходить через клетку карты
	 * @param point позиция для проверки
	 * @return ture - клетка проходимая; false - не проходимая
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
	 * Определяет можно ли видеть через клетку карты
	 * @param point позиция для проверки
	 * @return ture - через клетку можно видеть; false - клетка преграждает видимость
	 */
	public Boolean seeable(MapPoint point){
		GameObject go = getGameObject(point);
		if (go == null) return true; 
		Boolean seeable = (Boolean) go.getProperty("seeable");
		if (seeable == null) return true; 
		return seeable;
	}
	
}
