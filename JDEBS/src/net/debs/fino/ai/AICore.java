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

	/**
	 * Возвращает действие которое вычисляется скриптом
	 * @param gameObject игровой объект для которого
	 * @return вектор объектов
	 */
	public static Action getAction(GameObject gameObject, DebsMap map) {
		Action action = new Action();
		
		LuaState L = ScriptCore.getScriptCore().getLuaState();
			    
		L.pushJavaObject(action);
		L.setGlobal("action");
		
		L.pushJavaObject(new AIMap(map, gameObject));
		L.setGlobal("map");
		
		L.pushJavaObject(new AIMe(gameObject, map));
		L.setGlobal("me");
		
		if (L.LdoFile(gameObject.getProperty("script").toString()) != 0) System.out.println(String.valueOf(L.error()));
		
		L.close(); 
		
		return action;
	}

}
