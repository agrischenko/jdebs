package net.debs.fino.test;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.resolver.ResolverCore;

public class ResolverTest {

	public static void main(String[] args) {
		new ResolverTest().start();
	}
	
	private void start() {
		
		//Создание карты
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		//Создание объекта тестового юнита
		GameObject object = new GameObject();
						
		//Добавление юнита на карту (в позицию 5,5)
		map.addObject(5, 5, object);
		
		//Создание действия "testaction"
		Action action = new Action();
		action.setType("testaction");
		
		//Задание пути к папке со скриптами действий
		ResolverCore.setPathToActionScript("test/scripts");
		
		//Выполнение действия
		ResolverCore.resolveAction(object, action, map);
		
		//Проверка выолнено ли действие (свойство "testProperty" устанавливается в скрипте)
		if (object.getProperty("testProperty") != null) {
			System.out.printf("testProperty = '%s'\n", object.getProperty("testProperty").toString());
		}
		else {
			System.out.printf("testProperty is null\n");
		}
		
	}
	
}
