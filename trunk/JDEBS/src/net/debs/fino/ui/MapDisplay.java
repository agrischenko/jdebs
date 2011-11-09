package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

public class MapDisplay extends Component {

	private static final long serialVersionUID = -1187739180606177056L;

	public static int MINCELLSIZE = 20;
	public static int MAXCELLSIZE = 50;
	
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

		DisplayMapMouseControl mad = new DisplayMapMouseControl(this);
		addMouseListener(mad);
		addMouseWheelListener(mad);
		addMouseMotionListener(mad);
		
		viewPort = new Rectangle(0, 0, 1, 1);
		recalcOfflineImageSize();
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
	
	public void recalcOfflineImageSize() {
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

	public void repairViewPort() {
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
	}

	private void drawObjects(Graphics2D g) {
		MapObject object;
		for (object = map.getFirstObject(); object!=null; object = map.getNextObject()){
			MapPoint point = object.getMapPoint();
			int x = point.getX() * cellSideLen;
			int y = point.getY() * cellSideLen;
			
			if (object instanceof GameObject) {
				GameObject gm = (GameObject) object;
				Image im = (Image) (gm.getProperty("graphics.defaultImage"));
				g.drawImage(im, x, y, cellSideLen, cellSideLen, this);
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
	
	public DebsMap getDebsMap() {
		return map;
	}
	
	public synchronized void redraw() {
		createLandscapeImage();
		createSceneImage();
		repaint();
		updateControls();
	}

	public Rectangle getViewPort() {
		return viewPort;
	}

	public int getCellSideLen() {
		return cellSideLen;
	}

	public Dimension getOffscreenImageDimension() {
		return new Dimension(imw, imh);
	}

	public void updateFinalImage() {
		createLandscapeImage();
		createSceneImage();
	}

	public int setCellSideLen(int i) {
		cellSideLen = i;
		if (cellSideLen<MapDisplay.MINCELLSIZE) {
			cellSideLen = MapDisplay.MINCELLSIZE;
		} else if (cellSideLen>MapDisplay.MAXCELLSIZE) {
			cellSideLen = MapDisplay.MAXCELLSIZE;
		}
		return cellSideLen;
	}
}
