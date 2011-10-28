package net.debs.fino.resolver;

import java.io.File;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;

/**
 * Реализация выполнения действия для юнита на карте
 * @author AAntonenko
 *
 */
public class ResolverCore {

	private static String pathToActionScript = ""; 
	
	/**
	 * Устанавливает путь к папке со скриптами действий
	 * @param path путь к папке без слеша в конце
	 */
	public static void setPathToActionScript(String path){
		ResolverCore.pathToActionScript = path;
	}
	
	/**
	 * Выполняет действие игрового объекта на карте
	 * @param object игровой объект для которого выполняется действие
	 * @param action действие которое необходимо выполнить
	 * @param map карта на которой выполняется действие
	 */
	public static void resolveAction(GameObject object, Action action, DebsMap map){
		
		//Если не задан путь к папке со скриптами дейсвтий - то выходим
		if (ResolverCore.pathToActionScript.equalsIgnoreCase("")) {
			System.err.println("Не задан путь к папке со скриптами дейсвтий");
			return;
		}
		
		//Проверка существует ли файл действия
		String pathScript = pathToActionScript + "/" + action.getType() + ".lua";
		File f=  new File(pathToActionScript);
        if (!f.exists()) {
        	System.err.println("Скрипт для действия '" + action.getType() + "' по пути '" + pathScript + "' не найден");
			return;
        }
		
        //Выполнение файла действия (В скрипте доступны: object - объект который выполняет дейсвтие; action - выполняемое действие; map - карта на которой выполняется дейсвтие)
        LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
		
		L.pushJavaObject(object);
		L.setGlobal("object");
		
		L.pushJavaObject(action);
		L.setGlobal("action");
		
		L.pushJavaObject(map);
		L.setGlobal("map");
		
		String error = "";
		L.pushJavaObject(error);
		L.setGlobal("error");
		
		if (L.LdoFile(pathScript) != 0) System.err.println(String.valueOf(L.error()));
        
		if (!error.isEmpty()) System.err.println("Ошибка выполнения дайствия '" + action.getType() + "' для объекта с Id '" + object.getId() + "': " + error);
		
	}
	
}
