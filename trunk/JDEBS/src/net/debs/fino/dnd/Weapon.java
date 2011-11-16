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
	 * ��������� �������� �� �������� � �������� ������������ �����
	 * @param value �������� ��� ��������
	 * @return true - ��������; false - �� ��������
	 */
	public boolean inCriticalRange(int value){
		String critivalRangeStr = (String) this.getProperty("criticalRange");
		
		// ���� ����������� �������� �� �����, �� ������� ��� ��� 20
		if (critivalRangeStr == null){
			System.err.println("Weapon \"" + this.getName() + "\" does not contains property \"CriticalRange\", using default CriticalRange (20)");
			return (value == 20);
		}
		if (critivalRangeStr.equals("")) return (value == 20);
		
		int index = critivalRangeStr.indexOf("-");
		if (index == -1){
			return (Integer.valueOf(critivalRangeStr) == value); 
		}
		else{
			int min = Integer.valueOf(critivalRangeStr.substring(0, index));
			int max = Integer.valueOf(critivalRangeStr.substring(index + 1, critivalRangeStr.length() - 1));
			return (value >= min && value <= max);
		}
	}
	
	public int getCriticalKoef(){
		String criticalKoefStr = (String) this.getProperty("CriticalKoef");
		
		// ���� ����������� ����������� �� �����, �� ������� ��� ��� 2
		if (criticalKoefStr == null){
			System.err.println("Weapon \"" + this.getName() + "\" does not contains property \"CriticalKoef\", using default CriticalKoef (2)");
			return 2;
		}
		if (criticalKoefStr.equals("")) return 2;
		
		return Integer.valueOf(criticalKoefStr);
	}
	
	/**
	 * ���������� ��������� ����� ������
	 * @return ��������� �����
	 */
	public int getRange(){
		String range = (String) this.getProperty("range");
		if (range == null){
			System.err.println("Weapon \"" + this.getName() + "\" does not contains property \"range\", using default range (1)");
			return 1;
		}
		return Integer.valueOf(range);
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
