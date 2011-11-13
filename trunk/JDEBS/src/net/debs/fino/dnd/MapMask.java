package net.debs.fino.dnd;

import java.util.Hashtable;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;

/**
 *  ласс дл€ получени€ масок накладываемых на карту дл€ быстрого поиска объектов наход€щихс€ в радиусе или на дистанции
 * @author AAntonenko
  */
public class MapMask {

	private static Hashtable<Integer, boolean[][]> radiusMaskCache = new Hashtable<Integer, boolean[][]>();
	private static Hashtable<Integer, boolean[][]> distanceMaskCache = new Hashtable<Integer, boolean[][]>();
	
	//“ест масок
	public static void main(String [] args){
		
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);
		
		Vector<MapPoint> res = getMapPointsByDistance(map, new MapPoint(2, 2), 2);
		
		for (MapPoint point : res) {
			System.out.println(point);
		}
		
	}
	
	/**
	 * ¬озвращает маску(битовый массив) по указанному радиусу
	 * @param radius радиус
	 * @return битовый массив размером 2 * radius + 1
	 */
	public static boolean[][] getRadiusMask(int radius){
		if (radiusMaskCache.get(radius) == null) {
			int len = (radius << 1) + 1;
			MapPoint centerMapPoint = new MapPoint(radius, radius);
			MapPoint iMapPoint;
			boolean[][] mask = new boolean[len][len];
			for (int i = 0; i < len; i++) {
				for (int j = 0; j < len; j++) {
					iMapPoint = new MapPoint(i ,j);
					mask[i][j] = MapDistance.distance(null, centerMapPoint, iMapPoint) <= radius;
				}
			}
			radiusMaskCache.put(radius, mask);
			return mask;
		}
		return radiusMaskCache.get(radius);
	}
	
	/**
	 * ¬озвращает маску(битовый массив) по указанному дистанции
	 * @param distance радиус
	 * @return битовый массив размером 2 * distance + 1
	 */
	public static boolean[][] getDistanceMask(int distance){
		if (distanceMaskCache.get(distance) == null) {
			int len = (distance << 1) + 1;
			MapPoint centerMapPoint = new MapPoint(distance, distance);
			MapPoint iMapPoint;
			boolean[][] mask = new boolean[len][len];
			for (int i = 0; i < len; i++) {
				for (int j = 0; j < len; j++) {
					iMapPoint = new MapPoint(i ,j);
					mask[i][j] = MapDistance.distance(null, centerMapPoint, iMapPoint) == distance;
				}
			}
			distanceMaskCache.put(distance, mask);
			return mask;
		}
		return distanceMaskCache.get(distance);
	}

	/**
	 * ¬озвращает вектор клеток рассположенных в указанном радиусе от указанной клетки
	 * @param map карта (необходима дл€ определени€ границ)
	 * @param point клетка от которой будет отсчитыватс€ радиус
	 * @param radius радиус
	 * @return вектор клеток вход€щих в радиус
	 */
	public static Vector<MapPoint> getMapPointsByRadius(DebsMap map, MapPoint point, int radius){
		
		Vector<MapPoint> res = new Vector<MapPoint>();
		
		boolean[][] mask = MapMask.getRadiusMask(radius);
		
		int x1 = point.getX() - radius;
		int dx = 0;
		if (x1 < 0) {dx = -x1; x1 = 0;}
		int x2 = point.getX() + radius;
		if (x2 >= map.getWidth()) x2 = map.getWidth() - 1;
		int y1 = point.getY() - radius;
		int dy = 0;
		if (y1 < 0) {dy = -y1; y1 = 0;}
		int y2 = point.getY() + radius;
		if (y2 >= map.getHeight()) y2 = map.getHeight() - 1;
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				if (mask[x+dx][y+dy]) res.add(new MapPoint(x, y));
			}
		}
		
		return res;
		
	}
	
	/**
	 * ¬озвращает вектор клеток рассположенных на указанном рассто€нии от указанной клетки
	 * @param map карта (необходима дл€ определени€ границ)
	 * @param point клетка от которой будет отсчитыватс€ рассто€ние
	 * @param distance рассто€ние
	 * @return вектор клеток расположенных на дистанции
	 */
	public static Vector<MapPoint> getMapPointsByDistance(DebsMap map, MapPoint point, int distance){
		
		Vector<MapPoint> res = new Vector<MapPoint>();
		
		boolean[][] mask = MapMask.getDistanceMask(distance);
		
		int x1 = point.getX() - distance;
		int dx = 0;
		if (x1 < 0) {dx = -x1; x1 = 0;}
		int x2 = point.getX() + distance;
		if (x2 >= map.getWidth()) x2 = map.getWidth() - 1;
		int y1 = point.getY() - distance;
		int dy = 0;
		if (y1 < 0) {dy = -y1; y1 = 0;}
		int y2 = point.getY() + distance;
		if (y2 >= map.getHeight()) y2 = map.getHeight() - 1;
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				if (mask[x+dx][y+dy]) res.add(new MapPoint(x, y));
			}
		}
		
		return res;
		
	}
	
}
