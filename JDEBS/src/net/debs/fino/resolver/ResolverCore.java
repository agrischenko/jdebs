package net.debs.fino.resolver;

import java.io.File;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;

/**
 * ���������� ���������� �������� ��� ����� �� �����
 * @author AAntonenko
 *
 */
public class ResolverCore {

	private static String pathToActionScript = ""; 
	
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
        
		if (!error.isEmpty()) System.err.println("������ ���������� �������� '" + action.getType() + "' ��� ������� � Id '" + object.getId() + "': " + error);
		
	}
	
}
