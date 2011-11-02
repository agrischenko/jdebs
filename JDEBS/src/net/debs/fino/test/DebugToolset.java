package net.debs.fino.test;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.resolver.ResolverCore;
import net.debs.fino.ui.MapDisplay;

public class DebugToolset extends JPanel {

	private static final long serialVersionUID = -5494456214747371073L;

	private static final String NEXT_UNIT_STEP = "nextUnitStep";
	DebugButtonsAdapter bad = new DebugButtonsAdapter();

	MapDisplay display;
	
	public DebugToolset(MapDisplay display) {
		this.display = display;
		init();
	}

	private void init() {
		add(createNextStep());
	}

	private Component createNextStep() {
		JButton btn = new JButton();
		btn.setText("Next unit step");
		btn.setActionCommand(NEXT_UNIT_STEP);
		btn.addActionListener(bad);
		return btn;
	}

	private class DebugButtonsAdapter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DebsMap map = display.getDebsMap();
			if (e.getActionCommand().equalsIgnoreCase(NEXT_UNIT_STEP)) {
				Vector<MapObject> allObjects = new Vector<MapObject>(map.getAllMapObjects());
				for (MapObject mapObject : allObjects) {
					if (mapObject instanceof GameObject) {
						GameObject object = (GameObject) mapObject;
						Action action = object.getAction(map);
						ResolverCore.resolveAction(object, action, map);
						display.redraw();
					}
				}
			}
		}
		
	}
	
}
