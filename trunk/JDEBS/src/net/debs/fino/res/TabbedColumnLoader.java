package net.debs.fino.res;

public class TabbedColumnLoader extends ResourceLoader {

	String columns[];
	private String id;
	String domainName;
	
	public TabbedColumnLoader(ResourceManager resourceManager, String domainName) {
		super(resourceManager);
		this.domainName = domainName;
	}

	protected void parseLine(String text)  {
		String values[] = text.split("\t");
		if (text.startsWith("//")) {
			// comments
		} else if (text.startsWith("#")) {
			// columns
			columns = values;
			columns[0] = columns[0].substring(1);
		} else {
			// columns values
			if (columns!=null) {
				for(int i=0; i<columns.length; i++) {
					parseColumn(columns[i], values[i]);
				}
			} else {
				System.err.println("parse file error: "+filePath);
			}
		}
	}

	private void parseColumn(String colName, String value) {
		if (colName.equalsIgnoreCase("id"))
			id = value;
		else
			resourceManager.putResource(domainName+"."+id+"."+colName, new Resource(parseColumnObject(colName, value)));		
	}

	protected Object parseColumnObject(String colName, String value) {
		return value;
	}
}
