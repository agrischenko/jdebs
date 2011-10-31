package net.debs.fino.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.ui.MapDisplay;

public class UITest extends JFrame {

	public static void main(String[] args) {
		new UITest().start();
	}

	private void start() {
		
		setSize(800, 600);
		setLocation(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		map.addObject(5, 5, new GameObject("warrior","red"));
		map.addObject(6, 5, new GameObject("warrior","red"));
		map.addObject(7, 5, new GameObject("archer","red"));
		map.addObject(6, 6, new GameObject("warrior","blue"));
		
		getContentPane().add(new JScrollPane(new MapDisplay(map)) );
		
		setVisible(true);
	}

}
