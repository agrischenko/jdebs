package net.debs.fino.script;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * Реализация глобального класса отвечающего за создание и подготовку скриптовго движка LUA (синглтон)
 * @author AAntonenko
 */
public class ScriptCore {
	
	private static ScriptCore script = null;
	
	private LuaFactory factory = null;
	
	private static final String pathToFactoriesScript = "scripts/factories.lua";
	
	/**
	 * Функция получения объекта ScriptCore
	 * @return объект ScriptCore
	 */
	public static ScriptCore getScriptCore() {
		if (script == null) script = new ScriptCore();
		return script;
	}
	
	/**
	 * Конструктор объекта закрыт - объект необходимо получать функцией getScriptCore()
	 */
	private ScriptCore() {
		super();
	}
	
	/**
	 * Возвращает готовый к работе объект LuaState для поледующего выполнения в нем скриптов
	 * @return объект LuaState подготовленный к работе
	 */
	public LuaState getLuaState() {
		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
		L.pushJavaObject(ScriptCore.getScriptCore().getLuaFactory());
		L.setGlobal("LuaFactory");
		if (L.LdoFile(pathToFactoriesScript) != 0) System.err.println(String.valueOf(L.error()));
		return L;
	}
	
	/**
	 * Возвращает объект фабрики классов необходимый для подготовки скрипта
	 * @return объекта LuaFactory (фабрика классов использемая для создания объектов в скрипте) 
	 */
	private LuaFactory getLuaFactory() {
		if (factory == null) factory = new LuaFactory();
		return factory;
	}
	
}
