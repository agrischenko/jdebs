package net.debs.fino.test;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.ai.AICore;

public class AITest {

	public static void main(String[] args) {
		new AITest().start();
	}
	
	private void start() {
	
		//�������� �����
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		//�������� ������� ��������� �����
		GameObject me = new GameObject();
		
		//��������� ���� � ����� �������
		me.setProperty("script", "test/scripts/testscript.lua");
		
		//��������� ��������� ��������� � �����
		me.setProperty("rangeOfVisibility", 5);
		
		//���������� ����� �� ����� (� ������� 5,5)
		map.addObject(5, 5, me);
		
		//��������� �������� ������� ����� ��������� ���� (����������� �� ��������� �������)
		Action action = AICore.getAction(me, map);
		
		//����� ����������� �������� �� �������
		if (action != null) {
			System.out.printf("Action.type = '%s'\n", action.getType());
			System.out.printf("Action.id = '%s'\n", action.getId());
			System.out.printf("Action.point = '%s'\n", action.getPoint());
		}
		else {
			System.out.printf("Action is null\n");
		}
	}
	
}
