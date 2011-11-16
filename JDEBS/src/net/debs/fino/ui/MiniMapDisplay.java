package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;
import net.debs.fino.res.ResourceManager;

public class MiniMapDisplay extends Component {

	private static final long serialVersionUID = 74666789191130106L;

	MapDisplay display;

	final static Color CONTROL_COLOR = new Color(150, 150, 0);
	
	DebsMap map;
	Rectangle viewPortCtl;
	BufferedImage mapImage;
	Rectangle area;
	double viewPortScale = 0.0;
	Dimension imsize;
	
	public MiniMapDisplay(MapDisplay display) {
		
		this.display = display;
		map = display.getMap();
		Dimension sz = new Dimension(200, 150);
		setPreferredSize(sz);
		setSize(sz);

		mapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		ViewControlMoveAdapter mad = new ViewControlMoveAdapter();
		addMouseMotionListener(mad);
		addMouseListener(mad);
		
		// on change size - redraw images		
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				mapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
				stuck();
			}

		});
	}

	private void stuck() {
		display.fireViewPortChanged();
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
		imsize = display.getFullMapDimension();
		
		double k = 0.0;
		if (imsize.width>=imsize.height) {
			k = (double) getWidth() / imsize.width;
		} else {
			k = (double) getHeight() / imsize.height;
		}

		area = new Rectangle(0, 0, (int)(imsize.width*k), (int)(imsize.height*k));
		
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

		redrawAll();

	}

	public synchronized void redrawAll() {
		// fill background
		Graphics2D g = mapImage.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		// draw objects
		drawObjects(g);

		// draw area bounding box
		g.setColor(Color.DARK_GRAY);
		g.drawRect(area.x, area.y, area.width-1, area.height-1);

		// draw viewport rectangle
		g.setColor(CONTROL_COLOR);
		g.drawRect(viewPortCtl.x, viewPortCtl.y, viewPortCtl.width, viewPortCtl.height);

		g.dispose();
		
		repaint();
	}

	private void drawObjects(Graphics2D g) {
		int cellw = area.width / map.getWidth();
		int cellh = area.height / map.getHeight();
		double cellSideLen = Math.max(cellw, cellh);
		
		MapObject object;
		Iterator<MapObject> it = new Vector<MapObject>(map.getAllMapObjects()).iterator();
		for (;it.hasNext();){
			object = it.next();
			MapPoint point = object.getMapPoint();
			int x = area.x + (int) (point.getX() * cellSideLen);
			int y = area.y + (int) (point.getY() * cellSideLen);
			if (object instanceof GameObject) {
				GameObject gm = (GameObject) object;
				Color fc = ResourceManager.getFactionColor((String)gm.getProperty("faction.name"));
				g.setColor(fc);
				int wo = (int) (cellSideLen<2.0?2.0:cellSideLen);
				g.fillOval(x, y, wo, wo);
			}
		}
	}

	public void updateArea(Rectangle viewPort) {
		viewPortCtl = new Rectangle(viewPort);
		updateAreaImpl();
	}

	private class ViewControlMoveAdapter extends MouseAdapter {
		int mdx = -1, mdy = -1;
		protected void repos(MouseEvent e) {
			int newX = e.getX() - viewPortCtl.width / 2;
			int newY = e.getY() - viewPortCtl.height / 2;

			display.changeViewPortPosition((int)((newX - viewPortCtl.x)/viewPortScale), (int)((newY - viewPortCtl.y)/viewPortScale));

			mdx = e.getXOnScreen();
			mdy = e.getYOnScreen();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (area.contains(e.getPoint())) {
				if (viewPortCtl.contains(e.getPoint())) {
					if (mdx>=0) {
						int x = e.getXOnScreen(), y = e.getYOnScreen();
						display.changeViewPortPosition((int)(-(mdx - x)/viewPortScale), (int)(-(mdy - y)/viewPortScale));
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
