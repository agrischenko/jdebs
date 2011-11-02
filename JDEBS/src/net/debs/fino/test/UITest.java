package net.debs.fino.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.res.ResourceManager;
import net.debs.fino.ui.MapDisplay;
import net.debs.fino.ui.MiniMapDisplay;

public class UITest extends JFrame {

	public static void main(String[] args) {
		new UITest().start();
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

		map.addObject(5, 5, new GameObject("warrior","blue"));
		map.addObject(7, 5, new GameObject("archer","Green"));
		map.addObject(10, 8, new GameObject("chupa","red"));
		map.addObject(6, 6, new GameObject("elephant","gray"));
		map.addObject(29, 5, new GameObject("chupa","red"));
		
		getContentPane().setLayout(new BorderLayout(2,10));
		MapDisplay dspmap = new MapDisplay(map);
		getContentPane().add(dspmap, BorderLayout.CENTER);
		JPanel tools = new JPanel(new BorderLayout());
		getContentPane().add(tools, BorderLayout.EAST);
		MiniMapDisplay minimap = new MiniMapDisplay(dspmap);
		minimap.addDisplay(dspmap);
		tools.add(minimap, BorderLayout.NORTH);
		tools.add(new JPanel(), BorderLayout.CENTER);
		
		setVisible(true);
		
		dspmap.updateControls();
	}

}