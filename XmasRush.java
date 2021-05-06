import java.util.*;
import java.awt.Dialog.ModalityType;
import java.io.*;
import java.math.*;

/**
 * Help the Christmas elves fetch presents in a magical labyrinth!
 **/
class Player {
	static Joueur moi = null;
	static Joueur adversaire = null;
	static final int PUSH = 0;
	static final int MOVE = 1;
	static final int HAUT = 0;
	static final int DROITE = 1;
	static final int BAS = 2;
	static final int GAUCHE = 3;
	static String[][] mapPrecedente = new String[7][7];
	static int count = 0;

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		// game loop
		while (true) {
			String[][] map = new String[7][7];

			int turnType = loadMap(in, map);

			// debugMap(map);
			System.err.println("count " + count);
			// debugMap(mapPrecedente);
			loadJoueur(in);
			// debugJoueur();

			int numItems = in.nextInt(); // the total number of items available
											// on board and on player tiles
			List<Objet> myObjets = new ArrayList<>();
			List<Objet> adversaireObjets = new ArrayList<>();
			loadObjects(in, numItems, myObjets, adversaireObjets);
			// debugObjet(myObjets);
			// debugObjet(adversaireObjets);

			int numQuests = in.nextInt();
			List<String> myQuests = new ArrayList<>();
			List<String> adversaireQuests = new ArrayList<>();
			loadQuest(in, numQuests, myQuests, adversaireQuests);

			// Ordre myquest les plus proche :
			myObjets.sort((Objet o1, Objet o2) -> o1.getDistance() - o2.getDistance());
			// debugQuest(myQuests);
			// debugQuest(adversaireQuests);
			// System.err.println("Liste trié");
			// debugObjet(myObjets);
			Objet courant = null;
			for (Objet objet : myObjets) {
				if (myQuests.contains(objet.itemName)) {
					courant = objet;
				}
			}
			System.err.println(courant);
			String action = "";
			StringBuilder dir = new StringBuilder();
			int pos = moi.playerX;
			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			if (turnType == PUSH) {
				if (compareTableau(map, mapPrecedente)) {
					count++;
				} else {
					count = 0;
				}
				pos = gamePush(map, courant, dir);

				if (pos == -2 || (count > 2 && count % 2 == 0)) {
					System.out.println("PUSH " + moi.playerY + " RIGHT");
				} else if (pos == -3 || (count > 2 && count % 2 != 0)) {
					System.out.println("PUSH " + moi.playerX + " DOWN");
				} else if (dir.length() > 0) {
					action = "PUSH " + pos + " " + dir;
					System.out.println(action);
				} else {
					System.out.println("PUSH " + adversaire.playerY + " RIGHT");
				}
				for (int x = 0; x < map.length; x++) {
					for (int y = 0; y < map[x].length; y++) {
						mapPrecedente[x][y] = map[x][y];
					}
				}
			} else {

				gamePlayer(map, courant, dir);

				if (dir.length() > 0) {
					action = "MOVE " + dir;
				}

				if (action.isEmpty()) {
					System.out.println("PASS");
				} else {
					System.out.println(action);
				}
			}

		}
	}

	private static boolean compareTableau(String[][] map, String[][] mapPrecedente) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				// System.err.println("map[i][j] "+map[i][j]+ " =
				// mapPrecedente[i][j] " +mapPrecedente[i][j]);

				// System.err.println(map[i][j].equals(mapPrecedente[i][j]));

				if (!map[i][j].equals(mapPrecedente[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	private static void moveTitle(String[][] map, StringBuilder dir) {
		String maTuille = map[moi.playerY][moi.playerX];
		int posX = moi.playerX;
		int posY = moi.playerY;
		String sens = "";
		if (moi.playerX < 6 && maTuille.charAt(DROITE) == '1'
				&& map[moi.playerY][moi.playerX + 1].charAt(GAUCHE) == '1') {
			dir.append("RIGHT");
			sens = "RIGHT";
			posX++;
		} else if (moi.playerX > 0 && maTuille.charAt(GAUCHE) == '1'
				&& map[moi.playerY][moi.playerX - 1].charAt(DROITE) == '1') {
			dir.append("LEFT");
			sens = "LEFT";
			posX--;
		} else if (moi.playerY < 6 && maTuille.charAt(BAS) == '1'
				&& map[moi.playerY + 1][moi.playerX].charAt(HAUT) == '1') {
			dir.append("DOWN");
			sens = "DOWN";
			posY++;
		} else if (moi.playerY > 0 && maTuille.charAt(HAUT) == '1'
				&& map[moi.playerY - 1][moi.playerX].charAt(BAS) == '1') {
			dir.append("UP");
			sens = "UP";
			posY--;
		}

		if (dir.length() != 0) {
			moveTitle(map, dir, posX, posY, sens,0);
		}
	}

	private static void moveTitle(String[][] map, StringBuilder dir, int posX, int posY, String sens,int nbCoup) {
		String maTuille = map[posY][posX];
		StringBuilder dirNew = new StringBuilder();
		System.err.println("maTuille = "+maTuille);
		System.err.println("posX = "+posX+" posY = " + posY);
		System.err.println("dir = "+dir+" sens = " + sens);

		if (sens != "LEFT" && posX < 6 && maTuille.charAt(DROITE) == '1' && map[posY][posX + 1].charAt(GAUCHE) == '1') {
			dirNew.append(" RIGHT");
			sens = "RIGHT";
			posX++;
		} else if (sens != "RIGHT" && posX > 0 && maTuille.charAt(GAUCHE) == '1'
				&& map[posY][posX - 1].charAt(DROITE) == '1') {
			dirNew.append(" LEFT");
			sens = "LEFT";
			posX--;
		} else if (sens != "UP" && posY < 6 && maTuille.charAt(BAS) == '1' && map[posY + 1][posX].charAt(HAUT) == '1') {
			dirNew.append(" DOWN");
			sens = "DOWN";
			posY++;
		} else if (sens != "UP" && posY > 0 && maTuille.charAt(HAUT) == '1' && map[posY - 1][posX].charAt(BAS) == '1') {
			dirNew.append(" UP");
			sens = "UP";
			posY--;
		}
		
		if (nbCoup < 10 && dirNew.length() != 0) {
			nbCoup++;
			moveTitle(map, dir, posX, posY, sens,nbCoup);
		}
		dir.append(dirNew);
	}

	private static void gamePlayer(String[][] map, Objet courant, StringBuilder dir) {
		int posX = moi.playerX;
		int posY = moi.playerY;
		String maTuille = map[moi.playerY][moi.playerX];
		if (courant.itemX != moi.playerX) {
			int direction = courant.itemX - moi.playerX;
			System.err.println("direction X " + direction);
			System.err.println("Title playeur " + maTuille);
			System.err.println("pos X playeur " + moi.playerY);
			if (direction > 0) {
				if (moi.playerX < 6 && maTuille.charAt(DROITE) == '1'
						&& map[moi.playerY][moi.playerX + 1].charAt(GAUCHE) == '1') {
					dir.append("RIGHT");
					posX++;
				}
			} else {
				if (moi.playerX > 0 && maTuille.charAt(GAUCHE) == '1'
						&& map[moi.playerY][moi.playerX - 1].charAt(DROITE) == '1') {
					dir.append("LEFT");
					posX--;
				}
			}
		}

		if (dir.length() == 0 && courant.itemY != moi.playerY) {
			int direction = courant.itemY - moi.playerY;
			System.err.println("direction Y " + direction);
			System.err.println("Title playeur " + maTuille);
			System.err.println("pos Y playeur " + moi.playerY);
			if (direction > 0) {
				if (moi.playerY < 6 && maTuille.charAt(BAS) == '1'
						&& map[moi.playerY + 1][moi.playerX].charAt(HAUT) == '1') {
					dir.append("DOWN");
					posY++;
				}
			} else {
				if (moi.playerY > 0 && maTuille.charAt(HAUT) == '1'
						&& map[moi.playerY - 1][moi.playerX].charAt(BAS) == '1') {
					dir.append("UP");
					posY--;
				}
			}
		}
		if (dir.length() != 0) {
			gamePlayer(map, courant, dir, posX, posY);
		}
		if (dir.length() == 0) {
			moveTitle(map, dir);
		}
	}

	private static void gamePlayer(String[][] map, Objet courant, StringBuilder dir, int posX, int posY) {
		String maTuille = map[posX][posY];
		StringBuilder dirNew = new StringBuilder();
		if (courant.itemX != posX) {
			int direction = courant.itemX - posX;
			System.err.println("direction X " + direction);
			System.err.println("Title playeur " + maTuille);
			System.err.println("pos X playeur " + posY);
			if (direction > 0) {
				if (posX < 6 && maTuille.charAt(DROITE) == '1' && map[posY][posX + 1].charAt(GAUCHE) == '1') {
					dirNew.append(" RIGHT");
					posX++;
				}
			} else {
				if (posX > 0 && maTuille.charAt(GAUCHE) == '1' && map[posY][posX - 1].charAt(DROITE) == '1') {
					dirNew.append(" LEFT");
					posX--;
				}
			}
		}
		if (dirNew.length() == 0 && courant.itemY != posY) {
			int direction = courant.itemY - posY;
			System.err.println("direction Y " + direction);
			System.err.println("Title playeur " + maTuille);
			System.err.println("pos Y playeur " + posY);
			if (direction > 0) {
				if (posY < 6 && maTuille.charAt(BAS) == '1' && map[posY + 1][posX].charAt(HAUT) == '1') {
					dirNew.append(" DOWN");
					posY++;
				}
			} else {
				if (posY > 0 && maTuille.charAt(HAUT) == '1' && map[posY - 1][posX].charAt(BAS) == '1') {
					dirNew.append(" UP");
					posY--;
				}
			}
		}
		if (dirNew.length() != 0) {
			gamePlayer(map, courant, dirNew, posX, posY);
		}
		dir.append(dirNew);
	}

	private static int gamePush(String[][] map, Objet courant, StringBuilder dir) {
		int pos = moi.playerY;
		int directionX = courant.itemX - moi.playerX;
		int directionY = courant.itemY - moi.playerY;
		if (directionY != 0) {
			if (directionX > 0) {
				dir.append("RIGHT");
			} else {
				dir.append("LEFT");
			}
		}
		if (dir.length() == 0 && directionX != 0) {
			pos = moi.playerX;
			if (directionY > 0) {
				dir.append("DOWN");
			} else {
				dir.append("UP");
			}
		}
		if (dir.length() == 0 && directionX == 0) {
			pos = -2;
		} else if (dir.length() == 0 && directionY == 0) {
			pos = -3;
		}
		return pos;
	}

	private static void debugQuest(List<String> quests) {
		for (int i = 0; i < quests.size(); i++) {
			System.err.println(quests.get(i));
		}
	}

	private static void debugObjet(List<Objet> objets) {
		for (int i = 0; i < objets.size(); i++) {
			System.err.println(objets.get(i));
		}
	}

	private static void loadQuest(Scanner in, int numQuests, List<String> myQuest, List<String> adversaireQuest) {
		for (int i = 0; i < numQuests; i++) {
			String questItemName = in.next();
			int questPlayerId = in.nextInt();
			if (questPlayerId == 0) {
				myQuest.add(questItemName);
			} else {
				adversaireQuest.add(questItemName);
			}
		}
	}

	private static void loadObjects(Scanner in, int numItems, List<Objet> myObjet, List<Objet> adversaireObjet) {
		for (int i = 0; i < numItems; i++) {
			Objet Objet = new Objet();
			Objet.itemName = in.next();
			Objet.itemX = in.nextInt();
			Objet.itemY = in.nextInt();
			Objet.itemPlayerId = in.nextInt();

			if (Objet.itemPlayerId == 0) {
				myObjet.add(Objet);
				Objet.getDistanceJoueur(moi);
			} else {
				adversaireObjet.add(Objet);
				Objet.getDistanceJoueur(adversaire);
			}
		}
	}

	private static void debugJoueur() {
		System.err.println(moi);
		System.err.println(adversaire);
	}

	private static void debugMap(String[][] map) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				System.err.print(map[i][j] + "|");
			}
			System.err.println("");
		}
	}

	private static void loadJoueur(Scanner in) {
		for (int i = 0; i < 2; i++) {
			Joueur joueur = new Joueur();
			joueur.numPlayerCards = in.nextInt();
			joueur.playerX = in.nextInt();
			joueur.playerY = in.nextInt();
			joueur.playerTile = in.next();
			// System.err.println(joueur);
			if (i == 0) {
				moi = joueur;
			} else {
				adversaire = joueur;
			}
		}
	}

	private static int loadMap(Scanner in, String[][] map) {
		int turnType = in.nextInt();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				String tile = in.next();
				map[i][j] = tile;
				// System.err.print(map[i][j] + "|");
			}
			// System.err.println("");
		}
		return turnType;
	}

}

class Objet {
	String itemName;
	int itemX;
	int itemY;
	int itemPlayerId;
	int distance;

	@Override
	public String toString() {
		return "Objet [itemName=" + itemName + ", itemX=" + itemX + ", itemY=" + itemY + ", itemPlayerId="
				+ itemPlayerId + ", distance=" + distance + "]";
	}

	void getDistanceJoueur(Joueur joueur) {
		distance = (int) Math.sqrt(Math.pow(itemX - joueur.playerX, 2) + Math.pow(itemY - joueur.playerY, 2));
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}

class Joueur {
	int numPlayerCards;
	int playerX;
	int playerY;
	String playerTile;

	@Override
	public String toString() {
		return "Joueur [numPlayerCards=" + numPlayerCards + ", playerX=" + playerX + ", playerY=" + playerY
				+ ", playerTile=" + playerTile + "]";
	}
}