package net.debs.fino.dnd;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;

/**
 * ���������� ����� ��� ����������� ���������� �� �������� DnD
 * @author AAntonenko
 */
public class MapDistance {

	/**
	 * ���������� ���������� ����� ����� �������
	 * @param map ����� �� ������� ����� ����������� ������ ����������
	 * @param p1 ����� �� ������� ����� ����������� ������ ����������
	 * @param p2 ����� �� ������� ����� ����������� ������ ����������
	 * @return ���������� ����� �������
	 */
	public static Integer distance(DebsMap map, MapPoint p1, MapPoint p2) {
		int dx = Math.abs(p1.getX() - p2.getX());
		int dy = Math.abs(p1.getY() - p2.getY());
		int minD = Math.min(dx, dy);
		int maxD = Math.max(dx, dy);
		return ((int) (1.5 * minD)) + maxD - minD;
	}
	
}
