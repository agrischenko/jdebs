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
	MapPoint pivotPoint;

	public MapPoint getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(MapPoint pivotPoint) {
		this.pivotPoint = pivotPoint;
	}
	
	
}
