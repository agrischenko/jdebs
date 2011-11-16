package net.debs.fino.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class DisplayMapMouseControl extends MouseAdapter {
	int mdx = -1, mdy = -1;
	
	private MapDisplay display;
	
	public DisplayMapMouseControl(MapDisplay dsp) {
		display = dsp;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (mdx>=0) {
			int x = e.getXOnScreen(), y = e.getYOnScreen();
			display.changeViewPortPosition(mdx - x, mdy - y);
		}
		mdx = e.getXOnScreen();
		mdy = e.getYOnScreen();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		display.changeCellSize(-e.getWheelRotation() * 2);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mdx = -1;
		mdy = -1;
	}
}
