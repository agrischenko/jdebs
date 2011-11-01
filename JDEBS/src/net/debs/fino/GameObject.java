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
 * Реализация игрового объекта
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
	 * не видно в AI Script
	 */
	Hashtable<String, Object> properties = new Hashtable<String, Object>();
	/**
	 * видно в AI Script
	 */
	Hashtable<String, Object> aiProperties = new Hashtable<String, Object>();

	/**
	 * Получить свойство объекта (используемое в скрипте)
	 * @return значение по ключу. Если значение отсутствует - значит null
	 * @param key ключ свойства
	 */	
	public Object getAiProperty(String key) {
		return aiProperties.get(key);
	}
	
	/**
	 * Установить свойство объекта (используемое в скрипте)
	 * @return старое значение по этому ключу. Если значение отсутствует - значит null
	 * @param key ключ свойства
	 * @param value значение
	 */
	public Object setAiProperty(String key, Object value) {
		Object oldValue = getAiProperty(key);
		aiProperties.put(key, value);
		return oldValue;
	}

	/**
	 * Получить свойство объекта
	 * @return значение по ключу. Если отсутствует - значит null
	 * @param key ключ свойства
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * Установить свойство объекта
	 * @return старое значение по этому ключу. Если значение отсутствует - значит null
	 */
	public Object setProperty(String key, Object value) {
		Object oldValue = getProperty(key);
		properties.put(key, value);
		return oldValue;
	}

	/**
	 * Получить ID объекта
	 * @return номер объекта в системе
	 */
	public String getId() {
		return id;
	}

	/**
	 * Запомнить ID объекта
	 * @param id уникальный номер объекта в системе
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * запрос действия объекта
	 * Вызывается игровым циклом
	 * @return объект действия
	 * @param map игровая карта
	 * @see Action
	 */
	public Action getAction(DebsMap map) {
		return AICore.getAction(this, map);
	}
	
}
