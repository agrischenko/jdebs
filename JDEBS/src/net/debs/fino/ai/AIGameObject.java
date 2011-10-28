package net.debs.fino.ai;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;

/**
 * Реализация игрового объекта доступного в скрипте
 * @author AAntonenko
 *
 */
public class AIGameObject {

	protected GameObject object;
	protected DebsMap map;
	
	public AIGameObject(GameObject object, DebsMap map) {
		this.object = object;
		this.map = map;
	}
	
	public String getId() {
		return object.getId();
	}

	public MapPoint getMapPoint(){
		return object.getMapPoint();
	}
	
	public Integer getDistanceTo(GameObject object){
		return this.object.getMapPoint().getDistanceTo(object.getMapPoint());
	}
	
	public Integer getDistanceTo(MapPoint point){
		return this.object.getMapPoint().getDistanceTo(point);
	}
	
}
