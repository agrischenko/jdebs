package net.debs.fino;

/**
 * –еализаци€ точки на двумерной карте 
 * @author AGrischenko
 *
 */
public class MapPoint {
	int x;
	int y;
	
	public static MapPoint createMapPoint(int x, int y){
		return new MapPoint(x, y);
	}
	
	public MapPoint(int x, int y) {
		this.x = x; this.y = y;
	}
	public MapPoint() {
		x = -1;
		y = -1;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * ѕолучить квадрат рассто€ни€ между этой и другой точкой
	 * @param p друга€ точка
	 * @return квадрат рассто€ни€
	 */
	public int getSqDistanceTo(MapPoint p) {
		int lx = (getX()-p.getX());
		int ly = (getY()-p.getY());
		return lx*lx + ly*ly;
	}
	
	/**
	 * ѕолучить рассто€ние между этой точкой и другой
	 * @param p друга€ точка
	 * @return рассто€ни€
	 */
	public int getDistanceTo(MapPoint p) {
		int lx = (getX()-p.getX());
		int ly = (getY()-p.getY());
		return (int) Math.sqrt(lx*lx + ly*ly);
	}

	@Override
	public String toString() {
		return String.valueOf(x) + ":" + String.valueOf(y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MapPoint) {
			MapPoint mp = (MapPoint) obj;
			return (this.getX() == mp.getX()) && (this.getY() == mp.getY());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return  x*y+y-x;
	}
	
	
	
}
