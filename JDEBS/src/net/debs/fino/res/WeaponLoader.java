package net.debs.fino.res;

import net.debs.fino.dnd.Weapon;

public class WeaponLoader extends TabbedColumnLoader {

	private static final String nameColumnName = "name";
	
	private Weapon curWeapon;
	
	public WeaponLoader(ResourceManager resourceManager) {
		super(resourceManager, ResourceManager.WEAPON);
	}

	@Override
	protected void parseColumn(Integer rowNum, String colName, String value) {
		if (colName.equalsIgnoreCase(WeaponLoader.nameColumnName)){
			if (curWeapon != null) ResourceManager.putResource(domainName + "." + curWeapon.getName(), curWeapon);
			curWeapon = new Weapon(value);
		}
		else if (curWeapon != null) {
			curWeapon.setProperty(colName, value);
		}
	}

	@Override
	protected void parceEnd() {
		if (curWeapon != null) ResourceManager.putResource(domainName + "." + curWeapon.getName(), curWeapon);
	}

}
