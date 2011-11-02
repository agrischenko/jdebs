package net.debs.fino.test;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.res.ResourceManager;
import net.debs.fino.resolver.ResolverCore;
import net.debs.fino.ui.MapDisplay;
import net.debs.fino.ui.MiniMapDisplay;

public class UITest extends JFrame {

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
		object1.setProperty("script", "test/scripts/testscript.lua");

		GameObject object2 = new GameObject("archer","Green");
		object2.setId("2");
		object2.setProperty("script", "test/scripts/testscript.lua");

		map.addObject(5, 5, object1);
		map.addObject(7, 5, object2);
		
		ResolverCore.setPathToActionScript("test/scripts");
		
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
