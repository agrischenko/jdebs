package net.debs.fino.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.dnd.CharGenerator;
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

		GameObject object1 = CharGenerator.generate("1", "blue", CharGenerator.TYPE_WARRIOR);
		GameObject object2 = CharGenerator.generate("2", "green", CharGenerator.TYPE_WARRIOR);
		
		object1.setProperty("script", "test/scripts/testscript.lua");
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
	}

}
