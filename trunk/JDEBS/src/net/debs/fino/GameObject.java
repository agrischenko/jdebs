package net.debs.fino;

import java.util.Hashtable;

import net.debs.fino.ai.AICore;

/**
 * ���������� �������� �������
 * @author AGrischenko
 *
 */
public class GameObject extends ComplexMapObject {

	public final static String UNDEFINED_ID = "<undefined>";
	
	private String id = UNDEFINED_ID;
	
	/**
	 * �� ����� � AI Script
	 */
	Hashtable<String, Object> properties = new Hashtable<String, Object>();
	/**
	 * ����� � AI Script
	 */
	Hashtable<String, Object> aiProperties = new Hashtable<String, Object>();

	/**
	 * �������� �������� ������� (������������ � �������)
	 * @return �������� �� �����. ���� �������� ����������� - ������ null
	 * @param key ���� ��������
	 */	
	public Object getAiProperty(String key) {
		return aiProperties.get(key);
	}
	
	/**
	 * ���������� �������� ������� (������������ � �������)
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 * @param key ���� ��������
	 * @param value ��������
	 */
	public Object setAiProperty(String key, Object value) {
		Object oldValue = getAiProperty(key);
		aiProperties.put(key, value);
		return oldValue;
	}

	/**
	 * �������� �������� �������
	 * @return �������� �� �����. ���� ����������� - ������ null
	 * @param key ���� ��������
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * ���������� �������� �������
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 */
	public Object setProperty(String key, Object value) {
		Object oldValue = getProperty(key);
		properties.put(key, value);
		return oldValue;
	}

	/**
	 * �������� ID �������
	 * @return ����� ������� � �������
	 */
	public String getId() {
		return id;
	}

	/**
	 * ��������� ID �������
	 * @param id ���������� ����� ������� � �������
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * ������ �������� �������
	 * ���������� ������� ������
	 * @return ������ ��������
	 * @param map ������� �����
	 * @see Action
	 */
	public Action getAction(DebsMap map) {
		return AICore.getAction(this, map);
	}
	
}
