package net.debs.fino.dnd;

import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;
import net.debs.fino.Path;

/**
 * Класс определения ближайшей клетки с которой можно атаковать
 * @author AAntonenko
 */
public class MapTactic {

	//Тест поиска
	public static void main(String [] args){
			
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);
		
		MapPoint res = getNearestAttackMapPoint(map, new MapPoint(6, 95), new MapPoint(5, 95), 6, 1);
		
		System.out.println(res);
		
	}
	
	/**
	 * Возвращает ближайшую клетку для клетки <code>objectPoint</code> путь до которой не длинее чем <code>speed</code>, с которой можно видеть клетку <code>attackPoint</code> и расстояние до нее не больше <code>distance</code> 
	 * @param map карта на которой будт расчет
	 * @param objectPoint клетка от которой исчется необходимая клетка
	 * @param attackPoint целевая клетка
	 * @param speed максимальное расстояние от исходной клетки
	 * @param distance расстояние до клетки цели
	 * @return искомая клетка
	 */
	public static MapPoint getNearestAttackMapPoint(DebsMap map, MapPoint objectPoint, MapPoint attackPoint, int speed, int distance){
		
		MapPoint res = null;
		int resDistance = -1;
		
		// Если искомой клеткой является текущая (хватает расстояние и целевая клетка видна)
		if (MapDistance.distance(map, objectPoint, attackPoint) <= distance){
			if (MapVisibility.see(map, objectPoint, attackPoint)) return new MapPoint(objectPoint.getX(), objectPoint.getY());
		}
		// Если расстояние сишком большое
		else if ((MapDistance.distance(map, objectPoint, attackPoint) - 1) > (speed + distance)) return null;
		
		for (int radius = 1; radius <= speed; radius++) {
			//Клетки находящиеся в радиусе radius от начальной
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
