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
	private LuaError error = null;
	private LuaRandom random = null;
	
	private static final String pathToFactoriesScript = "scripts/factories.lua";
	
	private static final String pathToCoreActionsScript = "scripts/coreactions.lua";
	
	private static final String pathToCoreAiScriptScript = "scripts/coreai.lua";
	
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
		
		error = new LuaError();
		
		random = new LuaRandom();
		
		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
		
		L.pushJavaObject(ScriptCore.getScriptCore().getLuaFactory());
		L.setGlobal("LuaFactory");
		
		if (L.LdoFile(pathToFactoriesScript) != 0) System.err.println("������ ���������� �������: " + String.valueOf(L.error()));
		
		return L;
	}
	
	public LuaState getLuaStateForActions(){
		LuaState L = this.getLuaState();
		
		L.pushJavaObject(new LuaDnd());
		L.setGlobal("dnd");
		
		L.pushJavaObject(this.error);
		L.setGlobal("LuaError");
		
		if (pathToCoreActionsScript.length() != 0)
			if (L.LdoFile(pathToCoreActionsScript) != 0) System.err.println("������ ���������� ������� ��� ���������� �������� :" + String.valueOf(L.error()));
		
		return L;
	}
	
	public LuaState getLuaStateForAiScripts(){
		LuaState L = this.getLuaState();
		
		L.pushJavaObject(this.random);
		L.setGlobal("LuaRandom");
		
		if (pathToCoreAiScriptScript.length() != 0)
			if (L.LdoFile(pathToCoreAiScriptScript) != 0) System.err.println("������ ���������� ������� ��� ���������� ai ������� :" + String.valueOf(L.error()));
		
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
	
	public String getError(){
		String err = error.getError(); 
		if  (err == null) return "";
		return err;
	}
	
}
