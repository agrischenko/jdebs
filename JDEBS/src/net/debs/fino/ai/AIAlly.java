package net.debs.fino.ai;

import net.debs.fino.GameObject;

/**
 * Реализация класа описывающего союзного юнита
 * @author AAntonenko
 */
public class AIAlly extends AIGameObject {

	public AIAlly(GameObject object) {
		super(object);
	}

	public Object getProperty(String key){
		System.out.println("geting " + key + "=" + this.object.getAiProperty(key));
		return this.object.getAiProperty(key);
	}

}
