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
	private LuaError error = null;
	private LuaRandom random = null;
	
	private static final String pathToFactoriesScript = "scripts/factories.lua";
	
	private static final String pathToCoreActionsScript = "scripts/coreactions.lua";
	
	private static final String pathToCoreAiScriptScript = "scripts/coreai.lua";
	
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
		
		error = new LuaError();
		
		random = new LuaRandom();
		
		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
		
		L.pushJavaObject(ScriptCore.getScriptCore().getLuaFactory());
		L.setGlobal("LuaFactory");
		
		if (L.LdoFile(pathToFactoriesScript) != 0) System.err.println("Ошибка подготовки скрипта: " + String.valueOf(L.error()));
		
		return L;
	}
	
	public LuaState getLuaStateForActions(){
		LuaState L = this.getLuaState();
		
		L.pushJavaObject(new LuaDnd());
		L.setGlobal("dnd");
		
		L.pushJavaObject(this.error);
		L.setGlobal("LuaError");
		
		if (pathToCoreActionsScript.length() != 0)
			if (L.LdoFile(pathToCoreActionsScript) != 0) System.err.println("Ошибка подготовки скрипта для выполнения действия :" + String.valueOf(L.error()));
		
		return L;
	}
	
	public LuaState getLuaStateForAiScripts(){
		LuaState L = this.getLuaState();
		
		L.pushJavaObject(this.random);
		L.setGlobal("LuaRandom");
		
		if (pathToCoreAiScriptScript.length() != 0)
			if (L.LdoFile(pathToCoreAiScriptScript) != 0) System.err.println("Ошибка подготовки скрипта для выполнения ai скрипта :" + String.valueOf(L.error()));
		
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
	
	public String getError(){
		String err = error.getError(); 
		if  (err == null) return "";
		return err;
	}
	
}
