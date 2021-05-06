import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Survive the wrath of Kutulu Coded fearlessly by JohnnyYuge & nmahoude (ok we
 * might have been a bit scared by the old god...but don't say anything)
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt();
		int height = in.nextInt();
		Carte carteLab = new Carte(height, width);
		if (in.hasNextLine()) {
			in.nextLine();
		}
		for (int y = 0; y < height; y++) {
			String line = in.nextLine();
			for (int x = 0; x < line.length(); x++) {
				carteLab.addValue(x, y, line.charAt(x));
			}
		}
		carteLab.toPrint();
		int sanityLossLonely = in.nextInt(); // how much sanity you lose every
												// turn when alone, always 3
												// until wood 1
		int sanityLossGroup = in.nextInt(); // how much sanity you lose every
											// turn when near another player,
											// always 1 until wood 1
		int wandererSpawnTime = in.nextInt(); // how many turns the wanderer
												// take to spawn, always 3 until
												// wood 1
		int wandererLifeTime = in.nextInt(); // how many turns the wanderer is
												// on map after spawning, always
												// 40 until wood 1
		int loop = 0;

		// game loop
		while (true) {
			loop++;
			int entityCount = in.nextInt(); // the first given entity
											// corresponds to your explorer
			List<Unit> explorers = new ArrayList<>();
			List<Unit> planExplorers = new ArrayList<>();
			List<Unit> lightExplorers = new ArrayList<>();
			List<Unit> wanderers = new ArrayList<>();
			Unit myUnit = new Unit();
			myUnit = affectationUnit(in, entityCount, explorers, planExplorers, lightExplorers, wanderers, myUnit);
			if (loop == 20 || loop == 40) {
				System.out.println("PLAN");
			} else {
				deplacement(width, height, carteLab, explorers, planExplorers, lightExplorers, wanderers, myUnit);
			}

		}
	}

	private static void deplacement(int width, int height, Carte carteLab, List<Unit> explorers,
			List<Unit> planExplorers, List<Unit> lightExplorers, List<Unit> wanderers, Unit myUnit) {
		int nbEplorer = explorers.size() + planExplorers.size() + lightExplorers.size();
		if (nbEplorer > 1) {
			Position positionDeplacement = new Position();
			Carte carteTmp = carteLab.copy();
			// carteTmp.toPrint();
			int minDistance = 1000;
			if (explorers.size() > 1) {
				positionDeplacement.x = explorers.get(1).x;
				positionDeplacement.y = explorers.get(1).y;
			}

			Unit unitProche = unitProche(explorers, myUnit, carteTmp, false);
			positionDeplacement.x = unitProche.x;
			positionDeplacement.y = unitProche.y;

			for (Unit unitPlan : planExplorers) {
				if (unitPlan.id != myUnit.id) {
					positionDeplacement.x = unitPlan.x;
					positionDeplacement.y = unitPlan.y;
				}
			}
			Unit unitEnnemy = unitProche(wanderers, myUnit, carteTmp, true);
			// carteTmp.toPrint();
			if (unitEnnemy != null && myUnit.distance(unitEnnemy) < 2) {
				positionDeplacementParRapportEnnemy(width, height, carteTmp, myUnit, positionDeplacement, unitEnnemy,
						unitProche);
			}
			System.err.println("carte positionDeplacement.x positionDeplacement.x "
					+ carteLab.getValue(positionDeplacement.x, positionDeplacement.y));
			if (Light.effetLight == 0) {
				Light.effetLight = 5;
			}
			if (Light.effetLight != 5) {
				Light.effetLight--;
			}
			System.err.println("nbLight " + Light.nbLight);
			System.err.println("effetLight " + Light.effetLight);

			System.out.println("MOVE " + positionDeplacement.x + " " + positionDeplacement.y);
		} else {
			System.out.println("WAIT");
		}
	}

	private static void positionDeplacementParRapportEnnemy(int width, int height, Carte carteLab, Unit myUnit,
			Position positionDeplacement, Unit unitEnnemy, Unit unitProche) {
		System.err.println("Ennemy < 2");
		System.err.println(myUnit.x + " " + myUnit.y + " - " + unitEnnemy.x + " " + unitEnnemy.y);
		boolean isNotUnitProche = true;
		if ((myUnit.x + 1) < height && carteLab.getValue(myUnit.x + 1, myUnit.y) != '#'
				&& carteLab.getValue(myUnit.x + 1, myUnit.y) != 'E') {
			positionDeplacement.x = myUnit.x + 1;
			positionDeplacement.y = myUnit.y;
			isNotUnitProche = isNotUnitProche(positionDeplacement, unitProche, isNotUnitProche);
		}
		if (isNotUnitProche && (myUnit.y + 1) < width && carteLab.getValue(myUnit.x, myUnit.y + 1) != '#'
				&& carteLab.getValue(myUnit.x, myUnit.y + 1) != 'E') {
			positionDeplacement.x = myUnit.x;
			positionDeplacement.y = myUnit.y + 1;
			isNotUnitProche = isNotUnitProche(positionDeplacement, unitProche, isNotUnitProche);
		}
		if (isNotUnitProche && myUnit.x > 0 && carteLab.getValue(myUnit.x - 1, myUnit.y) != '#'
				&& carteLab.getValue(myUnit.x - 1, myUnit.y) != 'E') {
			positionDeplacement.x = myUnit.x - 1;
			positionDeplacement.y = myUnit.y;
			isNotUnitProche = isNotUnitProche(positionDeplacement, unitProche, isNotUnitProche);
		}
		if (isNotUnitProche && myUnit.y > 0 && carteLab.getValue(myUnit.x, myUnit.y - 1) != '#'
				&& carteLab.getValue(myUnit.x, myUnit.y - 1) != 'E') {
			positionDeplacement.x = myUnit.x;
			positionDeplacement.y = myUnit.y - 1;
			isNotUnitProche = isNotUnitProche(positionDeplacement, unitProche, isNotUnitProche);
		}
	}

	private static boolean isNotUnitProche(Position positionDeplacement, Unit unitProche, boolean isNotUnitProche) {
		if (unitProche.x == positionDeplacement.x && unitProche.y == positionDeplacement.y) {
			isNotUnitProche = false;
		}
		return isNotUnitProche;
	}

	private static Unit unitProche(List<Unit> units, Unit myUnit, Carte carteLab, boolean isEnnemy) {
		int minDistance = 1000;
		Unit unitProche = null;
		for (Unit unitExplo : units) {
			if (isEnnemy) {
				carteLab.addValue(unitExplo.x, unitExplo.y, 'E');
			}
			if (unitExplo.id != myUnit.id && myUnit.distance(unitExplo) < minDistance) {
				minDistance = myUnit.distance(unitExplo);
				unitProche = unitExplo;
			}
		}
		return unitProche;
	}

	private static Unit affectationUnit(Scanner in, int entityCount, List<Unit> explorers, List<Unit> planExplorers,
			List<Unit> lightExplorers, List<Unit> wanderers, Unit myUnit) {
		boolean isMyUnit = true;
		for (int i = 0; i < entityCount; i++) {
			Unit unit = new Unit();
			unit.entityType = in.next();
			unit.id = in.nextInt();
			unit.x = in.nextInt();
			unit.y = in.nextInt();
			unit.param0 = in.nextInt();
			unit.param1 = in.nextInt();
			unit.param2 = in.nextInt();

			if ("EXPLORER".equals(unit.entityType)) {
				explorers.add(unit);
				if (isMyUnit) {
					myUnit = unit;
					isMyUnit = false;
				}
			} else if ("EFFECT_PLAN".equals(unit.entityType)) {
				planExplorers.add(unit);
				if (isMyUnit) {
					myUnit = unit;
					isMyUnit = false;
				}
			} else if ("EFFECT_LIGHT".equals(unit.entityType)) {
				lightExplorers.add(unit);
				if (isMyUnit) {
					myUnit = unit;
					isMyUnit = false;
				}
			} else {
				wanderers.add(unit);
			}
		}
		return myUnit;
	}
}

class Light {
	static int effetLight = 5;
	static int nbLight = 3;
}

class Position {
	int x;
	int y;
}

class Unit {
	String entityType;
	int id;
	int x;
	int y;
	int param0;
	int param1;
	int param2;

	int distance(Unit unitOther) {
		return Math.abs(x - unitOther.x) + Math.abs(y - unitOther.y);
	}

}

class Carte {
	private char[][] carteJeu;
	private int height;
	private int width;

	Carte(int height, int width) {
		this.height = height;
		this.width = width;
		carteJeu = new char[width][height];
	}

	char[][] getCarteJeu() {
		return this.carteJeu;
	}

	void addValue(int x, int y, char value) {
		this.carteJeu[x][y] = value;
	}

	char getValue(int x, int y) {
		return this.carteJeu[x][y];
	}

	Carte copy() {
		Carte carteCopy = new Carte(height, width);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				carteCopy.addValue(x, y, this.getValue(x, y));
			}
		}
		return carteCopy;
	}

	void toPrint() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.err.print(this.getValue(x, y));
			}
			System.err.println();
		}
	}
}