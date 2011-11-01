package net.debs.fino.script;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * ���������� ����������� ������ ����������� �� �������� � ���������� ���������� ������ LUA (��������)
 * @author AAntonenko
 */
public class ScriptCore {
	
	private static ScriptCore script = null;
	
	private LuaFactory factory = null;
	
	private static final String pathToFactoriesScript = "scripts/factories.lua";
	
	/**
	 * ������� ��������� ������� ScriptCore
	 * @return ������ ScriptCore
	 */
	public static ScriptCore getScriptCore() {
		if (script == null) script = new ScriptCore();
		return script;
	}
	
	/**
	 * ����������� ������� ������ - ������ ���������� �������� �������� getScriptCore()
	 */
	private ScriptCore() {
		super();
	}
	
	/**
	 * ���������� ������� � ������ ������ LuaState ��� ����������� ���������� � ��� ��������
	 * @return ������ LuaState �������������� � ������
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
	 * ���������� ������ ������� ������� ����������� ��� ���������� �������
	 * @return ������� LuaFactory (������� ������� ����������� ��� �������� �������� � �������) 
	 */
	private LuaFactory getLuaFactory() {
		if (factory == null) factory = new LuaFactory();
		return factory;
	}
	
}
