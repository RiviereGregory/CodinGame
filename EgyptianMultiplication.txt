import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int a = in.nextInt();
		int b = in.nextInt();
		System.err.println("a " + a);
		System.err.println("b " + b);
		int multi;
		int number;
		if (a < b) {
			multi = a;
			number = b;
		} else {
			multi = b;
			number = a;
		}

		System.out.println(number + " * " + multi);
		String resultFin = "";
		int result = 0;
		while (true) {
			if (multi == 0) {
				System.out.println("= " + result);
				break;
			}
			switch (multi % 2) {
			case 0:
				multi = multi / 2;
				number = 2 * number;
				affiche(resultFin, number, multi);			
				break;
			case 1:
				if (resultFin.isEmpty()) {
					result += number;
					resultFin += number;
				} else {
					resultFin += " + " + number;
					result += number;
				}
				multi = multi - 1;
				affiche(resultFin, number, multi);
				break;
			default:
				break;
			}			
		}
	}
	static public void affiche(String resultFin, int numberTmp, int multiTmp){
		if (resultFin.isEmpty()) {
			System.out.println("= " + numberTmp + " * " + multiTmp);
		} else {
			System.out.println("= " + numberTmp + " * " + multiTmp + " + " + resultFin);
		}
	}
}
