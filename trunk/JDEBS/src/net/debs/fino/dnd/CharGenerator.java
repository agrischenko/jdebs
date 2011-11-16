package net.debs.fino.dnd;

import net.debs.fino.GameObject;

public class CharGenerator {

	public static final String TYPE_WARRIOR = "warrior";
	public static final String TYPE_ARCHER = "archer";
	
	public static int id = 0;
	
	/**
	 * Генерирует персонажа 3-го уровня по правилам DnD
	 * @param фракция персонажа
	 * @return сгенерированый персонаж (объект класса GameObject)
	 */
	public static GameObject generate(String faction, String color, String type){
		
		//Уровень генерируемого персонажа
		int lvl = 3;
		
		//Кубик жизней для персонажа (взят для война)
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
		
		//Набрасывание жизней персонажа по схеме: на первом уровне берется максимальное значение кубика + модификатор конституции, на каждом следующем прибавляется бросок кубика + модификатор конституции (но не меньше 1)
		int hp = hpDice + Char.getAttributeModifier(res, "constitution");
		for (int i = 1; i < lvl; i++) {
			int dophp = D20.d(hpDice) + Char.getAttributeModifier(res, "constitution");
			if (dophp < 1) hp += 1;
			hp += dophp;
		}
		res.setProperty("hp", hp);
		res.setProperty("maxhp", hp);
		
		//Определение AC персонажа
		int ac = 10;
		ac += Char.getAttributeModifier(res, "dexterity");
		ac += 4; //предполагается что на персонаже одета стандартная броня (как у война)
		res.setProperty("ac", ac);
		
		//Определение модификатора атаки персонажа
		int attack = 0;
		if (type.equals(TYPE_WARRIOR)){
			attack += Char.getAttributeModifier(res, "strength");
		}
		else if (type.equals(TYPE_ARCHER)){
			attack += Char.getAttributeModifier(res, "dexterity");
		}

		attack += 3; //прибавление стандартного прироста атаки по уровню для война 
		attack += 1; //предполагается что воин владеет оружием которое имеет
		res.setProperty("attack", attack);
		
		return res;
	}
	
	/**
	 * Генерирует атрибут по следующему правилу: выбрасывает 4 кубика d6 и отбрасывает меньший из них
	 * @return значение атрибута
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
