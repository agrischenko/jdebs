package net.debs.fino;

/**
 * –еализаци€ точки на двумерной карте 
 * @author AGrischenko
 *
 */
public class MapPoint {
	int x;
	int y;
	
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
	public int getSqLengthTo(MapPoint p) {
		int lx = (getX()-p.getX());
		int ly = (getY()-p.getY());
		return lx*lx + ly*ly;
	}
}
