package net.debs.fino.res;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

/**
 * Реализация хранилища ресурсов
 * @author AGrischenko
 *
 */
public class ResourceManager {
	
	Hashtable<String, Object> resources = new Hashtable<String, Object>();

	public static String MODEL = "model";
	public static String FACTION = "faction";
	public static String WEAPON = "weapon";
	
	protected static ResourceManager me;
	
	protected ResourceManager() {
	}
	
	public static void showAllResourcesAsText() {
		Iterator<Entry<String, Object>> it = me.resources.entrySet().iterator();
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = (java.util.Map.Entry<String, Object>) it.next();
			System.out.println(entry.getKey()+" = "+entry.getValue());
		}
	}

	public static ResourceManager init() {
		if (me==null)
			me = new ResourceManager();
		
		new FactionsLoader(me).load("resources/factions.txt");
		new ModelsLoader(me).load("resources/models.txt");
		new WeaponLoader(me).load("resources/weapons.txt");
		
		return me;
	}

	public static Object getResource(String query) {
		return me.getResourceImpl(query);
	}

	protected Object getResourceImpl(String query) {
		return resources.get(query.toLowerCase());
	}
	
	public static void putResource(String key, Object resource) {
		me.putResourceImpl(key, resource);
	}
	
	public void putResourceImpl(String key, Object resource) {
		resources.put(key.toLowerCase(), resource);
	}

	public static BufferedImage loadImage(String imagePath) throws IOException {
		FileInputStream fis = new FileInputStream(imagePath);
		BufferedImage img = null;
		try {
			img = ImageIO.read(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fis.close();
		return img;
	}

	public static Image getModelImage(String modelName, String factionName) {
		return me.getModelImageImpl(modelName, factionName);
	}

	protected Image getModelImageImpl(String modelName, String factionName) {
		BufferedImage bim = null;
		try {
			String imageKey = MODEL+"."+modelName+"."+factionName+".image.normal";
			bim = (BufferedImage) getResourceImpl(imageKey);
			if (bim==null) {
				
				// if image[model+faction] not exists - create it
				
				String resKey = MODEL+"."+modelName+".";
				String filePath = (String) getResourceImpl(resKey+ModelsLoader.IMAGEPATH);
				Image im = ResourceManager.loadImage("resources/"+filePath);

				// fill faction color

				int imw = im.getWidth(null);
				int imh = im.getHeight(null);
				bim = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB|
						BufferedImage.BITMASK|BufferedImage.OPAQUE);
				
				resKey = FACTION+"."+factionName+".";
				Color color = (Color) getResourceImpl(resKey+FactionsLoader.COLOR);
				Graphics g = bim.getGraphics();
				g.setColor(color);
				g.fillOval(2,2, im.getWidth(null)-4, im.getHeight(null)-4);
				g.drawImage(im, 0, 0, null);
				g.dispose();
				
				putResource(imageKey, bim);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bim;
	}

	public static Color getFactionColor(String factionName) {
		return me.getFactionColorImpl(factionName);
	}
	protected Color getFactionColorImpl(String factionName) {
		String resKey = FACTION+"."+factionName+".";
		return (Color) getResourceImpl(resKey+FactionsLoader.COLOR);
	}
}
