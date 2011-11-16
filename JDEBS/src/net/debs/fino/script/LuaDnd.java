package net.debs.fino.script;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;
import net.debs.fino.Path;
import net.debs.fino.dnd.Char;
import net.debs.fino.dnd.D20;
import net.debs.fino.dnd.MapDistance;
import net.debs.fino.dnd.MapTactic;
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
	
	public Path MapPath(DebsMap map, MapPoint p1, MapObject object){
		return MapPath.path(map, p1, object);
	}
	
	public MapPoint NearestAttackMapPoint(DebsMap map, MapPoint objectPoint, MapPoint attackPoint, int speed, int distance){
		return MapTactic.getNearestAttackMapPoint(map, objectPoint, attackPoint, speed, distance);
	}
	
	public int d(int dice){
		return D20.d(dice);
	}
	
	public int AttributeModifier(GameObject object, String attribute){
		return Char.getAttributeModifier(object, attribute);
	}
		
}
