package net.debs.fino.ai;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.script.ScriptCore;

import org.keplerproject.luajava.LuaState;

/**
 * Реализация пользовательского скрипта, используется LUA
 * @author AAntonenko
 *
 */
public class AICore {

	public static DebsMap gMap = null;
	public static GameObject gObject = null;
	
	public static AIMap gAiMap = null;
	public static AIMe gAiMe = null;
	
	/**
	 * Возвращает действие которое вычисляется скриптом
	 * @param gameObject игровой объект для которого
	 * @return вектор объектов
	 */
	public static Action getAction(GameObject object, DebsMap map) {
		
		if (object.getProperty("script") == null) return null;
		
		Action action = new Action();

		LuaState L = ScriptCore.getScriptCore().getLuaStateForAiScripts();

		gMap = map;
		gObject = object;
		
		gAiMap = new AIMap(gMap, gObject);
		gAiMe = new AIMe(gObject);
		
		L.pushJavaObject(action);
		L.setGlobal("action");
		
		L.pushJavaObject(gAiMap);
		L.setGlobal("map");
		
		L.pushJavaObject(gAiMe);
		L.setGlobal("me");
		
		//System.out.println("getting action for object '" + object.getId() + "'");
		if (L.LdoFile(object.getProperty("script").toString()) != 0) System.out.println(String.valueOf(L.error()));
		
		L.close(); 
		
		return action;
	}

}
