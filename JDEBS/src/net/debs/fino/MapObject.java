package net.debs.fino;

/**
 * ����������� ������ �� �����
 * @author AGrischenko
 *
 */
public abstract class MapObject {
	/**
	 * �����
	 */
	private MapPoint pivotPoint;
	
	public MapPoint getMapPoint(){
		return pivotPoint;
	}
	

	public MapPoint getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(MapPoint pivotPoint) {
		this.pivotPoint = pivotPoint;
	}

}
