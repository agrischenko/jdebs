package net.debs.fino;

import java.util.Vector;

public class Path {
	
	private Vector<MapPoint> points = null;
	private Integer distanceCache = null;

	public Path(){
		this.points = new Vector<MapPoint>();
	}
	
	public void add(MapPoint point){
		points.add(point);
	}
	
	public MapPoint get(int i){
		return points.get(i);
	}
	
	/**
	 * ��������� �������� �� ���� �� �������� ��������� �� ������ 
	 * @param distance ���������� �� ������ ����
	 * @return ������� �� ����
	 */
	public MapPoint getMapPointByDistance(int distance){
		if (this.distance() <= distance) return points.get(points.size() - 1);
		
		double dist = 0;
		
		MapPoint start = get(0);
		MapPoint point = start;
		
		for (int i = 1; i < this.size(); i++) {
			
			//���� ��� �� ��������� - �� ���������� 1.5, ����� 1
			if ((this.get(i - 1).getX() != this.get(i).getX()) && (this.get(i - 1).getY() != this.get(i).getY())) dist+=1.5;
			else dist++;
			
			if ((int)dist > distance) return point;
			
			point = get(i);
		}
		return point;
	}
	
	/*
	 * ���������� ��������� � ����
	 */
	public Integer size(){
		return points.size();
	}
	
	/*
	 * ���������� ����
	 */
	public Integer distance(){
		if (this.distanceCache == null){
			double dist = 0.0;
			for (int i = 1; i < this.size(); i++) {
				//���� ��� �� ��������� - �� ���������� 1.5, ����� 1
				if ((this.get(i - 1).getX() != this.get(i).getX()) && (this.get(i - 1).getY() != this.get(i).getY())) dist+=1.5;
				else dist++;
			}
			this.distanceCache = (int) dist;
		}
		return this.distanceCache;
	}

	@Override
	public String toString() {
		String pathstr = "";
		for (int i = 1; i < this.size(); i++) {
			if (!pathstr.equals("")) pathstr += ", ";
			pathstr += "(" + points.get(i) + ")";
		}
		return "(" + pathstr + ")";
	}
	
}
