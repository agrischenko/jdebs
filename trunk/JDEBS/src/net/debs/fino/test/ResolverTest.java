package net.debs.fino.test;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.resolver.ResolverCore;

public class ResolverTest {

	public static void main(String[] args) {
		new ResolverTest().start();
	}
	
	private void start() {
		
		//�������� �����
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		//�������� ������� ��������� �����
		GameObject object = new GameObject();
		object.setProperty("speed", 5);
		
		//���������� ����� �� ����� (� ������� 5,5)
		map.addObject(5, 5, object);
		
		//�������� �������� "testaction"
		Action action = new Action();
		action.setType("move");
		action.setMapPoint(new MapPoint(1, 1));
		
		//������� ���� � ����� �� ��������� ��������
		ResolverCore.setPathToActionScript("scripts/actions");
		
		//���������� ��������
		ResolverCore.resolveAction(object, action, map);
		
		//�������� �������� �� �������� (�������� "testProperty" ��������������� � �������)
		if (object.getProperty("testProperty") != null) {
			System.out.printf("testProperty = '%s'\n", object.getProperty("testProperty").toString());
		}
		else {
			System.out.printf("testProperty is null\n");
		}
		
	}
	
}
