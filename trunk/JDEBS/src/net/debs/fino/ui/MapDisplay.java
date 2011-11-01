package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JViewport;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

public class MapDisplay extends Component {

	protected int MAXCELLSIZE = 50;
	
	protected int cellWidth = MAXCELLSIZE / 2;
	protected int cellHeight = MAXCELLSIZE / 2;
	
	BufferedImage sceneImage;
	int imw;
	int imh;
	
	protected DebsMap map;
	
	public MapDisplay(DebsMap map) {
		this.map = map;

		recalcSceneImage();
		setPreferredSize(new Dimension(imw, imh));
		
		addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.isControlDown()) {
					int oldw = cellWidth;
					int mod = e.getWheelRotation() * 2;
					cellWidth -= mod;
					cellHeight -= mod;
					if (cellWidth<10) {
						cellWidth = cellHeight = 10;
					} else if (cellWidth>MAXCELLSIZE) {
						cellWidth = cellHeight = MAXCELLSIZE;
					}
					
					if (oldw!=cellWidth) {
						recalcSceneImage();
						setPreferredSize(new Dimension(sceneImage.getWidth(), sceneImage.getWidth()));
						repaint();
					}
				}
			}
		});
		
		createSceneImage();
	}
	
	private void recalcSceneImage() {
		imw = map.getWidth() * cellWidth;
		imh = map.getHeight() * cellHeight;
		sceneImage = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public void paint(Graphics g) {
		// рисуем буфер картинки
		g.drawImage(sceneImage, 0, 0, this);
	}

	private void createSceneImage() {
		// create substrate

		Graphics2D g = sceneImage.createGraphics();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, imw, imh);

		drawScene(imw, imh, g);

		g.dispose();
	}

	private void drawScene(int imw, int imh, Graphics2D g) {
		drawGrid(g);
		drawObjects(g);
	}

	private void drawObjects(Graphics2D g) {
		MapObject obj;
		for (obj = map.getFirstObject(); obj!=null; obj = map.getNextObject()){
			MapPoint point = obj.getPivotPoint();
			int x = point.getX() * cellWidth;
			int y = point.getY() * cellHeight;
			
			if (obj instanceof GameObject) {
				GameObject gm = (GameObject) obj;
				Image im = (Image) (gm.getProperty("graphics.defaultImage"));
				g.drawImage(im, x, y, cellWidth, cellHeight, this);
			}
		}
	}

	private void drawGrid(Graphics2D g) {
		int i, pos;
		g.setColor(Color.DARK_GRAY);
		for(i=0, pos = cellWidth; i<map.getWidth(); i++, pos+=cellWidth)
			g.drawLine(pos, 0, pos, imh);
		for(i=0, pos = cellHeight; i<map.getHeight(); i++, pos+=cellHeight)
			g.drawLine(0, pos, imw, pos);
	}

	
	
}
