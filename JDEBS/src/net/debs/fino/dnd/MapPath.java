package net.debs.fino.dnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapPoint;
import net.debs.fino.Path;
import net.debs.fino.ai.AIMap;

/**
 * –еализаци€ класа дл€ определени€ пути по правилам DnD
 * @author AAntonenko
 */
public class MapPath {

	private static AIMap gMap = null;
	private static MapPoint gP1 = null;
	private static MapPoint gP2 = null;
	
	//“ест построени€ пути
	public static void main(String [] args){
		
		DebsMap map = new DebsMap();
		map.setWidth(30);
		map.setHeight(30);
		
		GameObject me = new GameObject();
		me.setProperty("rangeOfVisibility", 20);
		me.setProperty("seeable", true);
		me.setProperty("passable", false);
		map.addObject(0, 0, me);
		
		GameObject object1 = new GameObject();
		object1.setProperty("seeable", false);
		object1.setProperty("passable", false);

		map.addObject(5, 5, object1);
		
		map.addObject(6, 5, object1);
		map.addObject(7, 5, object1);
		map.addObject(8, 5, object1);
		map.addObject(9, 5, object1);
		
		map.addObject(5, 6, object1);
		map.addObject(5, 7, object1);
		map.addObject(5, 8, object1);
		map.addObject(5, 9, object1);
		
		map.addObject(9, 9, object1);
		map.addObject(10, 9, object1);
		map.addObject(9, 10, object1);
		
		Path path = getPath(new AIMap(map, me), new MapPoint(0,0), new MapPoint(10, 10));
		
		if(path == null)
			System.out.println("No path");
		else{
			System.out.print("Path = ");
			for (int j = 0; j < path.size(); j++) {
				if (j != 0) System.out.print(", ");
				System.out.print(path.get(j));
			}
		}
	}
	
	/**
	 * ќпредел€ет путь между двум€ точками
	 * @param map карта на которой будет происходить определение пути
	 * @param p1 точка от которой будет происходить  определение пути
	 * @param p2 точка до которой будет происходить  определение пути
	 * @return путь от точки p1 до точки p2
	 */
	public static Path getPath(AIMap map, MapPoint p1, MapPoint p2){
		
		gMap = map;
		gP1 = p1;
		gP2 = p2;
		
		Vector<MapPoint> closeset = new Vector<MapPoint>();
		BinaryHeap<eMapPoint> openset = new BinaryHeap<eMapPoint>();

		Hashtable<MapPoint, Double> tg = new Hashtable<MapPoint, Double>();
		Hashtable<MapPoint, Integer> th = new Hashtable<MapPoint, Integer>();
		Hashtable<MapPoint, Double> tf = new Hashtable<MapPoint, Double>();
		
		Double xg, yg;
		Integer xh, yh;
		Double xf, yf;

		xg = 0.;
		xh = heuristic(gP1);
		xf = xg + xh;
		
		tg.put(gP1, xg);
		th.put(gP1, xh);
		tf.put(gP1, xf);
		
		openset.add(new eMapPoint(gP1, null, xf));
		
		while (!openset.isEmpty()){
			
			//ѕолучаем текущую точку из очереди на обработку
			eMapPoint iPoint = openset.remove();
			
			xg = tg.get(iPoint);
			
			//≈сли текуща€ тока пути €вл€етс€ конечной - то возвращаем сформированый путь
			if (iPoint.equals(gP2)) return getPathFromEnd(iPoint);
			
			//ƒобавление точки к уже обработаным вершинам
			closeset.add(iPoint);
			
			Vector<MapPoint> nearestMapPoints = getNearestMapPoints(iPoint);
			
			boolean isBeter = false;
			
			for (MapPoint point : nearestMapPoints) {
				if (closeset.contains(point)) continue;
				
				yg = xg + distance(iPoint, point);
				yh = heuristic(point);
				yf = yg + yh;
				
				eMapPoint ePoint = new eMapPoint(point, iPoint, yf);
				
				if(!openset.contains(ePoint)){
					openset.add(ePoint);
					isBeter = true;
				}
				else{
					if (yg < tg.get(point)){
						openset.changeToBetter(ePoint, ePoint);
						isBeter = true;
					}
				}
				if (isBeter){
					tg.put(point, yg);
					th.put(point, yh);
					tf.put(point, yf);
				}
			}
		}
		
		return null;
	}
	
	/*
	 * ≈вристическа€ оценка текущей точки к конечной
	 */
	private static Integer heuristic(MapPoint cur){

		int dx = Math.abs(cur.getX() - gP2.getX());
		int dy = Math.abs(cur.getY() - gP2.getY());
		int minD = Math.min(dx, dy);
		int maxD = Math.max(dx, dy);
		return (minD >> 1) + maxD;
		
	}
	
