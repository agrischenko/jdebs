package net.debs.fino.ai;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;

/**
 * Реализация класа описывающего союзного юнита
 * @author AAntonenko
 */
public class AIAlly extends AIGameObject {

	public AIAlly(GameObject object, DebsMap map) {
		super(object, map);
	}

	public Object getProperty(String key){
		return this.object.getAiProperty(key);
	}

}
