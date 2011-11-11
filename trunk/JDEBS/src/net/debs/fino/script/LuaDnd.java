package net.debs.fino.script;

import net.debs.fino.DebsMap;
import net.debs.fino.MapPoint;
import net.debs.fino.Path;
import net.debs.fino.dnd.MapDistance;
import net.debs.fino.dnd.MapVisibility;
import net.debs.fino.dnd.MapPath;

public class LuaDnd {

	public Integer MapDistance(DebsMap map, MapPoint p1, MapPoint p2){
		return MapDistance.distance(map, p1, p2);
	}
	
	public Boolean MapVisible(DebsMap map, MapPoint p1, MapPoint p2){
		return MapVisibility.see(map, p1, p2);
	}
	
	public Path MapPath(DebsMap map, MapPoint p1, MapPoint p2){
		return MapPath.path(map, p1, p2);
	}
	
}
