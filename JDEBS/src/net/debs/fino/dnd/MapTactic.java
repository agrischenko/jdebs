package net.debs.fino.dnd;

import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;
import net.debs.fino.Path;

/**
 * ����� ����������� ��������� ������ � ������� ����� ���������
 * @author AAntonenko
 */
public class MapTactic {

	//���� ������
	public static void main(String [] args){
			
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);
		
		MapPoint res = getNearestAttackMapPoint(map, new MapPoint(6, 95), new MapPoint(5, 95), 6, 1);
		
		System.out.println(res);
		
	}
	
	/**
	 * ���������� ��������� ������ ��� ������ <code>objectPoint</code> ���� �� ������� �� ������ ��� <code>speed</code>, � ������� ����� ������ ������ <code>attackPoint</code> � ���������� �� ��� �� ������ <code>distance</code> 
	 * @param map ����� �� ������� ���� ������
	 * @param objectPoint ������ �� ������� ������� ����������� ������
	 * @param attackPoint ������� ������
	 * @param speed ������������ ���������� �� �������� ������
	 * @param distance ���������� �� ������ ����
	 * @return ������� ������
	 */
	public static MapPoint getNearestAttackMapPoint(DebsMap map, MapPoint objectPoint, MapPoint attackPoint, int speed, int distance){
		
		MapPoint res = null;
		int resDistance = -1;
		
		// ���� ������� ������� �������� ������� (������� ���������� � ������� ������ �����)
		if (MapDistance.distance(map, objectPoint, attackPoint) <= distance){
			if (MapVisibility.see(map, objectPoint, attackPoint)) return new MapPoint(objectPoint.getX(), objectPoint.getY());
		}
		// ���� ���������� ������ �������
		else if ((MapDistance.distance(map, objectPoint, attackPoint) - 1) > (speed + distance)) return null;
		
		for (int radius = 1; radius <= speed; radius++) {
			//������ ����������� � ������� radius �� ���������
			Vector<MapPoint> points = MapMask.getMapPointsByDistance(map, objectPoint, radius);
			
			for (MapPoint point : points) {
				if (MapDistance.distance(map, point, attackPoint) <= distance) {
					Path path = MapPath.path(map, objectPoint, point);
					if (path != null){
						int dist = path.distance();
						if (resDistance < dist) {
							res = point;
							resDistance = dist;
						}
					}
				}
			}
		}
		
		return res;
	}
	
}
