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
	 * ¬ычисл€ет урон оружи€ на основании его формулы дл€ расчета урона
	 * @return урон наносимый оружием
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
	 * ѕровер€ет попадает ли значение в диапозон критического удара
	 * @param value значение дл€ проверки
	 * @return true - попадает; false - не попадает
	 */
	public boolean inCriticalRange(int value){
		String critivalRangeStr = (String) this.getProperty("criticalRange");
		
		// ≈сли критический диапозон не задан, то считаем что это 20
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
		
		// ≈сли критический коэффициент не задан, то считаем что это 2
		if (criticalKoefStr == null){
			System.err.println("Weapon \"" + this.getName() + "\" does not contains property \"CriticalKoef\", using default CriticalKoef (2)");
			return 2;
		}
		if (criticalKoefStr.equals("")) return 2;
		
		return Integer.valueOf(criticalKoefStr);
	}
	
	/**
	 * ¬озвращает дальность атаки оружи€
	 * @return дальность атаки
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
	 * ѕолучение название оружи€
	 * @return название оружий
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * ѕолучить свойство оружи€
	 * @return значение по ключу. ≈сли отсутствует - значит null
	 * @param key ключ свойства
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
	 * ”становить свойство оружи€
	 * @return старое значение по этому ключу. ≈сли значение отсутствует - значит null
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
