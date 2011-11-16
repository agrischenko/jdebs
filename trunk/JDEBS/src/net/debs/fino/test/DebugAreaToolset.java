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
import net.debs.fino.ui.area.MapDisplayAreaRedraw;

public class DebugAreaToolset extends JPanel {

	private static final long serialVersionUID = -5494456214747371073L;

	private static Vector<MapObject> allObjects = null; 

	private static final String START_SIMULATE = "startSimulate";
	private static final String STOP_SIMULATE = "stopSimulate";
	DebugButtonsAdapter bad = new DebugButtonsAdapter();

	MapDisplayAreaRedraw display;
	
	public DebugAreaToolset(MapDisplayAreaRedraw display) {
		this.display = display;
		init();
	}

	private void init() {
		add(createButton("Start Simulate", START_SIMULATE));
		add(createButton("Stop Simulate", STOP_SIMULATE));
	}

	private Component createButton(String caption, String cmd) {
		JButton btn = new JButton();
		btn.setText(caption);
		btn.setActionCommand(cmd);
		btn.addActionListener(bad);
		return btn;
	}

	private class DebugButtonsAdapter implements ActionListener {

		LUAProcessor processor;
		
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase(START_SIMULATE)) {
				processor = new LUAProcessor();
				processor.execute();
			} else if (e.getActionCommand().equalsIgnoreCase(STOP_SIMULATE)) {
				processor.interrupt();
			}

		}
	}
	
	class LUAProcessor extends SwingWorker<String, Object> {
		boolean isInterrupted = false;
		
		@Override
		public String doInBackground() {
			DebsMap map = display.getMap();
			while(!isInterrupted) {
				
				if (allObjects == null) allObjects = new Vector<MapObject>(map.getAllMapObjects());

				for (MapObject mapObject : allObjects) {
					if (mapObject instanceof GameObject) {
						GameObject object = (GameObject) mapObject;
						Action action = object.getAction(map);
						ResolverCore.resolveAction(object, action, map);
					}
				}
				synchronized (display) {
					display.redraw();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			return "OK";
		}

		public void interrupt() {
			isInterrupted = true;
		}

		@Override
		protected void done() {
			System.out.println("done");		
		}
		
	}
}
