package net.debs.fino.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.res.ResourceManager;
import net.debs.fino.ui.MapDisplay;
import net.debs.fino.ui.MiniMapDisplay;

public class UITest extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UITest().start();
			}
		});
	}

	private void start() {
		
		setSize(850, 650);
		setLocation(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ResourceManager.init();
		ResourceManager.showAllResourcesAsText();

		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);

		//map.addObject(5, 5, new GameObject("warrior","blue"));
		//map.addObject(7, 5, new GameObject("archer","Green"));
		//map.addObject(10, 8, new GameObject("chupa","red"));
		//map.addObject(6, 6, new GameObject("elephant","gray"));
		//map.addObject(29, 5, new GameObject("chupa","red"));
		
		GameObject object1 = new GameObject("warrior","blue");
		object1.setId("1");
		object1.setProperty("speed", 2);
		object1.setProperty("faction", "1");
		object1.setProperty("rangeOfVisibility", 10);
		object1.setProperty("seeable", true);
		object1.setProperty("passable", false);
		object1.setProperty("script", "test/scripts/testscript.lua");

		GameObject object2 = new GameObject("archer","Green");
		object2.setId("2");
		object2.setProperty("faction", "2");
		object2.setProperty("speed", 1);
		object2.setProperty("rangeOfVisibility", 10);
		object2.setProperty("seeable", true);
		object2.setProperty("passable", false);
		object2.setProperty("script", "test/scripts/testscript2.lua");

		map.addObject(5, 29, object1);
		map.addObject(7, 5, object2);
		
		getContentPane().setLayout(new BorderLayout(2,10));
		MapDisplay dspmap = new MapDisplay(map);
		getContentPane().add(dspmap, BorderLayout.CENTER);
		JPanel tools = new JPanel(new BorderLayout());
		getContentPane().add(tools, BorderLayout.EAST);
		MiniMapDisplay minimap = new MiniMapDisplay(dspmap);
		minimap.addDisplay(dspmap);
		tools.add(minimap, BorderLayout.NORTH);

		DebugToolset toolset = new DebugToolset(dspmap);
		
		tools.add(toolset, BorderLayout.CENTER);
		
		setVisible(true);
		
		dspmap.updateControls();
	}

}
