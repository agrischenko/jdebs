package net.debs.fino.ai;

import net.debs.fino.GameObject;

/**
 * ���������� ����� �������� ����� (��� �������� ����������� ������)
 * @author AAntonenko
 */
public class AIMe extends AIAlly{

	public AIMe(GameObject object) {
		super(object);
	}

	public Object setProperty(String key, Object value){
		return this.object.setProperty(key, value);
	}
	
}
