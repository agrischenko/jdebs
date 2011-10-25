package net.debs.fino;

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
	protected Integer width = 256;
	/**
	 *  ������ ����� �� ������ � �������
	 */
	protected Integer height = 256;
	/**
	 * �������, ����������� �� �����
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

	public void addObject(int x, int y, GameObject sol) {
		sol.setPivotPoint(new MapPoint(x, y));
		objects.put(sol.getPivotPoint(), sol);
	}
}
