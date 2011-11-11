package net.debs.fino.res;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class ResourceLoader {
	ResourceManager resourceManager;
	
	String filePath;
	
	public ResourceLoader(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void load(String resourceFilePath) {
		
		// read tabbed text file
		
		filePath = resourceFilePath;
		
		StringBuffer text = new StringBuffer();

		try {
			FileReader fr = new FileReader(resourceFilePath);
			char cb[] = new char[1024];
			while(fr.ready()) {
				int byteReaded = fr.read(cb);
				text.append(cb, 0, byteReaded);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// parse lines
		
		String[] lines = text.toString().split("\r\n");
		for (String line : lines) {
			parseLine(line);
		}
		parceEnd();
	}

	abstract protected void parseLine(String text);
	
	abstract protected void parceEnd();
	
}
