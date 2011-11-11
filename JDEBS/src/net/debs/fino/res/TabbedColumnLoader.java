package net.debs.fino.res;

public class TabbedColumnLoader extends ResourceLoader {

	String columns[];
	String id;
	private int rowNum = 0;
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
					parseColumn(rowNum, columns[i], values[i]);
					++rowNum;
				}
			} else {
				System.err.println("parse file error: "+filePath);
			}
		}
	}

	protected void parseColumn(Integer rowNum, String colName, String value) {
		if (colName.equalsIgnoreCase("id"))
			id = value;
		else
			ResourceManager.putResource(domainName+"."+id+"."+colName, parseColumnObject(colName, value));		
	}

	protected Object parseColumnObject(String colName, String value) {
		return value;
	}

	@Override
	protected void parceEnd() {
		// TODO Auto-generated method stub
		
	}
}
