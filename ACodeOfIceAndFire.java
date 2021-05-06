
import java.util.*;
import java.io.*;
import java.math.*;

class Player {

	public static void main(String args[], Iterator<Pac> iterator) {
		Scanner in = new Scanner(System.in);

		Game g = new Game();
		g.init(in);

		// game loop
		while (true) {

			g.update(in);
			// g.debug();
			g.buildOutput();
			g.output();
		}
	}
}

class Game {

	List<Unit> units;

	List<Building> buildings;

	int gold, income;

	List<Command> output;

	public Game() {
		units = new ArrayList<Unit>();
		buildings = new ArrayList<Building>();
		output = new ArrayList<Command>();
	}

	// not useful in Wood 3
	public void init(Scanner in) {
		int numberMineSpots = in.nextInt();
		for (int i = 0; i < numberMineSpots; i++) {
			int x = in.nextInt();
			int y = in.nextInt();
		}
	}

	public void update(Scanner in) {

		units.clear();
		buildings.clear();
		output.clear();

		// READ TURN INPUT
		gold = in.nextInt();
		int income = in.nextInt();
		int opponentGold = in.nextInt();
		int opponentIncome = in.nextInt();
		for (int i = 0; i < 12; i++) {
			String line = in.next();
			for (int j = 0; j < line.length(); j++) {
				MapWorld.map[i][j] = line.charAt(j);
			}
			MapWorld.print();
		}
		int buildingCount = in.nextInt();
		for (int i = 0; i < buildingCount; i++) {
			int owner = in.nextInt();
			int buildingType = in.nextInt();
			int x = in.nextInt();
			int y = in.nextInt();
			buildings.add(new Building(x, y, buildingType, owner));
		}
		int unitCount = in.nextInt();
		for (int i = 0; i < unitCount; i++) {
			int owner = in.nextInt();
			int unitId = in.nextInt();
			int level = in.nextInt();
			int x = in.nextInt();
			int y = in.nextInt();
			units.add(new Unit(x, y, unitId, level, owner));
		}
	}

	public void buildOutput() {
		// @TODO "core" of the AI
		trainUnits();

		moveUnits();
	}

	private void trainUnits() {
		Position trainingPosition = findTrainingPosition();
		long count = units.stream().filter(u -> u.isOwned()).count();
		if (count == 0) {
			output.add(new Command(CommandType.TRAIN, 1, trainingPosition));
		}

		// if (count < 4 && gold > 50) {
		// output.add(new Command(CommandType.TRAIN, 2, trainingPosition));
		// }
		if (count < 2 && gold > 100) {
			output.add(new Command(CommandType.TRAIN, 3, trainingPosition));
		}
	}

	// move to the center
	private void moveUnits() {
		units.stream().filter(u -> u.isOwned())
				.forEach(myUnit -> output.add(new Command(CommandType.MOVE, myUnit.id, findMovePosition(myUnit))));
	}

	// train near the HQ for now
	private Position findTrainingPosition() {
		Building HQ = getHQ();
		if (HQ.p.x == 0) {
			return new Position(0, 1);
		}
		return new Position(11, 10);
	}

	private Position findMovePosition(Unit unit) {
		boolean notFind = true;
		Building HQ = getHQ();
		if (unit.level > 1) {
			if (HQ.p.x == 0) {
				for (int i = 0; i < MapWorld.map.length; i++) {
					for (int j = 0; j < MapWorld.map.length; j++) {
						char position = MapWorld.map[i][j];
						if (position == 'X' || position == 'x') {
							return new Position(j, i);
						}
					}
				}
			} else {
				for (int i = MapWorld.map.length - 1; i > 0; i--) {
					for (int j = MapWorld.map.length - 1; j > 0; j--) {
						char position = MapWorld.map[i][j];
						if (position == 'X' || position == 'x') {
							return new Position(j, i);
						}
					}
				}
			}
		} else {
			while (notFind) {
				int i = new Random().nextInt(11);
				int j = new Random().nextInt(11);
				char position = MapWorld.map[i][j];
				if (position == '.' || position == 'X' || position == 'x') {
					return new Position(j, i);
				}
			}
		}
		return new Position(11, 10);
	}

	public void output() {
		StringBuilder s = new StringBuilder("");
		if (output.isEmpty()) {
			s.append("WAIT;");
		}
		output.stream().forEach(c -> s.append(c.get()));
		System.out.println(s);
	}

	public void debug() {
		units.stream().forEach(u -> u.debug());
		buildings.stream().forEach(b -> b.debug());
	}

	private Building getHQ() {
		return buildings.stream().filter(b -> b.isHQ() && b.isOwned()).findFirst().get();
	}

	private Building getOpponentHQ() {
		return buildings.stream().filter(b -> b.isHQ() && !b.isOwned()).findFirst().get();
	}
}

class Command {

	CommandType t;

	Position p;

	int idOrLevel;

	public Command(CommandType t, int idOrLevel, Position p) {
		this.t = t;
		this.p = p;
		this.idOrLevel = idOrLevel;
	}

	public String get() {
		return t.toString() + " " + idOrLevel + " " + p.x + " " + p.y + ";";
	}
}

class Position {

	int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Unit {

	Position p;

	int level;

	int owner;

	int id;

	public Unit(int x, int y, int id, int level, int owner) {
		this.p = new Position(x, y);
		this.id = id;
		this.level = level;
		this.owner = owner;
	}

	public void debug() {
		System.err.println("unit of level " + level + " at " + p.x + " " + p.y + " owned by " + owner);
	}

	public boolean isOwned() {
		return owner == 0;
	}
}

class Building {

	Position p;

	BuildingType t;

	int owner;

	public Building(int x, int y, int t, int owner) {
		this.p = new Position(x, y);
		this.t = BuildingType.get(t);
		this.owner = owner;
	}

	public void debug() {
		System.err.println(t + " at " + p.x + " " + p.y + " owned by " + owner);
	}

	public boolean isHQ() {
		return t.equals(BuildingType.HQ);
	}

	public boolean isOwned() {
		return owner == 0;
	}

}

class MapWorld {
	public static char map[][] = new char[12][12];

	public static void print() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.err.print(map[i][j]);
			}
			System.err.println("");
		}
	}
}

enum BuildingType {

	HQ;

	static public BuildingType get(int type) {
		switch (type) {
		case 0:
			return HQ;
		}
		return null;
	}
}

enum CommandType {

	MOVE, TRAIN;
}
