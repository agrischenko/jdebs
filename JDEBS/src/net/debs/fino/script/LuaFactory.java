package net.debs.fino.script;

import net.debs.fino.MapPoint;

public class LuaFactory {
	
	public MapPoint createMapPoint(int x, int y){
		return new MapPoint(x, y);
	}
	
}
