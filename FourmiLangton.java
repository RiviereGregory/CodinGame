import java.util.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int W = in.nextInt();
		int H = in.nextInt();
		int x = in.nextInt();
		int y = in.nextInt();
		String direction = in.next();
		int T = in.nextInt();
		if (in.hasNextLine()) {
			in.nextLine();
		}
		System.err.println("W " + W + " H " + H + " x " + x + " y " + y + " T " + T + " Dire " + direction);
		char[][] plateau = initPlateau(in, W, H);

		FourmiLangton fourmi = new FourmiLangton(x, y, H, W, direction, plateau);
		for (int i = 0; i < T; i++) {
			nouvellePosition(fourmi);
		}

		resultat(W, H, plateau);
	}

	private static void resultat(int W, int H, char[][] plateau) {
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				System.out.print(plateau[i][j]);
			}
			System.out.println("");

		}
	}

	private static char[][] initPlateau(Scanner in, int W, int H) {
		char[][] plateau = new char[H][W];
		for (int i = 0; i < H; i++) {
			String C = in.nextLine();
			for (int j = 0; j < W; j++) {
				plateau[i][j] = C.charAt(j);
			}
		}
		return plateau;
	}

	public static void nouvellePosition(FourmiLangton fourmi) {
		if (fourmi.getCouleur() == '.') {
			fourmi.plateau[fourmi.x][fourmi.y] = '#';
			fourmi.tourneGauche();
		} else {
			fourmi.plateau[fourmi.x][fourmi.y] = '.';
			fourmi.tourneDroite();
		}
	}
}

class FourmiLangton {
	int x;
	int y;
	int limiteX;
	int limiteY;
	String direction;
	char[][] plateau;

	public FourmiLangton(int x, int y, int limiteX, int limiteY, String direction, char[][] plateau) {
		super();
		this.x = x;
		this.y = y;
		this.limiteX = limiteX;
		this.limiteY = limiteY;
		this.direction = direction;
		this.plateau = plateau;
	}

	public char getCouleur() {
		return plateau[x][y];
	}
	
	public void tourneDroite() {
		switch (direction) {
		case "N":
			y++;
			direction = "E";
			break;
		case "E":
			x++;
			direction = "S";
			break;
		case "S":
			y--;
			direction = "W";
			break;
		case "W":
			x--;
			direction = "N";
			break;

		default:
			break;
		}
	}
	
	public void tourneGauche() {
		switch (direction) {
		case "N":
			y--;
			direction = "W";
			break;
		case "W":
			x++;
			direction = "S";
			break;
		case "S":
			y++;
			direction = "E";
			break;
		case "E":
			x--;
			direction = "N";
			break;

		default:
			break;
		}
	}

}
