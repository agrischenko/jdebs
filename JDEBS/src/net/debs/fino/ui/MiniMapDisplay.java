package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MiniMapDisplay extends Component {

	private static final long serialVersionUID = 74666789191130106L;

	MapDisplay display;

	final static Color CONTROL_COLOR = new Color(150, 150, 0);
	
	Rectangle viewPortCtl;
	BufferedImage mapImage;
	BufferedImage sceneImage;
	Rectangle area;
	double viewPortScale = 0.0;
			
	public MiniMapDisplay(MapDisplay display) {
		
		this.display = display;
		
		Dimension sz = new Dimension(200, 150);
		setPreferredSize(sz);
		setSize(sz);

		mapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		ViewControlMoveAdapter mad = new ViewControlMoveAdapter();
		addMouseMotionListener(mad);
		addMouseListener(mad);
		
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(mapImage, 0, 0, this);
	}

	public void addDisplay(MapDisplay control) {
		display = control;
		display.addMiniControl(this);
	}

	public void viewPortChanged(Rectangle viewPort) {
		viewPortCtl = new Rectangle(viewPort);
		updateAreaImpl();
	}

	private void updateAreaImpl() {
		if (sceneImage==null) return;

		Graphics2D g = mapImage.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		double k = 0.0;
		if (sceneImage.getWidth()>=sceneImage.getHeight()) {
			k = (double) getWidth() / sceneImage.getWidth();
		} else {
			k = (double) getHeight() / sceneImage.getHeight();
		}

		area = new Rectangle(0, 0, (int)(sceneImage.getWidth()*k), (int)(sceneImage.getHeight()*k));
		double k2 = 1.0;
		if (area.width>getWidth()) {
			k2 = (double) getWidth() / area.width;
			area.width = getWidth();
			area.height *= k2;
		} else if (area.height>getHeight()) {
			k2 = (double) getHeight() / area.height;
			area.height = getHeight();
			area.width *= k2;
		}

		area.x = (getWidth()-area.width) / 2;
		area.y = (getHeight()-area.height) / 2;

		viewPortScale = k * k2;
		viewPortCtl.x = (int) (viewPortCtl.x * viewPortScale) + area.x;
		viewPortCtl.y = (int) (viewPortCtl.y * viewPortScale) + area.y;
		viewPortCtl.width *= viewPortScale;
		viewPortCtl.height *= viewPortScale;

		
		g.setColor(Color.DARK_GRAY);
		g.drawImage(sceneImage, area.x, area.y, area.x + area.width, area.y + area.height,
				0, 0, sceneImage.getWidth(), sceneImage.getHeight(), this);
		g.drawRect(area.x, area.y, area.width-1, area.height-1);

		// draw viewport rectangle

		g.setColor(CONTROL_COLOR);
		g.drawRect(viewPortCtl.x, viewPortCtl.y, viewPortCtl.width, viewPortCtl.height);

		g.dispose();
		
		repaint();
	}

	public void updateArea(BufferedImage image) {
		this.sceneImage = image;
	}
	public void updateArea(BufferedImage image, Rectangle viewPort) {
		viewPortCtl = new Rectangle(viewPort);
		this.sceneImage = image;
		updateAreaImpl();
	}

	private class ViewControlMoveAdapter extends MouseAdapter {
		int mdx = -1, mdy = -1;
		protected void repos(MouseEvent e) {
			int newX = e.getX() - viewPortCtl.width / 2;
			int newY = e.getY() - viewPortCtl.height / 2;
			display.viewPortChanged((int)((newX - viewPortCtl.x)/viewPortScale), (int)((newY - viewPortCtl.y)/viewPortScale));

			mdx = e.getXOnScreen();
			mdy = e.getYOnScreen();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (area.contains(e.getPoint())) {
				if (viewPortCtl.contains(e.getPoint())) {
					if (mdx>=0) {
						int x = e.getXOnScreen(), y = e.getYOnScreen();
						display.viewPortChanged((int)(-(mdx - x)/viewPortScale), (int)(-(mdy - y)/viewPortScale));
					}
				} else
					repos(e);
			}
			mdx = e.getXOnScreen();
			mdy = e.getYOnScreen();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			repos(e);
		}
		
	}		
}
