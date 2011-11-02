package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import net.debs.fino.ComplexMapObject;
import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

public class MapDisplay extends Component {

	private static final long serialVersionUID = -1187739180606177056L;

	protected int MINCELLSIZE = 20;
	protected int MAXCELLSIZE = 50;
	
	protected int cellSideLen = MAXCELLSIZE;

	MiniMapDisplay miniControl;
	
	BufferedImage sceneImage;
	BufferedImage landscapeImage;
	int imw;
	int imh;
	int cw;
	int ch;

	protected DebsMap map;

	Rectangle viewPort;
	
	public MapDisplay(DebsMap map) {
		this.map = map;

		MouseAdapter mad = new MouseControl();
		
		addMouseListener(mad);
		addMouseWheelListener(mad);
		addMouseMotionListener(mad);
		
		viewPort = new Rectangle(0, 0, 1, 1);
		recalcOfflineImageSize();
		viewPort.width = Math.min(cw, imw);
		viewPort.height = Math.min(ch, imh);
		repairViewPort();
		createLandscapeImage();
		createSceneImage();
		updateControls();
		
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				cw = getWidth();
				ch = getHeight();
				viewPort.width = Math.min(cw, imw);
				viewPort.height = Math.min(ch, imh);
				repaint();
				if (miniControl!=null)
					miniControl.viewPortChanged(viewPort);
			}
		});
		
	}
	
	private void recalcOfflineImageSize() {
		imw = map.getWidth() * cellSideLen;
		imh = map.getHeight() * cellSideLen;
		
		// repair by bigger side
		if (imw<cw && imh<ch) {
			if (imh>=imw) {
				cellSideLen = ch / map.getHeight();
			} else {
				cellSideLen = cw / map.getWidth();
			}
			imw = map.getWidth() * cellSideLen;
			imh = map.getHeight() * cellSideLen;
		}
		
		
	}

	private void repairViewPort() {
		int maxX = imw-cw;
		int maxY = imh-ch;
		if (imw<cw || viewPort.x<0) viewPort.x = 0;
		else if (viewPort.x>maxX) viewPort.x = maxX;
		if (imh<ch || viewPort.y<0) viewPort.y = 0;
		else if (viewPort.y>maxY) viewPort.y = maxY;

		if (miniControl!=null)
			miniControl.viewPortChanged(viewPort);
	}

	@Override
	public void paint(Graphics g) {
		// рисуем буфер картинки
		g.setColor(Color.black);
		g.fillRect(0,0, getWidth(), getHeight());
		int w = Math.min(cw, viewPort.width);
		int h = Math.min(ch, viewPort.height);
		int sx = (w<cw)?((cw-w)/2):0;
		int sy = (h<ch)?((ch-h)/2):0;
		g.drawImage(sceneImage, sx, sy, sx+w, sy+h,
				viewPort.x, viewPort.y, viewPort.x+w, viewPort.y+h,
				this);
	}

	private void createLandscapeImage() {
		landscapeImage = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = landscapeImage.createGraphics();
		drawLandscape(g);
		g.dispose();
	}

	private void drawLandscape(Graphics2D g) {
	}

	private void createSceneImage() {
		sceneImage = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = sceneImage.createGraphics();
		g.drawImage(landscapeImage, 0, 0, this);
		drawScene(g);
		g.dispose();
		
		if (miniControl!=null)
			miniControl.updateArea(sceneImage);

		drawGrid(g);
	}

	private void drawScene(Graphics2D g) {
		drawObjects(g);
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
        //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
        //Print free memory
        System.out.println("Free Memory:"
            + runtime.freeMemory() / mb);
        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);
        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
	}

	private void drawObjects(Graphics2D g) {
		MapObject obj;
		for (obj = map.getFirstObject(); obj!=null; obj = map.getNextObject()){
			Vector<MapObject> localObjects = null;
			if (obj instanceof ComplexMapObject) {
				// get all local visible simple objects
				localObjects = ((ComplexMapObject) obj).getAllObjects();
			} else {
				// add as local object
				localObjects = new Vector<MapObject>();
				localObjects.add(obj);
			}

			for (MapObject mapObject : localObjects) {
				MapPoint point = mapObject.getMapPoint();
				int x = point.getX() * cellSideLen;
				int y = point.getY() * cellSideLen;
				
				if (mapObject instanceof GameObject) {
					GameObject gm = (GameObject) mapObject;
					Image im = (Image) (gm.getProperty("graphics.defaultImage"));
					g.drawImage(im, x, y, cellSideLen, cellSideLen, this);
				}
			}
		}
	}

	private void drawGrid(Graphics g) {
		int i, pos;
		g.setColor(Color.DARK_GRAY);
		for(i=0, pos = cellSideLen; i<map.getWidth(); i++, pos+=cellSideLen)
			g.drawLine(pos, 0, pos, imh);
		for(i=0, pos = cellSideLen; i<map.getHeight(); i++, pos+=cellSideLen)
			g.drawLine(0, pos, imw, pos);
	}
	
	private class MouseControl extends MouseAdapter {
		int mdx = -1, mdy = -1;
		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.isControlDown()) {
				if (mdx>=0) {
					int x = e.getXOnScreen(), y = e.getYOnScreen();
					viewPort.x += mdx - x;
					viewPort.y += mdy - y;
					repairViewPort();
					repaint();
				}
				mdx = e.getXOnScreen();
				mdy = e.getYOnScreen();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				int oldw = cellSideLen;
				int mod = e.getWheelRotation() * 2;
				cellSideLen -= mod;
				if (cellSideLen<MINCELLSIZE) {
					cellSideLen = MINCELLSIZE;
				} else if (cellSideLen>MAXCELLSIZE) {
					cellSideLen = MAXCELLSIZE;
				}
				
				if (oldw!=cellSideLen) {
					int oldImw = imw, oldImh = imh;

					recalcOfflineImageSize();
					// update viewport center point
					viewPort.x += (imw - oldImw) / 2;
					viewPort.y += (imh - oldImh) / 2;
					viewPort.width = Math.min(cw, imw);
					viewPort.height = Math.min(ch, imh);

					createLandscapeImage();
					createSceneImage();

					repairViewPort();
					
					repaint();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mdx = -1;
			mdy = -1;
		}
	}

	public void addMiniControl(MiniMapDisplay control) {
		miniControl = control;
	}

	public void viewPortChanged(int dx, int dy) {
		viewPort.x += dx;
		viewPort.y += dy;
		repairViewPort();
		repaint();
	}

	public void updateControls() {
		if (miniControl!=null) {
			miniControl.updateArea(sceneImage, viewPort);
		}
	}
}