	/*
	 * ѕолучение ближайших проходимых (невидимые квадраты считаютс€ проходимыми) квадратов к переданному
	 */
	private static Vector<MapPoint> getNearestMapPoints(MapPoint point){
		
		Vector<MapPoint> points = new Vector<MapPoint>();
		
		int maxX = MapPath.gMap.getWidth() - 1;
		int maxY = MapPath.gMap.getHeight() - 1;
		int x = point.getX();
		int y = point.getY();
		
		MapPoint iPoint;
		Boolean passable;
		
		if (x - 1 >= 0 && y - 1 >= 0){
			iPoint = new MapPoint(x - 1, y - 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (y - 1 >= 0){
			iPoint = new MapPoint(x, y - 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (x + 1 <= maxX && y - 1 >= 0){
			iPoint = new MapPoint(x + 1, y - 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (x + 1 <= maxX){
			iPoint = new MapPoint(x + 1, y);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (x + 1 <= maxX && y + 1 <= maxY){
			iPoint = new MapPoint(x + 1, y + 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (y + 1 <= maxY){
			iPoint = new MapPoint(x, y + 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (x - 1 >= 0 && y + 1 <= maxY){
			iPoint = new MapPoint(x - 1, y + 1);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		if (x - 1 >= 0){
			iPoint = new MapPoint(x - 1, y);
			passable = gMap.passable(iPoint);
			if (passable == null || passable) points.add(iPoint);
		}
		
		return points;
	}
	
	/*
	 * ѕолучение пути от изначальной точки к переданной
	 */
	private static Path getPathFromEnd(eMapPoint point){
		
		LinkedList<MapPoint> tmppath = new LinkedList<MapPoint>();
		
		eMapPoint iPoint = point;
		while (!iPoint.equals(gP1)){
			tmppath.addFirst(iPoint);
			iPoint = iPoint.from;
		}

		Path path = new Path();
		for (int i = 0; i < tmppath.size(); i++) {
			path.add(tmppath.get(i));
		}
		
		return path;
	}
	
	private static Double distance(MapPoint point1, MapPoint point2){
		int dx = Math.abs(point1.getX() - point2.getX());
		int dy = Math.abs(point1.getY() - point2.getY());
		int minD = Math.min(dx, dy);
		int maxD = Math.max(dx, dy);
		return (1.5 * minD) + maxD - minD;
	}
	
	/**
	 * –еализаци€ класа Binary Heap (дл€ быстрого получени€ меньшего элемента)
	 */
	private static class BinaryHeap<E extends Comparable<E>> {
		
		List<E> h = new ArrayList<E>();

		public void add(E node) {
		    h.add(node);
		    moveUp(h.size() - 1);
		}

		void moveUp(int pos) {
		    while (pos > 0) {
		    	int parent = (pos - 1) / 2;
		    	if (h.get(pos).compareTo(h.get(parent)) >= 0) {
		    		break;
		    	}
		    	Collections.swap(h, pos, parent);
		    	pos = parent;
		    }
		}

		public E remove() {
		    E removedNode = h.get(0);
		    E lastNode = h.remove(h.size() - 1);
		    if (!h.isEmpty()) {
		    	h.set(0, lastNode);
		    	moveDown(0);
		    }
		    return removedNode;
		}

		void moveDown(int pos) {
		    while (pos < h.size() / 2) {
		    	int child = 2 * pos + 1;
		    	if (child < h.size() - 1 && h.get(child).compareTo(h.get(child + 1)) > 0) {
		    		++child;
		    	}
		    	if (h.get(pos).compareTo(h.get(child)) <= 0) {
		    		break;
		    	}
		    	Collections.swap(h, pos, child);
		    	pos = child;
		    }
		}

		public boolean isEmpty(){
			return h.isEmpty();
		}
		  
		public boolean contains(E node){
			return h.contains(node);
		}
		  
		public void changeToBetter(E from, E to){
			int pos = h.indexOf(from);
			h.remove(pos);
			h.add(pos, to);
			moveUp(pos);
		}
	}
	
	private static class eMapPoint extends MapPoint implements Comparable<eMapPoint>{

		public Double f;
		public eMapPoint from; 
		
		public eMapPoint(MapPoint point, eMapPoint from, Double f){
			
			this.f = f;
			this.from = from;
			this.setX(point.getX());
			this.setY(point.getY());
		}

		@Override
		public int compareTo(eMapPoint node) {
			if (this.f > node.f) return 1;
			if (this.f < node.f) return -1;
			return 0; 
			
		}

	}
	
}
