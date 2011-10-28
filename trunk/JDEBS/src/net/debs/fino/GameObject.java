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
	 * ����� � AI Script
	 */
	Hashtable<String, Object> properties = new Hashtable<String, Object>();
	/**
	 * �� ����� � AI Script
	 */
	Hashtable<String, Object> systemProperties = new Hashtable<String, Object>();

	/**
	 * �������� ��������� �������� �������
	 * @return �������� �� �����. ���� ����������� - ������ null
	 * @param key ���� ��������
	 */
	public Object getSystemProperty(String key) {
		return systemProperties.get(key);
	}
	/**
	 * ���������� ��������� �������� �������
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 */
	public Object setSystemProperty(String key, Object value) {
		Object oldValue = getSystemProperty(key);
		systemProperties.put(key, value);
		return oldValue;
	}

	/**
	 * �������� ����������� �������� �������
	 * @return �������� �� �����. ���� �������� ����������� - ������ null
	 * @param key ���� ��������
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}
	/**
	 * ���������� ����������� �������� �������
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 * @param key ���� ��������
	 * @param value ��������
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
