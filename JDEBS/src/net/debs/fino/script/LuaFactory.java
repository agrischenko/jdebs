package net.debs.fino.script;

import net.debs.fino.MapPoint;

/**
 * ������� ������� ������������ � LUA ������� ��� �������� ��������
 * @author AAntonenko
 */
public class LuaFactory {
		
	/**
	 * �������� ������� MapPoint
	 * @return ������ MapPoint
	 */
	public MapPoint createMapPoint(int x, int y){
		return new MapPoint(x, y);
	}
	
}
