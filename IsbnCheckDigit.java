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
		int N = in.nextInt();
		if (in.hasNextLine()) {
			in.nextLine();
		}
		List<String> ISBN = new ArrayList<>();
		List<String> ISBNErreurs = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			ISBN.add(in.nextLine());
			System.err.println(ISBN.get(i));
		}

		checkerreurISBN(ISBNErreurs, ISBN);

		/*
		*/
		// Write an action using System.out.println()
		// To debug: System.err.println("Debug messages...");

		System.out.println(ISBNErreurs.size() + " invalid:");
		for (String sortie : ISBNErreurs) {
			System.out.println(sortie);
		}
	}

	private static void checkerreurISBN(List<String> ISBNErreurs, List<String> ISBN) {
		for (String element : ISBN) {
			System.err.println("element = " + element);
			if (element.length() != 10 && element.length() != 13) {
				System.err.println("length = " + element.length());
				ISBNErreurs.add(element);
			} else if (element.length() == 10) {
				if (!checkISBN10(element)) {
					ISBNErreurs.add(element);
				}
			} else {
				if (!checkISBN13(element)) {
					ISBNErreurs.add(element);
				}
			}

		}
	}

	private static boolean checkISBN10(String iSBN10) {
		int total = 0;
		int dernierChiffre = iSBN10.charAt(9) - '0';
		for (int i = 0; i < 9; i++) {
			int charAt = iSBN10.charAt(i) - '0';
			total += charAt * (10 - i);
			// System.err.println("charAt = " + charAt);
		}
		System.err.println("total = " + total);
		int modulo = total % 11;
		System.err.println("modulo = " + modulo);
		int dernierChiffreCalcul = modulo == 0 ? 0 : 11 - modulo;
		dernierChiffreCalcul = dernierChiffreCalcul == 10 ? 'X'-'0':dernierChiffreCalcul;
		System.err.println("dernierChiffre = " + dernierChiffre);
		System.err.println("dernierChiffreCalcul = " + dernierChiffreCalcul);
		// System.err.println(dernierChiffreCalcul==dernierChiffre);
		return dernierChiffreCalcul == dernierChiffre;
	}

	private static boolean checkISBN13(String iSBN13) {

		int total = 0;
		int dernierChiffre = iSBN13.charAt(12) - '0';
		for (int i = 0; i < 12; i += 2) {
			int charAt = iSBN13.charAt(i) - '0';
			total += charAt;
		}
		for (int i = 1; i < 12; i += 2) {
			int charAt = iSBN13.charAt(i) - '0';
			total += charAt * 3;
		}
		System.err.println("total = " + total);
		int modulo = total % 10;
		System.err.println("modulo = " + modulo);
		int dernierChiffreCalcul = modulo == 0 ? 0 : 10 - modulo;
		System.err.println("dernierChiffre = " + dernierChiffre);
		System.err.println("dernierChiffreCalcul = " + dernierChiffreCalcul);
		// System.err.println(dernierChiffreCalcul==dernierChiffre);
		return dernierChiffreCalcul == dernierChiffre;
	}
}