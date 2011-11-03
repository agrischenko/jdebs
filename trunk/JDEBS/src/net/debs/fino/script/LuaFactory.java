package net.debs.fino.script;

import net.debs.fino.MapPoint;

/**
 * Фабрика классов используемая в LUA скрипте для создания объектов
 * @author AAntonenko
 */
public class LuaFactory {
		
	/**
	 * Создание объекта MapPoint
	 * @return объект MapPoint
	 */
	public MapPoint createMapPoint(int x, int y){
		return new MapPoint(x, y);
	}
	
}
