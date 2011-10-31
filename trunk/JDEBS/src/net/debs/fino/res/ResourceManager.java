package net.debs.fino.res;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;

import net.debs.fino.Property;
import net.debs.fino.Variant;

/**
 * Реализация хранилища ресурсов
 * @author AGrischenko
 *
 */
public class ResourceManager {
	
	Hashtable<String, net.debs.fino.res.Resource> resources = new Hashtable<String, net.debs.fino.res.Resource>();

	public static String MODEL = "model";
	public static String FACTION = "faction";
	
	protected static ResourceManager me = new ResourceManager();
	
	protected ResourceManager() {
		new FactionsLoader(this).load("resources/factions.txt");
		new ModelsLoader(this).load("resources/models.txt");
		
		showAllResourcesAsText();
	}
	
	private void showAllResourcesAsText() {
		Iterator it = resources.entrySet().iterator();
		while(it.hasNext()) {
			java.util.Map.Entry<String, Resource> entry = (java.util.Map.Entry<String, Resource>) it.next();
			Resource r = entry.getValue();
			System.out.println(entry.getKey()+" = "+r.get());
		}
	}

	public static ResourceManager me() {
		return me;
	}
	
	public static Resource getResource(String query) {
		return me.getResourceImpl(query);
	}

	protected Resource getResourceImpl(String query) {
		Resource r = resources.get(query.toLowerCase());
		if (r==null)
			r = new Resource();
		return r;
	}
	
	public static void putResource(String key, Resource resource) {
		me.putResourceImpl(key, resource);
	}
	
	public void putResourceImpl(String key, Resource resource) {
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
			bim = (BufferedImage) getResourceImpl(imageKey).get();
			if (bim==null) {
				
				// if image[model+faction] not exists - create it
				
				String resKey = MODEL+"."+modelName+".";
				String filePath = getResourceImpl(resKey+ModelsLoader.IMAGEPATH).getString();
				Image im = ResourceManager.loadImage("resources/"+filePath);

				// fill faction color

				int imw = im.getWidth(null);
				int imh = im.getHeight(null);
				bim = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
				
				resKey = FACTION+"."+factionName+".";
				Color color = (Color) getResourceImpl(resKey+FactionsLoader.COLOR).get();
				Graphics g = bim.getGraphics();
				g.setColor(color);
				g.fillOval(0,0, im.getWidth(null), im.getHeight(null));
				g.drawImage(im, 0, 0, null);
				g.dispose();
				
				putResource(imageKey, new Resource(bim));
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bim;
	}

}
