package net.debs.fino.ai;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * Реализация пользовательского скрипта
 * используется LUA
 * @author AGrischenko
 *
 */
public class AICore {

	public Action getAction(GameObject gameObject, DebsMap map) {
		Action action = null;
		
		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
			    
		L.pushJavaObject(action);
		L.setGlobal("action");
		
		//L.pushJavaObject(map);
		//L.setGlobal("map");
		
		if (L.LdoFile(gameObject.getProperty("script").toString()) != 0) System.out.println(String.valueOf(L.error()));
		
		return action;
	}

}
