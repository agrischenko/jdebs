package net.debs.fino.dnd;

import net.debs.fino.GameObject;

public class Char {

	/**
	 * Определение модификатора атрибута
	 * @param object игровой объект у которого необходимо определить модификатор
	 * @param attribute название атрибута (strength, dexterity, constitution, intelligence, wisdom, charisma)
	 * @return значение атрибута (если атрибута нет - возвращает 0)
	 */
	public static int getAttributeModifier(GameObject object, String attribute){
		Integer attr = (Integer) object.getProperty(attribute);
		if (attr != null) return (attr - 10) >> 1;
		return 0;
	}
	
}
