package net.debs.fino;

import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

/**
 * Реализация сложного объекта
 * @author AGrischenko
 *
 */
public abstract class ComplexMapObject extends MapObject {
	/**
	 * Объекты по локальным координатам
	 */
	Hashtable<MapPoint, MapObject> objects;

	public Vector<MapObject> getVisibleObjects(Rectangle viewPort) {
		Vector<MapObject> v = new Vector<MapObject>();
		if (objects==null) {
			MapPoint p = getMapPoint();
			if (viewPort.contains(p.getX(), p.getY()))
					v.add(this);
		} else {
			Iterator <Entry<MapPoint,MapObject>> it = objects.entrySet().iterator();
			while(it.hasNext()) {
				Entry<MapPoint,MapObject> entry = it.next();
				MapObject obj = entry.getValue();
				if (obj instanceof ComplexMapObject) {
					ComplexMapObject cobj = (ComplexMapObject) obj;
					v.addAll(cobj.getVisibleObjects(viewPort));
				} else {
					MapPoint p = entry.getKey();
					if (viewPort.contains(p.getX(), p.getY()))
							v.add(entry.getValue());
				}
			}
		}
		return v;
	}

	public Vector<MapObject> getAllObjects() {
		Vector<MapObject> v = new Vector<MapObject>();
		if (objects==null) {
			v.add(this);
		} else {
			Iterator <Entry<MapPoint,MapObject>> it = objects.entrySet().iterator();
			while(it.hasNext()) {
				Entry<MapPoint,MapObject> entry = it.next();
				MapObject obj = entry.getValue();
				if (obj instanceof ComplexMapObject) {
					ComplexMapObject cobj = (ComplexMapObject) obj;
					v.addAll(cobj.getAllObjects());
				} else {
					v.add(entry.getValue());
				}
			}
		}
		return v;
	}
}
