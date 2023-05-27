import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        List<Cell> cells = new ArrayList<>();
        int numberOfCells = in.nextInt(); // amount of hexagonal cells in this map
        for (int i = 0; i < numberOfCells; i++) {
            int type = in.nextInt(); // 0 for empty, 1 for eggs, 2 for crystal
            int initialResources = in.nextInt(); // the initial amount of eggs/crystals on this cell
            // the index of the neighbouring cell for each direction
            List<Integer> neighbors = new ArrayList<>();
            neighbors.add(in.nextInt());
            neighbors.add(in.nextInt());
            neighbors.add(in.nextInt());
            neighbors.add(in.nextInt());
            neighbors.add(in.nextInt());
            neighbors.add(in.nextInt());
            Cell cell = new Cell(i, type, initialResources, neighbors, 0, 0);
            System.err.println(cell);
            cells.add(cell);
        }
        int numberOfBases = in.nextInt();
        List<Integer> myBases = new ArrayList<>();
        for (int i = 0; i < numberOfBases; i++) {
            myBases.add(in.nextInt());
        }
        List<Integer> oppBases = new ArrayList<>();
        for (int i = 0; i < numberOfBases; i++) {
            oppBases.add(in.nextInt());
        }

        // game loop
        while (true) {
            StringBuilder stringBuilder = null;
            int nextCell = myBases.get(0);
            for (int i = 0; i < numberOfCells; i++) {
                int resources = in.nextInt(); // the current amount of eggs/crystals on this cell
                int myAnts = in.nextInt(); // the amount of your ants on this cell
                int oppAnts = in.nextInt(); // the amount of opponent ants on this cell
                cells.get(i).resources = resources;
                cells.get(i).myAnts = myAnts;
                cells.get(i).oppAnts = oppAnts;
                System.err.println(cells.get(i));
                System.err.println("Types = " + cells.get(i).cellType + " resources = " + resources);
                boolean isOppAntBase = oppBases.contains(i);
                if (!isOppAntBase && cells.get(i).cellType == 2 && resources != 0) {
                    if (stringBuilder == null) {
                        stringBuilder = new StringBuilder("LINE " + nextCell + " " + i + " 1");
                        nextCell = i;
                    } else {
                        stringBuilder.append(";LINE " + nextCell + " " + i + " 1");
                        nextCell = i;
                    }
                }
            }
            // WAIT | LINE <sourceIdx> <targetIdx> <strength> | BEACON <cellIdx> <strength> | MESSAGE <text>
            if (stringBuilder == null) {
                System.out.println("WAIT");
            } else {
                System.out.println(stringBuilder.toString());
            }
        }
    }
}


class Cell {
    int index;
    int cellType;
    int resources;
    List<Integer> neighbors;
    int myAnts;
    int oppAnts;

    public Cell(int index, int cellType, int resources, List<Integer> neighbors, int myAnts, int oppAnts) {
        this.index = index;
        this.cellType = cellType;
        this.resources = resources;
        this.neighbors = neighbors;
        this.myAnts = myAnts;
        this.oppAnts = oppAnts;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "index=" + index +
                ", cellType=" + cellType +
                ", resources=" + resources +
                ", neighbors=" + neighbors +
                ", myAnts=" + myAnts +
                ", oppAnts=" + oppAnts +
                '}';
    }
}
