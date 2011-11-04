package net.debs.fino.resolver;

import java.io.File;

import org.keplerproject.luajava.LuaState;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.script.ScriptCore;

/**
 * Реализация выполнения действия для юнита на карте
 * @author AAntonenko
 *
 */
public class ResolverCore {

	private static String pathToActionScript = "scripts/actions"; 

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
		
		//Если действие не задано - то выходим
		if (action.getType() == null || action.getType().equalsIgnoreCase("")) return;
		
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
        LuaState L = ScriptCore.getScriptCore().getLuaStateForActions();
		
		L.pushJavaObject(object);
		L.setGlobal("object");
		
		L.pushJavaObject(action);
		L.setGlobal("action");
		
		L.pushJavaObject(map);
		L.setGlobal("map");
		
		if (L.LdoFile(pathScript) != 0) System.err.println(String.valueOf(L.error()));
        
		L.close();

		String error = ScriptCore.getScriptCore().getError();
		
		if (error.length()!=0) System.err.println("Ошибка выполнения действия '" + action.getType() + "' для объекта с Id '" + object.getId() + "': " + error);
		
	}
	
}
