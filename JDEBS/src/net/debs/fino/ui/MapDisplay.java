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
import java.util.Iterator;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

public class MapDisplay extends Component {

	private static final long serialVersionUID = -1187739180606177056L;

	private static final int MINCELLSIZE = 20;
	private static final int MAXCELLSIZE = 50;

	private int cellSize = MAXCELLSIZE;

	protected Image sceneImage;
	protected BufferedImage landscapeImage;
	protected int imw;
	protected int imh;

	protected DebsMap map;
	protected MiniMapDisplay miniControl;

	protected Rectangle viewPort = new Rectangle();
	protected Rectangle viewPortCells = new Rectangle();
	
	public MapDisplay(DebsMap map) {
		this.map = map;

		DisplayMapMouseControl mad = new DisplayMapMouseControl(this);
		addMouseListener(mad);
		addMouseWheelListener(mad);
		addMouseMotionListener(mad);

		setIgnoreRepaint(true);
		
		// on change size - redraw images		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				patchViewPortAndRedraw();
				fireViewPortChanged();
			}
		});
	}

	public void fireViewPortChanged() {
		if (miniControl!=null)
			miniControl.updateArea(viewPort);
	}

	public void reCreateViewScene() {
		if (viewPort.isEmpty())
			return;
		updateOffscreenImageDimension();
		createLandscapeImage();
		createSceneImage();
	}

	private void updateOffscreenImageDimension() {
		// calculate cells viewport
		viewPortCells.x = (int) viewPort.x / cellSize;
		viewPortCells.y = (int) viewPort.y / cellSize;

		viewPortCells.width = (int) viewPort.width / cellSize + 2;
		viewPortCells.height = (int) viewPort.height / cellSize + 2;
		if ((viewPortCells.x + viewPortCells.width)>map.getWidth()) viewPortCells.width = map.getWidth() - viewPortCells.x;
		if ((viewPortCells.y + viewPortCells.height)>map.getHeight()) viewPortCells.height = map.getHeight() - viewPortCells.y;
		
		// calculate image size
		imw = viewPortCells.width * cellSize;
		imh = viewPortCells.height * cellSize;
	}

	private void patchViewPortAndRedraw() {
		Dimension fimsize = getFullMapDimension();

		viewPort.width = getWidth();
		viewPort.height = getHeight();

		// calibrate size
		if (viewPort.width > fimsize.width)
			viewPort.width = fimsize.width;
		
		if (viewPort.height > fimsize.height)
			viewPort.height = fimsize.height;

		// calibrate position
		if ((viewPort.x+viewPort.width)>fimsize.width) viewPort.x = fimsize.width - viewPort.width;
		if ((viewPort.y+viewPort.height)>fimsize.height) viewPort.y = fimsize.height - viewPort.height;
		if (viewPort.x<0) viewPort.x = 0;
		if (viewPort.y<0) viewPort.y = 0;

		reCreateViewScene();
		repaint();
	}

	public void changeViewPortPosition(int mx, int my) {
		viewPort.x += mx;
		viewPort.y += my;
		patchViewPortAndRedraw();
		fireViewPortChanged();		
	}

	public void changeCellSize(int newCellSize) {
		cellSize += newCellSize;
		if (cellSize<MINCELLSIZE) cellSize = MINCELLSIZE;
		else if (cellSize>MAXCELLSIZE) cellSize = MAXCELLSIZE;

		patchViewPortAndRedraw();
		fireViewPortChanged();		
	}
	
	private void createLandscapeImage() {
		if (landscapeImage==null 
				|| landscapeImage.getWidth()!=imw || landscapeImage.getHeight()!=imh )
			landscapeImage = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = landscapeImage.createGraphics();
		drawLandscape(g);
		g.dispose();
	}

	private void drawLandscape(Graphics2D g) {
	}

	private void createSceneImage() {
		if (sceneImage==null 
				|| sceneImage.getWidth(null)!=imw || sceneImage.getHeight(null)!=imh )
			sceneImage = createImage(imw, imh);
		Graphics g = sceneImage.getGraphics();
		g.drawImage(landscapeImage, 0, 0, this);
		drawScene(g);
		drawGrid(g);
		g.dispose();
		
	}

	private void drawScene(Graphics g) {
		drawObjects(g);
	}

	private void drawObjects(Graphics g) {
		MapObject object;
		Iterator<MapObject> it = new Vector<MapObject>(map.getAllMapObjects()).iterator();
		for (;it.hasNext();){
			object = it.next();
			MapPoint point = object.getMapPoint();
			if (viewPortCells.contains(point.getAWTPoint())) {
				int x = (point.getX() - viewPortCells.x) * cellSize;
				int y = (point.getY() - viewPortCells.y) * cellSize;
				if (object instanceof GameObject) {
					GameObject gm = (GameObject) object;
					Image im = (Image) (gm.getProperty("graphics.defaultImage"));
					g.drawImage(im, x, y, cellSize, cellSize, this);
				}
			}
		}
	}

	private void drawGrid(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		int i, pos;
		for(i=0, pos = cellSize; i<viewPortCells.getWidth(); i++, pos+=cellSize)
			g.drawLine(pos, 0, pos, imh);
		for(i=0, pos = cellSize; i<viewPortCells.getHeight(); i++, pos+=cellSize)
			g.drawLine(0, pos, imw, pos);
	}

	public void updateFinalImage() {
		createLandscapeImage();
		createSceneImage();
	}

	public void addMiniControl(MiniMapDisplay miniMapAreaDisplay) {
		miniControl = miniMapAreaDisplay;
	}

	public synchronized void redraw() {
		createSceneImage();
		repaint();
		if (miniControl!=null)
			miniControl.redrawAll();
	}

	public DebsMap getMap() {
		return map;
	}

	public Dimension getFullMapDimension() {
		return new Dimension(map.getWidth()*cellSize, map.getHeight()*cellSize);
	}

	@Override
	public boolean isDoubleBuffered() {
		return true;
	}
	
	@Override
	public void paint(Graphics g) {
		// рисуем буфер картинки
		int w = Math.min(viewPort.width, imw);
		int h = Math.min(viewPort.height, imh);
		int x = imw<getWidth() ? (getWidth()-w)/2 : 0;
		int y = imh<getHeight() ? (getHeight()-h)/2 : 0;
		
		int cx = viewPort.x - (viewPortCells.x*cellSize);
		int cy = viewPort.y - (viewPortCells.y*cellSize);
		
		g.drawImage(sceneImage, x, y, x+w, y+h, 
			cx, cy, cx+viewPort.width, cy+viewPort.height,
			this);
	}
}
