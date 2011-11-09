package net.debs.fino.test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.debs.fino.Action;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.resolver.ResolverCore;
import net.debs.fino.ui.MapDisplay;

public class DebugToolset extends JPanel {

	private static final long serialVersionUID = -5494456214747371073L;

	private static Vector<MapObject> allObjects = null; 

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

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase(NEXT_UNIT_STEP)) {
				(new LUAProcessor()).execute();
				
			}
		}
	}
	
	class LUAProcessor extends SwingWorker<String, Object> {
		@Override
		public String doInBackground() {
			DebsMap map = display.getDebsMap();
			while(true) {
//				long tm = System.currentTimeMillis();
				
				if (allObjects == null) allObjects = new Vector<MapObject>(map.getAllMapObjects());

//				System.out.println(String.format("collection created in %d ms", System.currentTimeMillis() - tm));

//				tm = System.currentTimeMillis();
				for (MapObject mapObject : allObjects) {
					if (mapObject instanceof GameObject) {
						GameObject object = (GameObject) mapObject;
						Action action = object.getAction(map);
						ResolverCore.resolveAction(object, action, map);
					}
				}
//				System.out.println(String.format("lua processed in %d ms", System.currentTimeMillis() - tm));

//				tm = System.currentTimeMillis();
				synchronized (display) {
					display.redraw();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
//				System.out.println(String.format("Redraw in %d ms", System.currentTimeMillis() - tm));
			}
			//return "";
		}

		@Override
		protected void done() {
			System.out.println("done");		
		}
		
	}
}
