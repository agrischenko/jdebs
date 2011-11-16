package net.debs.fino;

import java.awt.Image;
import java.io.IOException;
import java.util.Hashtable;

import net.debs.fino.ai.AICore;
import net.debs.fino.dnd.Weapon;
import net.debs.fino.res.ResourceManager;

/**
 * ���������� �������� �������
 * @author AGrischenko
 *
 */
public class GameObject extends MapObject {

	public final static String UNDEFINED_ID = "<undefined>";
	
	private static int idcount = 0; 
	
	private String id = UNDEFINED_ID;
	
	public GameObject() {
		
		id = String.valueOf(GameObject.idcount++);
		
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
		setProperty("faction.name", factionName);
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
		Object prop = getPropertyWithoutCheck(key);
		if (prop == null) System.err.println(String.format("Property \"%s\" does not exists in GameObject::%s", key, getId()));
		return prop;
	}

	private Object getPropertyWithoutCheck(String key) {
		return properties.get(key);
	}
	
	/**
	 * ���������� �������� �������
	 * @return ������ �������� �� ����� �����. ���� �������� ����������� - ������ null
	 */
	public Object setProperty(String key, Object value) {
		Object oldValue = getPropertyWithoutCheck(key);
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
	
	public int getHp(){
		Integer hp = (Integer) properties.get("hp");
		if (hp == null) {
			System.err.println("Gameobject \"" + getId() + "\" does not contains property \"hp\"");
			return 0;
		}
		return hp;
	}
	
	public void setHp(int hp){
		properties.put("hp", hp);
	}
	
	public Weapon getWeapon(){
		
		String weaponName = (String) this.getProperty("weapon");
		
		//���� ������ � ������� �� ������ - �� ���������� ������ "unarmored"
		if (weaponName == null) return (Weapon) ResourceManager.getResource(ResourceManager.WEAPON + "." + "unarmored");
		
		Weapon weapon = (Weapon) ResourceManager.getResource(ResourceManager.WEAPON + "." + weaponName);
		if (weapon == null) {
			System.err.println("Gameobject \"" + getId() + "\" have weapon (" + weaponName + ") thet does not contains in resources");
			return null;
		}
		
		return weapon;
		
	}
		
}
