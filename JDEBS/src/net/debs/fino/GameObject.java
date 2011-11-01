package net.debs.fino;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import net.debs.fino.ai.AICore;
import net.debs.fino.res.ResourceManager;

/**
 * ���������� �������� �������
 * @author AGrischenko
 *
 */
public class GameObject extends ComplexMapObject {

	public final static String UNDEFINED_ID = "<undefined>";
	
	private String id = UNDEFINED_ID;
	
	public GameObject() {
		Image defaultImage = null;
		try {
			defaultImage = ResourceManager.loadImage("resources/overrides/warrior.gif ");
			properties.put("graphics.defaultImage", defaultImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameObject(String modelName, String factionName) {
		Image im = ResourceManager.getModelImage(modelName, factionName);
		setProperty("graphics.defaultImage", im);
	}

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
