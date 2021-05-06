import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

/**
 * Grab the pellets as fast as you can!
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt(); // size of the grid
		int height = in.nextInt(); // top left corner is (x=0, y=0)
		if (in.hasNextLine()) {
			in.nextLine();
		}
		char grid[][] = new char[height][width];
		for (int i = 0; i < height; i++) {
			String row = in.nextLine(); // one line of the grid: space " " is
										// floor, pound "#" is wall
			for (int j = 0; j < width; j++) {
				grid[i][j] = row.charAt(j);
				System.err.print(grid[i][j]);
			}
			System.err.println();
		}
		List<Pac> pacsMine = new ArrayList<Pac>();
		// game loop
		while (true) {
			int myScore = in.nextInt();
			int opponentScore = in.nextInt();
			int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs
												// in sight
			List<Integer> pacIds = new ArrayList<Integer>();
			for (int i = 0; i < visiblePacCount; i++) {
				int pacId = in.nextInt(); // pac number (unique within a team)
				boolean mine = in.nextInt() != 0; // true if this pac is yours
				int x = in.nextInt(); // position in the grid
				int y = in.nextInt(); // position in the grid
				String typeId = in.next(); // unused in wood leagues
				int speedTurnsLeft = in.nextInt(); // unused in wood leagues
				int abilityCooldown = in.nextInt(); // unused in wood leagues
				if (mine && !pacsMine.stream().filter(p -> p.pacId == pacId).findFirst().isPresent()) {
					pacIds.add(pacId);
					pacsMine.add(new Pac(pacId, mine, x, y, typeId, speedTurnsLeft, abilityCooldown));
				} else if (mine) {
					pacIds.add(pacId);
					pacsMine.stream().filter(p -> p.pacId == pacId).findFirst().get().update(pacId, mine, x, y, typeId,
							speedTurnsLeft, abilityCooldown);
				}
			}
			Iterator<Pac> iterator = pacsMine.iterator();
			while (iterator.hasNext()) {
				Pac pac = iterator.next();
				if (!pacIds.contains(pac.pacId)) {
					iterator.remove();
				}
			}
			int visiblePelletCount = in.nextInt(); // all pellets in sight
			List<Pellet> pellets = new ArrayList<Pellet>();
			for (int i = 0; i < visiblePelletCount; i++) {
				int x = in.nextInt();
				int y = in.nextInt();
				int value = in.nextInt(); // amount of points this pellet is
											// worth
				pellets.add(new Pellet(x, y, value));
			}

			StringBuilder action = new StringBuilder();

			for (int i = 0; i < pacsMine.size(); i++) {
				Pac pac = pacsMine.get(i);
				Boolean pelletExist = pellets.stream().filter(p -> p.x == pac.moveX && p.y == pac.moveY).findFirst()
						.isPresent();
				if (pac.moveX == -1 || !pelletExist || (pac.x == pac.moveX && pac.y == pac.moveY)
						|| (pac.x == pac.xI && pac.y == pac.yI)) {
					System.err.println("Change : " + pac.pacId);
					System.err.println("pac.moveX : " + pac.moveX);
					System.err.println("pelletExist : " + pelletExist);

					if (pellets.size() > 0) {
						int hPellet = (int) (Math.random() * pellets.size());
						Pellet myPellet = pellets.get(hPellet);
						List<Pellet> pelletsMax = pellets.stream().filter(p -> p.value != 1)
								.collect(Collectors.toList());
						if (!pelletsMax.isEmpty() && pelletsMax.size() > i && pelletsMax.get(i).x != pac.moveX
								&& pelletsMax.get(i).y != pac.moveY) {
							myPellet = pelletsMax.get(i);
						}
						pac.moveX = myPellet.x;
						pac.moveY = myPellet.y;
					}
				}
				pac.xI = pac.x;
				pac.yI = pac.y;
				action.append("MOVE " + pac.pacId + " " + pac.moveX + " " + pac.moveY);
				if (i < pacsMine.size() - 1) {
					action.append("|");
				}
			}

			System.out.println(action.toString());
		}
	}
}

class Pac {

	int pacId;
	boolean mine;
	int x;
	int y;
	String typeId;
	int speedTurnsLeft;
	int abilityCooldown;
	int moveX = -1;
	int moveY = -1;
	int xI = -1;
	int yI = -1;

	public Pac(int pacId, boolean mine, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {
		this.pacId = pacId;
		this.mine = mine;
		this.x = x;
		this.y = y;
		this.typeId = typeId;
		this.speedTurnsLeft = speedTurnsLeft;
		this.abilityCooldown = abilityCooldown;
	}

	public void update(int pacId, boolean mine, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {
		this.pacId = pacId;
		this.mine = mine;
		this.x = x;
		this.y = y;
		this.typeId = typeId;
		this.speedTurnsLeft = speedTurnsLeft;
		this.abilityCooldown = abilityCooldown;
	}

}

class Pellet {

	int x;
	int y;
	int value;

	public Pellet(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

}
