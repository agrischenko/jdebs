package net.debs.fino.dnd;

import java.util.Hashtable;

public class Weapon {

	private static final String damagePropertyName = "damage";
	
	private String name;
	private Hashtable<String, Object> properties = new Hashtable<String, Object>();
	
	public Weapon(String name){
		this.name = name;
	}
	
	/**
	 * ��������� ���� ������ �� ��������� ��� ������� ��� ������� �����
	 * @return ���� ��������� �������
	 */
	public Integer getDamage(){
		String damage = (String) this.getProperty(damagePropertyName);
		if (damage == null) {
			System.err.println("Weapon \"" + getName() + "\" does not contains property \"" + damagePropertyName + "\", damage is 0");
			return 0;
		}
		return D20.Exec(damage);
	}
	
	/**
	 * ��������� �������� ������
	 * @return �������� ������
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * �������� �������� ������
	 * @return �������� �� �����. ���� ����������� - ������ null
	 * @param key ���� ��������
	 */
	public Object getProperty(String key) {
		Object prop = getPropertyWithoutCheck(key);
		if (prop == null) System.err.println(String.format("Property \"%s\" does not exists in weapon \"%s\"", key, getName()));
		return prop;
	}

	private Object getPropertyWithoutCheck(String key) {
		return properties.get(key);
	}
	
	/**
	 * ���������� �������� ������
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 */
	public Object setProperty(String key, Object value) {
		Object oldValue = getPropertyWithoutCheck(key);
		properties.put(key, value);
		return oldValue;
	}

	@Override
	public String toString() {
		
		String res = name;
		String propertiesStr = "";
		
		for (String key: properties.keySet()) {
			if (!propertiesStr.isEmpty()) propertiesStr += ", ";
			propertiesStr += key + ": " + properties.get(key);
		}
		
		if (!propertiesStr.isEmpty()) res += " (" + propertiesStr + ")";
		
		return res;
	}
	
}
