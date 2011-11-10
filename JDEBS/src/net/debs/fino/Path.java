package net.debs.fino;

import java.util.Vector;

public class Path {
	
	private Vector<MapPoint> points = null;

	public Path(){
		this.points = new Vector<MapPoint>();
	}
	
	public void add(MapPoint point){
		points.add(point);
	}
	
	public MapPoint get(Integer i){
		return points.get(i);
	}
	
	public Integer size(){
		return points.size();
	}
	
}
