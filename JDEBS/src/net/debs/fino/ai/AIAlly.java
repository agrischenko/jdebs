package net.debs.fino.ai;

import net.debs.fino.GameObject;

/**
 * ���������� ����� ������������ �������� �����
 * @author AAntonenko
 */
public class AIAlly extends AIGameObject {

	public AIAlly(GameObject object) {
		super(object);
	}

	public Object getProperty(String key){
		return this.object.getAiProperty(key);
	}

}
