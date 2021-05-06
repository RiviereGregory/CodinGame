import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfCells = in.nextInt(); // 37
        List<Cells> cellList = new ArrayList<>();
        for (int i = 0; i < numberOfCells; i++) {
            int index = in.nextInt(); // 0 is the center cell, the next cells spiral outwards
            int richness = in.nextInt(); // 0 if the cell is unusable, 1-3 for usable cells
            int neigh0 = in.nextInt(); // the index of the neighbouring cell for each direction
            int neigh1 = in.nextInt();
            int neigh2 = in.nextInt();
            int neigh3 = in.nextInt();
            int neigh4 = in.nextInt();
            int neigh5 = in.nextInt();
            Cells cell = new Cells(index, richness, neigh0, neigh1, neigh2, neigh3, neigh4, neigh5);
            System.err.println(cell);
            cellList.add(cell);
        }


        // game loop
        while (true) {
            int day = in.nextInt(); // the game lasts 24 days: 0-23
            int nutrients = in.nextInt(); // the base score you gain from the next COMPLETE action
            int sun = in.nextInt(); // your sun points
            int score = in.nextInt(); // your current score
            int oppSun = in.nextInt(); // opponent's sun points
            int oppScore = in.nextInt(); // opponent's score
            boolean oppIsWaiting = in.nextInt() != 0; // whether your opponent is asleep until the next day
            int numberOfTrees = in.nextInt(); // the current amount of trees
            List<Tree> treeList = new ArrayList<>();
            for (int i = 0; i < numberOfTrees; i++) {
                int cellIndex = in.nextInt(); // location of this tree
                int size = in.nextInt(); // size of this tree: 0-3
                boolean isMine = in.nextInt() != 0; // 1 if this is your tree
                boolean isDormant = in.nextInt() != 0; // 1 if this tree is dormant
                Tree tree = new Tree(cellIndex, size, isMine, isDormant);
                System.err.println(tree);
                treeList.add(tree);
            }
            int numberOfPossibleActions = in.nextInt(); // all legal actions
            if (in.hasNextLine()) {
                in.nextLine();
            }
            List<String> actionPossibles = new ArrayList<>();
            for (int i = 0; i < numberOfPossibleActions; i++) {
                String possibleAction = in.nextLine(); // try printing something from here to start with
                if (!"WAIT".equals(possibleAction)) {
                    actionPossibles.add(possibleAction);
                }
            }
            List<Tree> trees = treeList.stream().filter(tree -> tree.isMine).collect(Collectors.toList());
            int max = 0;
            String message = "WAIT";
            List<Tree> trees3 = trees.stream().filter(tree -> tree.size == 3).collect(Collectors.toList());
            if (trees3.isEmpty()) {
                for (Tree tree : trees) {
                    int temp = tree.size;
                    if (temp > max) {
                        max = temp;
                        message = "GROW " + tree.cellIndex;
                    }

                }
            } else {
                for (Tree tree : trees3) {
                    int temp = cellList.stream().filter(cell -> cell.index == tree.cellIndex).findFirst().get().getRichness();
                    if (temp > max) {
                        max = temp;
                        message = "COMPLETE " + tree.cellIndex;
                    }
                }
            }


            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // GROW cellIdx | SEED sourceIdx targetIdx | COMPLETE cellIdx | WAIT <message>
            System.out.println(message);
        }
    }
}


class Tree {
    int cellIndex; // location of this tree
    int size; // size of this tree: 0-3
    boolean isMine; // 1 if this is your tree
    boolean isDormant;

    public Tree(int cellIndex, int size, boolean isMine, boolean isDormant) {
        this.cellIndex = cellIndex;
        this.size = size;
        this.isMine = isMine;
        this.isDormant = isDormant;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "cellIndex=" + cellIndex +
                ", size=" + size +
                ", isMine=" + isMine +
                ", isDormant=" + isDormant +
                '}';
    }
}

class Cells {
    int index; // 0 is the center cell, the next cells spiral outwards
    int richness; // 0 if the cell is unusable, 1-3 for usable cells
    int neigh0; // the index of the neighbouring cell for each direction
    int neigh1;
    int neigh2;
    int neigh3;
    int neigh4;
    int neigh5;

    public Cells(int index, int richness, int neigh0, int neigh1, int neigh2, int neigh3, int neigh4, int neigh5) {
        this.index = index;
        this.richness = richness;
        this.neigh0 = neigh0;
        this.neigh1 = neigh1;
        this.neigh2 = neigh2;
        this.neigh3 = neigh3;
        this.neigh4 = neigh4;
        this.neigh5 = neigh5;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "index=" + index +
                ", richness=" + richness +
                ", neigh0=" + neigh0 +
                ", neigh1=" + neigh1 +
                ", neigh2=" + neigh2 +
                ", neigh3=" + neigh3 +
                ", neigh4=" + neigh4 +
                ", neigh5=" + neigh5 +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public int getRichness() {
        return richness;
    }
}