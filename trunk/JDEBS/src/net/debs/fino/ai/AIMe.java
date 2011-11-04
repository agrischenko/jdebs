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
		System.out.println("seting "+ key + "=" + value);
		return this.object.setAiProperty(key, value);
	}
	
}
