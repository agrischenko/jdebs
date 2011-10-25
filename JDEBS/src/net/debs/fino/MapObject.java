package net.debs.fino;

/**
 * Абстрактный объект на карте
 * @author AGrischenko
 *
 */
public abstract class MapObject {
	/**
	 * точка
	 */
	MapPoint pivotPoint;

	public MapPoint getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(MapPoint pivotPoint) {
		this.pivotPoint = pivotPoint;
	}
	
	
}
