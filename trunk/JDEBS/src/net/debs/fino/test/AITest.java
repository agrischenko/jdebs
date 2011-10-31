package net.debs.fino.test;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.ai.AICore;

public class AITest {

	public static void main(String[] args) {
		new AITest().start();
	}
	
	private void start() {
	
		//Создание карты
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		//Создание объекта тестового юнита
		GameObject me = new GameObject();
		
		//Установка пути к файлу скрипта
		me.setProperty("script", "test/scripts/testscript.lua");
		
		//Установка дальности видимости у юнита
		me.setProperty("rangeOfVisibility", 5);
		
		//Добавление юнита на карту (в позицию 5,5)
		map.addObject(5, 5, me);
		
		//Получение действия которое хочет выполнить юнит (вычисляется на основании скрипта)
		Action action = AICore.getAction(me, map);
		
		//Вывод полученного действия на консоль
		if (action != null) {
			System.out.printf("Action.type = '%s'\n", action.getType());
			System.out.printf("Action.id = '%s'\n", action.getId());
			System.out.printf("Action.point = '%s'\n", action.getPoint());
		}
		else {
			System.out.printf("Action is null\n");
		}
	}
	
}
