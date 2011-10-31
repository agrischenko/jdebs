package net.debs.fino.res;

import java.awt.Color;

public class FactionsLoader extends TabbedColumnLoader {

	public static String COLOR = "color";
	
	public FactionsLoader(ResourceManager resourceManager) {
		super(resourceManager, ResourceManager.FACTION);
	}

	@Override
	protected Object parseColumnObject(String colName, String value) {
		if (colName.equalsIgnoreCase(COLOR)) {
			String srgb[] = value.split(" ");
			try {
				int r = Integer.parseInt(srgb[0]);
				int g = Integer.parseInt(srgb[1]);
				int b = Integer.parseInt(srgb[2]);
				return new Color(r, g, b);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Bad color definition in parse faction: "+value);
			}
		}
		return super.parseColumnObject(colName, value);
	}
}
