package net.debs.fino.test;

import java.util.HashMap;
import java.util.Map;

public class SimpleTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SimpleTests().run();
		
	}

	private void run() {
		Map<MP, String> table = new HashMap<MP, String>();
		
		table.put(new MP(1), "one");
		System.out.println(table.containsKey(new MP(1)));
		
		System.out.println((int)25/30);
	}

	class MP {

		public int a;
		
		public MP(int v) {
			a = v;
		}
		
		@Override
		public boolean equals(Object obj) {
			return a==((MP)obj).a;
		}

		@Override
		public int hashCode() {
			return a;
		}

		
	}
	
}
