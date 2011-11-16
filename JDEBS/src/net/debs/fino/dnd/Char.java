package net.debs.fino.dnd;

import net.debs.fino.GameObject;

public class Char {

	/**
	 * ����������� ������������ ��������
	 * @param object ������� ������ � �������� ���������� ���������� �����������
	 * @param attribute �������� �������� (strength, dexterity, constitution, intelligence, wisdom, charisma)
	 * @return �������� �������� (���� �������� ��� - ���������� 0)
	 */
	public static int getAttributeModifier(GameObject object, String attribute){
		Integer attr = (Integer) object.getProperty(attribute);
		if (attr != null) return (attr - 10) >> 1;
		return 0;
	}
	
}
