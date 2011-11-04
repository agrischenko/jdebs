package net.debs.fino.resolver;

import java.io.File;

import org.keplerproject.luajava.LuaState;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.script.ScriptCore;

/**
 * ���������� ���������� �������� ��� ����� �� �����
 * @author AAntonenko
 *
 */
public class ResolverCore {

	private static String pathToActionScript = "scripts/actions"; 

	/**
	 * ������������� ���� � ����� �� ��������� ��������
	 * @param path ���� � ����� ��� ����� � �����
	 */
	public static void setPathToActionScript(String path){
		ResolverCore.pathToActionScript = path;
	}
	
	/**
	 * ��������� �������� �������� ������� �� �����
	 * @param object ������� ������ ��� �������� ����������� ��������
	 * @param action �������� ������� ���������� ���������
	 * @param map ����� �� ������� ����������� ��������
	 */
	public static void resolveAction(GameObject object, Action action, DebsMap map){
		
		//���� �������� �� ������ - �� �������
		if (action.getType() == null || action.getType().equalsIgnoreCase("")) return;
		
		//���� �� ����� ���� � ����� �� ��������� �������� - �� �������
		if (ResolverCore.pathToActionScript.equalsIgnoreCase("")) {
			System.err.println("�� ����� ���� � ����� �� ��������� ��������");
			return;
		}
		
		//�������� ���������� �� ���� ��������
		String pathScript = pathToActionScript + "/" + action.getType() + ".lua";
		File f=  new File(pathToActionScript);
        if (!f.exists()) {
        	System.err.println("������ ��� �������� '" + action.getType() + "' �� ���� '" + pathScript + "' �� ������");
			return;
        }
		
        //���������� ����� �������� (� ������� ��������: object - ������ ������� ��������� ��������; action - ����������� ��������; map - ����� �� ������� ����������� ��������)
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
		
		if (error.length()!=0) System.err.println("������ ���������� �������� '" + action.getType() + "' ��� ������� � Id '" + object.getId() + "': " + error);
		
	}
	
}
