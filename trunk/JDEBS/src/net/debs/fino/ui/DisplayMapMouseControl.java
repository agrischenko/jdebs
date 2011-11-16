package net.debs.fino.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
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
		if (e.isControlDown()) {
			if (mdx>=0) {
				int x = e.getXOnScreen(), y = e.getYOnScreen();
				display.getViewPort().x += mdx - x;
				display.getViewPort().y += mdy - y;
				display.repairViewPort();
				display.repaint();
			}
			mdx = e.getXOnScreen();
			mdy = e.getYOnScreen();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {
			int cellSideLen = display.getCellSideLen();
			int oldw = cellSideLen;
			int mod = e.getWheelRotation() * 2;
			cellSideLen = display.setCellSideLen(cellSideLen - mod);
			
			if (oldw!=cellSideLen) {
				Dimension imd = display.getOffscreenImageDimension();
				int oldImw = imd.width, oldImh = imd.height;

				display.recalcOfflineImageSize();
				// update viewport center point
				Rectangle viewPort = display.getViewPort();
				viewPort.x += (imd.width - oldImw) / 2;
				viewPort.y += (imd.height - oldImh) / 2;
				viewPort.width = Math.min(display.getWidth(), imd.width);
				viewPort.height = Math.min(display.getHeight(), imd.height);

				display.updateFinalImage();
				display.repairViewPort();
				display.repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mdx = -1;
		mdy = -1;
	}
}
