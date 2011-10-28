package net.debs.fino;

import java.util.Hashtable;

import net.debs.fino.ai.AICore;

/**
 * Реализация игрового объекта
 * @author AGrischenko
 *
 */
public class GameObject extends ComplexMapObject {

	public final static String UNDEFINED_ID = "<undefined>";
	
	private String id = UNDEFINED_ID;
	
	/**
	 * видно в AI Script
	 */
	Hashtable<String, Object> properties = new Hashtable<String, Object>();
	/**
	 * не видно в AI Script
	 */
	Hashtable<String, Object> systemProperties = new Hashtable<String, Object>();

	/**
	 * Получить системное свойство объекта
	 * @return значение по ключу. Если отсутствует - значит null
	 * @param key ключ свойства
	 */
	public Object getSystemProperty(String key) {
		return systemProperties.get(key);
	}
	/**
	 * Установить системное свойство объекта
	 * @return старое значение по этому ключу. Если значение отсутствует - значит null
	 */
	public Object setSystemProperty(String key, Object value) {
		Object oldValue = getSystemProperty(key);
		systemProperties.put(key, value);
		return oldValue;
	}

	/**
	 * Получить скриптуемое свойство объекта
	 * @return значение по ключу. Если значение отсутствует - значит null
	 * @param key ключ свойства
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}
	/**
	 * Установить скриптуемое свойство объекта
	 * @return старое значение по этому ключу. Если значение отсутствует - значит null
	 * @param key ключ свойства
	 * @param value значение
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
