package net.debs.fino.dnd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���������� ����� ���������� ������� ������� �� �������� D20
 * @author AAntonenko
 */
public class D20 {

	private static Pattern patternDice = Pattern.compile("[0-9]*d[0-9]+");
	private static Pattern patternNumber = Pattern.compile("[0-9]+");
	
	//���� ����������
	public static void main(String [] args){
		for (int i = 0; i < 50; i++) {
			System.out.println(D20.Exec("100d3"));
		}
	}
	
	/**
	 * ���������� ������� ������� D20 (� ������� ����� ����������� �����, ����� "+","-" � ����������� ���� "d2", "2d4" � �.�.)
	 * @param s ������� ��� ����������
	 * @return ��������� ���������� �������
	 */
	public static Integer Exec(String s){
		
		String tmp = new String(s);

		Matcher matcher = patternDice.matcher(s);
		
		while (matcher.find()){
			String dices = matcher.group(0);
			
			int dIndex = dices.indexOf("d");
			
			int diceNum = 1;
			
			if (dIndex > 0) diceNum = Integer.parseInt(dices.substring(0, dIndex));
			int dice = Integer.parseInt(dices.substring(dIndex + 1, dices.length()));
			
			int res = 0;
			for (int i = 1; i <= diceNum; i++) {
				res += d(dice);
			}
			
			tmp = tmp.replaceFirst(dices, String.valueOf(res));
		}

		matcher = patternNumber.matcher(tmp);
		
		int res = 0;
		int prevEnd = 0;
		while (matcher.find()){
			
			String sign = tmp.substring(prevEnd, matcher.start());
			sign = sign.replace(" ", "");

			if ((sign.equals("+")) || (sign.isEmpty())) {
				res += Integer.valueOf(matcher.group(0));
			}
			else if (sign.equals("-")) {
				res -= Integer.valueOf(matcher.group(0));
			}
			else{
				System.err.println("D20:Exec: Error in formula: '" + s + "' unknown symbol (" + sign + ") between terms");
			}
			
			prevEnd = matcher.end() + 1;
			
		}
		
		return res;
	}

	/**
	 * ���������� ��������� ������ ������ � �������� ����������� ������
	 * @param d ���������� ������ ������
	 * @return��������� ������
	 */
	public static Integer d(int d){
		return (int)(d * Math.random() + 1);
	}
	
}
