package net.debs.fino.dnd;

import net.debs.fino.DebsMap;
import net.debs.fino.GameObject;
import net.debs.fino.MapObject;
import net.debs.fino.MapPoint;

/**
 * ���������� ����� ��� ����������� ����� ��������� �� �������� DnD
 * @author AAntonenko
 */
public class MapVisibility {

	private static DebsMap map = null;
	
	/**
	 * ���������� ����� �� �� ����� ����� ������
	 * @param map ����� �� ������� ����� ����������� ������ ���������
	 * @param p1 ����� �� ������� ����� �������� ���������
	 * @param p2 ����� � ������� ����� �������� ���������
	 * @return ture - ����� p2 ����� �� ����� p1; false - ����� p2 �� ����� �� ����� p1
	 */
	public static boolean see(DebsMap map, MapPoint p1, MapPoint p2) {
		
		MapVisibility.map = map;
		
		MapPoint MP1 = p1;
		MapPoint MP2 = p2;
		
		//���� MP1 ������ ��� MP2 - ������ �������
		if (MP1.getX() > MP2.getX()){
			MapPoint tmpMP = MP1;
			MP1 = MP2;
			MP2 = tmpMP;	
		}
				
		//���� MP1 � MP2 �� ����� � ��� �� ������ ��� �������
		if (MP1.getX() == MP2.getX()) return SeeSameX(MP1, MP2); 
		if (MP1.getY() == MP2.getY()) return SeeSameY(MP1, MP2); 
		
		//����������� dx � dy
		int dy = MP2.getY() - MP1.getY();
		
		//�������� ��������� ���� ����� ������ �������� �� ���� ����� ������� �������� (�������� ����� ����������� ��������� ��������� �� ���������)
		if (dy < 0){
			if (SeeMP1MP2(MP1, MP2, 0,-1,-1, 0))  return true; //11
			if (SeeMP1MP2(MP1, MP2, 0,-1, 0, 0))  return true; //12
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 0,-1,-1, 1))  return true; //13
			if (SeeMP1MP2(MP1, MP2, 0,-1, 0, 1))  return true; //14
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 1,-1,-1, 0))  return true; //21
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 1,-1, 0, 0))  return true; //22
			if ((CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() - 1)) && (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() + 1))) if (SeeMP1MP2(MP1, MP2, 1,-1,-1, 1))  return true; //23
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 1,-1, 0, 1))  return true; //24
			if (SeeMP1MP2(MP1, MP2, 0, 0,-1, 0))  return true; //31
			if (SeeMP1MP2(MP1, MP2, 0, 0, 0, 0))  return true; //32
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 0, 0,-1, 1))  return true; //33
			if (SeeMP1MP2(MP1, MP2, 0, 0, 0, 1))  return true; //34
			if (SeeMP1MP2(MP1, MP2, 1, 0,-1, 0))  return true; //41
			if (SeeMP1MP2(MP1, MP2, 1, 0, 0, 0))  return true; //42
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 1, 0,-1, 1))  return true; //43
			if (SeeMP1MP2(MP1, MP2, 1, 0, 0, 1))  return true; //44
		}
		else{
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 0, 0,-1,-1))  return true; //11
			if (SeeMP1MP2(MP1, MP2, 0, 0, 0,-1))  return true; //12
			if (SeeMP1MP2(MP1, MP2, 0, 0,-1, 0))  return true; //13
			if (SeeMP1MP2(MP1, MP2, 0, 0, 0, 0))  return true; //14
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 1, 0,-1,-1))  return true; //21
			if (SeeMP1MP2(MP1, MP2, 1, 0, 0,-1))  return true; //22
			if (SeeMP1MP2(MP1, MP2, 1, 0,-1, 0))  return true; //23
			if (SeeMP1MP2(MP1, MP2, 1, 0, 0, 0))  return true; //24
			if (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() - 1)) if (SeeMP1MP2(MP1, MP2, 0, 1,-1,-1))  return true; //31
			if (SeeMP1MP2(MP1, MP2, 0, 1, 0,-1))  return true; //32
			if (SeeMP1MP2(MP1, MP2, 0, 1,-1, 0))  return true; //33
			if (SeeMP1MP2(MP1, MP2, 0, 1, 0, 0))  return true; //34
			if ((CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() + 1)) && (CanSee(MP2.getX() - 1, MP2.getY()) || CanSee(MP2.getX(), MP2.getY() - 1))) if (SeeMP1MP2(MP1, MP2, 1, 1,-1,-1))  return true; //41
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 1, 1, 0,-1))  return true; //42
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 1, 1,-1, 0))  return true; //43
			if (CanSee(MP1.getX() + 1, MP1.getY()) || CanSee(MP1.getX(), MP1.getY() + 1)) if (SeeMP1MP2(MP1, MP2, 1, 1, 0, 0))  return true; //44
		}
		
		return false;
	}
	
	/**
	 * ����������� ��������� ���� ��� ��� ����� �� ����� ������
	 * @param MP1 ������ �����
	 * @param MP2 ������ �����
	 * @return ture - ����� MP2 ����� �� ����� MP1; false - ����� MP2 �� ����� �� ����� MP1
	 */
	private static boolean SeeSameX(MapPoint MP1, MapPoint MP2) {
			
		int yy = MP1.getY();
		int iyMax = MP2.getY();
		if (MP2.getY()<yy) {yy = MP2.getY(); iyMax = MP1.getY();}
		
		for(int iy=yy; iy<iyMax; iy++ ){
			if (!CanSee(MP1.getX(), iy)) return false;
		}
			
		return true;
	}

	/**
	 * ����������� ��������� ���� ��� ��� ����� �� ����� �������
	 * @param MP1 ������ �����
	 * @param MP2 ������ �����
	 * @return ture - ����� MP2 ����� �� ����� MP1; false - ����� MP2 �� ����� �� ����� MP1
	 */
	private static boolean SeeSameY(MapPoint MP1, MapPoint MP2) {
		
		for(int ix=MP1.getX()+1; ix<MP2.getX(); ix++ ){
			if (!CanSee(ix,MP2.getY())) return false;
		}
				
		return true;
	}
	
	/**
	 * ����������� ��������� ������ �������� �� ������� �������� �� ���������
	 * @param MP1 ������ �������
	 * @param MP2 ������ �������
	 * @param ddx1 �������� �� X ��� ������� ��������
	 * @param ddy1 �������� �� Y ��� ������� ��������
	 * @param ddx2 �������� �� X ��� ������� ��������
	 * @param ddy2 �������� �� Y ��� ������� ��������
	 * @return ture - ����� MP2 �� ��������� ����� �� ����� MP1 �� ���������; false - ����� MP2 �� ��������� �� ����� �� ����� MP1 �� ���������
	 */
	private static boolean SeeMP1MP2(MapPoint MP1, MapPoint MP2, int ddx1, int ddy1, int ddx2, int ddy2) {
		int rx1 = MP1.getX() + ddx1, ry1 = MP1.getY() + ddy1, rx2 = MP2.getX() + ddx2, ry2 = MP2.getY() + ddy2;
		if (rx1 > rx2) return false;
		else return SeeP1P2(rx1, ry1, rx2, ry2);
	}
	
	/**
	 * ����������� ��������� ����� ����� �� ������ (����������� ����� �� ���������� � ��������� ��� �� �� ��� ������ ����� ������� ������ ������)
	 * @param x1 ���������� x ������ �����
	 * @param y1 ���������� y ������ �����
	 * @param x2 ���������� x ������ �����
	 * @param y2 ���������� y ������ �����
	 * @return ture - ����� �����; false - �� �����
	 */
	private static boolean SeeP1P2(int x1, int y1, int x2, int y2) {
			
		int dx = x2 - x1, dy = y2 - y1;
		int ix = x1, iy = y1;
		int ddy = 1;
			
		int my1 = y1, my2 = y2; 
		if (dy < 0) {dy = -dy; ddy = -1; my1 = y2; my2 = y1;}
			
		dx++;
		dy++;
			
		// ������ �� X
		if (dx >= dy){
				
			int sdx = dx;
			while (ix <= x2){
				if (!CanSee(ix,iy)) return false;
				dx -= dy;
				if (dx<=0){
					if (dx == 0) if (ix != x2) if (!CanSee(ix+1,iy)) return false;
					iy+=ddy;
					if (iy < my1 || iy > my2) return true;
					if (!CanSee(ix,iy)) return false;
					
					dx += sdx;
				}
				ix++;
			}
		}
			
		// ������ �� Y
		else{
			int sdy = dy;
			if (y1 > y2){
				while (iy >= y2){
					if (!CanSee(ix,iy)) return false;
					dy -= dx;
					if (dy<=0){
						if (dy == 0) if (iy != y2) if (!CanSee(ix,iy-1)) return false;
						ix++;
						if (ix > x2) return true; 
						if (!CanSee(ix,iy)) return false;
						
						dy += sdy;
					}
					iy--;
				}
			}
			else
			{
				while (iy <= y2){
					if (!CanSee(ix,iy)) return false;
					dy -= dx;
					if (dy<=0){
						if (dy == 0) if (iy != y2) if (!CanSee(ix,iy+1)) return false;
						ix++;
						if (ix > x2) return true;
						if (!CanSee(ix,iy)) return false;
						
						dy += sdy;
					}
					iy++;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * ����������� �� ��������� �� ����� ��������� ������� �����
	 * @param x ���������� x ��������
	 * @param y ���������� y ��������
	 * @return ture - �� ���������; false - ���������
	 */
	private static boolean CanSee(int x, int y) {
		
		//��������� �������� ������������ �� ��������� �����������
		MapObject object = MapVisibility.map.getMapObject(new MapPoint(x, y));
		
		if (object == null) return true;
		
		//�������� ���� ����� �� ������ ����� ������ �� ��������� �����������
		if (object instanceof GameObject) {
			GameObject gameObject = (GameObject) object;
			return (Boolean) gameObject.getProperty("seeable");
		}
		
		return true;
	}
	
}
