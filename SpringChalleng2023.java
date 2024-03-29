import java.util.*;
import java.util.stream.Collectors;

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
            List<Cell> cellsResourcesNeighbors = new ArrayList<>();

            int numberMyAnts = 0;
            for (int i = 0; i < numberOfCells; i++) {
                int resources = in.nextInt(); // the current amount of eggs/crystals on this cell
                int myAnts = in.nextInt(); // the amount of your ants on this cell
                int oppAnts = in.nextInt(); // the amount of opponent ants on this cell
                cells.get(i).resources = resources;
                cells.get(i).myAnts = myAnts;
                cells.get(i).oppAnts = oppAnts;
                numberMyAnts += myAnts;
            }

            Map<Integer, List<Cell>> hmap = new HashMap<>();

            for (int i = 0; i < myBases.size(); i++) {
                cellsResourcesNeighbors = new ArrayList<>();
                List<Integer> neighborsRest = new ArrayList<>();
                List<Integer> neighborsVist = new ArrayList<>();
                int myCellBase = myBases.get(i);
                neighborsRest.addAll(cells.get(myCellBase).neighbors.stream().filter(voi -> voi != -1).collect(Collectors.toList()));
                while (!neighborsRest.isEmpty()) {
                    Cell cell = cells.get(neighborsRest.get(0));
                    neighborsRest.remove(0);
                    boolean isOppAntBase = oppBases.contains(cell.index);
                    if (!isOppAntBase && cell.cellType != 0 && cell.resources != 0 && !neighborsVist.contains(cell.index)) {
                        cellsResourcesNeighbors.add(cell);
                        //System.err.println("Add resources voisin" + cell);
                    }
                    neighborsVist.add(cell.index);
                    neighborsRest.addAll(cells.get(cell.index).neighbors.stream().filter(voi -> (voi != -1 && !neighborsVist.contains(voi))).collect(Collectors.toList()));
                }
                hmap.put(i, cellsResourcesNeighbors);
            }

            int strength = 1;
            for (Map.Entry entry : hmap.entrySet()) {
                cellsResourcesNeighbors = (List<Cell>) entry.getValue();
                int myCellBase = myBases.get((int) entry.getKey());
                if (!cellsResourcesNeighbors.isEmpty()) {
                    int size = Math.min(cellsResourcesNeighbors.size(), numberMyAnts / (numberOfCells / 8) + 1);
                    for (int i = 0; i < size; i++) {
                        //System.err.println("Add LINE");
                        if (stringBuilder == null) {
                            System.err.println("Add StringBuilder");
                            stringBuilder = new StringBuilder("LINE " + cellsResourcesNeighbors.get(i).index + " " + myCellBase + " " + strength);
                        } else {
                            stringBuilder.append(";LINE " + cellsResourcesNeighbors.get(i).index + " " + myCellBase + " " + strength);
                        }
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
