package net.debs.fino.ai;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;

/**
 * Реализация класа текущего юнита (для которого выполняется скрипт)
 * @author AAntonenko
 */
public class AIMe extends AIAlly{

	public AIMe(GameObject object, DebsMap map) {
		super(object, map);
	}

	public Object setProperty(String key, Object value){
		return this.object.setProperty(key, value);
	}
	
}
