package net.debs.fino.res;

import java.awt.Color;
import java.awt.Point;

public class ModelsLoader extends TabbedColumnLoader {

	public static final String FILLPOINTS = "fillpoints";
	public static final String IMAGEPATH = "imagepath";

	public ModelsLoader(ResourceManager resourceManager) {
		super(resourceManager, ResourceManager.MODEL);
	}

	@Override
	protected Object parseColumnObject(String colName, String value) {
		
		if (colName.equalsIgnoreCase(FILLPOINTS)) {
			Point points[] = null;
			if (value.indexOf(";")>=0) {
				String sps[] = value.split(";");
				points = new Point[sps.length];
				for (int i=0; i<sps.length; i++)
					points[i] = getPoint(sps[i]);
			} else {
				points = new Point[1];
				points[0] = getPoint(value);
			}
			return points;
		}
		
		return super.parseColumnObject(colName, value);
	}

	private Point getPoint(String value) {
		String xy[] = value.split(",");
		Point v = null;
		try {
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			v = new Point(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Bad color definition in parse faction: "+value);
		}
		return v;
	}
}
