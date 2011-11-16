package net.debs.fino;

public class Action {
	
	public static final String Move = "move";
	public static final String Attack = "attack";
	
	protected String type;
	protected String id;
	protected MapPoint point;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MapPoint getMapPoint() {
		return point;
	}
	public void setMapPoint(MapPoint point) {
		this.point = point;
	}
	@Override
	public String toString() {
		return type + " (id = " + id + " point = " + point + ")";
	}

	
}
