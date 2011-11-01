package net.debs.fino.dnd;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;

/**
 * Реализация класа для определения расстояния по правилам DnD
 * @author AAntonenko
 */
public class MapDistance {

	/**
	 * Определяет расстояние между двумя точками
	 * @param map карта на которой будет происходить расчет расстояния
	 * @param p1 точка от которой будет происходить расчет расстояния
	 * @param p2 точка до которой будет происходить расчет расстояния
	 * @return расстояние между точками
	 */
	public static Integer distance(DebsMap map, MapPoint p1, MapPoint p2) {
		int dx = Math.abs(p1.getX() - p2.getX());
		int dy = Math.abs(p1.getY() - p2.getY());
		int minD = Math.min(dx, dy);
		int maxD = Math.max(dx, dy);
		return ((int) (1.5 * minD)) + maxD - minD;
	}
	
}
