package net.debs.fino.dnd;

import net.debs.fino.GameObject;

public class CharGenerator {

	public static final String TYPE_WARRIOR = "warrior";
	public static final String TYPE_ARCHER = "archer";
	
	public static int id = 0;
	
	/**
	 * ���������� ��������� 3-�� ������ �� �������� DnD
	 * @param ������� ���������
	 * @return �������������� �������� (������ ������ GameObject)
	 */
	public static GameObject generate(String faction, String color, String type){
		
		//������� ������������� ���������
		int lvl = 3;
		
		//����� ������ ��� ��������� (���� ��� �����)
		int hpDice = 10;
		
		GameObject res = null;
		
		if (type.equals(TYPE_WARRIOR)){
			res = new GameObject("warrior", color);
			res.setProperty("weapon", "sword");
		}
		else if (type.equals(TYPE_ARCHER)){
			res = new GameObject("archer", color);
			res.setProperty("weapon", "bow");
		}
		
		res.setId(String.valueOf(id++));
		
		res.setProperty("speed", 6);
		res.setProperty("faction", faction);
		res.setProperty("rangeOfVisibility", 18);
		res.setProperty("seeable", true);
		res.setProperty("passable", false);
		
		res.setProperty("strength", generateAttrib());
		res.setProperty("dexterity", generateAttrib());
		res.setProperty("constitution", generateAttrib());
		res.setProperty("intelligence", generateAttrib());
		res.setProperty("wisdom", generateAttrib());
		res.setProperty("charisma", generateAttrib());
		
		//������������ ������ ��������� �� �����: �� ������ ������ ������� ������������ �������� ������ + ����������� �����������, �� ������ ��������� ������������ ������ ������ + ����������� ����������� (�� �� ������ 1)
		int hp = hpDice + Char.getAttributeModifier(res, "constitution");
		for (int i = 1; i < lvl; i++) {
			int dophp = D20.d(hpDice) + Char.getAttributeModifier(res, "constitution");
			if (dophp < 1) hp += 1;
			hp += dophp;
		}
		res.setProperty("hp", hp);
		res.setProperty("maxhp", hp);
		
		//����������� AC ���������
		int ac = 10;
		ac += Char.getAttributeModifier(res, "dexterity");
		ac += 4; //�������������� ��� �� ��������� ����� ����������� ����� (��� � �����)
		res.setProperty("ac", ac);
		
		//����������� ������������ ����� ���������
		int attack = 0;
		if (type.equals(TYPE_WARRIOR)){
			attack += Char.getAttributeModifier(res, "strength");
		}
		else if (type.equals(TYPE_ARCHER)){
			attack += Char.getAttributeModifier(res, "dexterity");
		}

		attack += 3; //����������� ������������ �������� ����� �� ������ ��� ����� 
		attack += 1; //�������������� ��� ���� ������� ������� ������� �����
		res.setProperty("attack", attack);
		
		return res;
	}
	
	/**
	 * ���������� ������� �� ���������� �������: ����������� 4 ������ d6 � ����������� ������� �� ���
	 * @return �������� ��������
	 */
	private static int generateAttrib(){
		int d1 = D20.d(6);
		int d2 = D20.d(6);
		int d3 = D20.d(6);
		int d4 = D20.d(6);
		int min = d1;
		if (d2 < min) min = d2;
		if (d3 < min) min = d3;
		if (d4 < min) min = d4;
		return d1 + d2 + d3 + d4 - min;
	}
	
}
