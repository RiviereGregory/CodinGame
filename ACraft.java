import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		char[][] map = new char[10][19];
		List<Robot> robots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			String line = in.next();
			for (int j = 0; j < line.length(); j++) {
				map[i][j] = line.charAt(j);
			}
			System.err.println(map[i]);
		}
		int robotCount = in.nextInt();
		for (int i = 0; i < robotCount; i++) {
			Robot robot = new Robot();
			robot.x = in.nextInt();
			robot.y = in.nextInt();
			robot.direction = in.next();
			robots.add(robot);
			System.err.println(robot);
		}

		// Write an action using System.out.println()
		// To debug: System.err.println("Debug messages...");

		System.out.println(getSortie(map, robots));
	}

	private static String getSortie(char[][] map, List<Robot> robots) {
		StringBuilder builder = new StringBuilder();
		for (Robot robot : robots) {
			builder.append(getMur(map, robot));
		}

		return builder.toString();
	}

	private static String getMur(char[][] map, Robot robot) {
		int x = robot.x;
		int y = robot.y;
		int valeurX = 0;
		int valeurY = 0;
		String direction = "D";
		System.err.println("y = "+y+" x = "+x);
		System.err.println("map = "+map[y][x]);
		while (map[y][x] != '#') {
		    System.err.println("map = "+map[y][x]);
		    valeurX = x;
			valeurY = y;
			switch (robot.direction) {
			case "D":
				y++;
				direction = "U";
				break;

			case "L":
				x--;
				direction = "R";
				break;
			case "R":
				x++;
				direction = "L";
				break;
			case "U":
				y--;
				direction = "D";
				break;
			}
			System.err.println("y = "+y+" x = "+x);
			if (y < 0 || x < 0 || y > 9 || x > 18) {
				break;
			}

		}

		return " "+valeurX+" "+valeurY+" "+direction;
	}

}

class Robot {
	int x;
	int y;
	String direction;

	@Override
	public String toString() {
		return "Robot position " + x + "-" + y + " direction " + direction;
	}
}
