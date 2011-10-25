package net.debs.fino.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.debs.fino.DebsMap;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

public class MapDisplay extends Component {

	protected int cellWidth = 15;
	protected int cellHeight = 15;
	
	Color defaultUnitColor = new Color(45, 15, 235);
	
	BufferedImage sceneImage;
	int imw;
	int imh;
	
	private DebsMap map;
	
	public MapDisplay(DebsMap map) {
		this.map = map;

		imw = map.getWidth() * cellWidth;
		imh = map.getHeight() * cellHeight;
		sceneImage = new BufferedImage(imw, imh, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(imw, imh));
	}
	
	@Override
	public void paint(Graphics g) {
		// ������ ����� ��������
		createSceneImage();
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
			
			g.setColor(defaultUnitColor);
			
			g.fillOval(x, y, cellWidth, cellHeight);
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
