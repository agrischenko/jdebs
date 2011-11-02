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
