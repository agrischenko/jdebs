package net.debs.fino.script;

import java.util.Random;

public class LuaRandom {
	
	private static Random rand;
	
	public static void main(String [] args){
				
		for (int i = 0; i < 100; i++) {
			System.out.println(random(-1, 1));
		}
		
	}
	
	public static double random(){
		return LuaRandom.getRand().nextDouble();
	}
	
	public static int random(int i){
		return (int) ((i + 1) * LuaRandom.random());
	}
	
	public static int random(int i, int j){
		int min = Math.min(i, j);
		int max = Math.max(i, j);
		int res = (int) (min + LuaRandom.random(max - min));
		return res;
	}
	
	private static Random getRand(){
		if (rand == null){
			LuaRandom.rand = new Random(); 
		}
		return LuaRandom.rand;
	}
	
}
