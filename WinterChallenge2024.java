import java.util.*;
import java.io.*;
import java.math.*;

class Pos {
    final int x;
    final int y;

    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Organ {
    int id;
    int owner;
    int parentId;
    int rootId;
    Pos pos;
    String organType;
    String dir;

    Organ(int id, int owner, int parentId, int rootId, Pos pos, String organType, String dir) {
        this.id = id;
        this.owner = owner;
        this.parentId = parentId;
        this.rootId = rootId;
        this.pos = pos;
        this.organType = organType;
        this.dir = dir;
    }
}

class Cell {
    Pos pos;
    boolean isWall;
    String protein;
    Organ organ;

    Cell(Pos pos, boolean isWall, String protein, Organ organ) {
        this.pos = pos;
        this.isWall = isWall;
        this.protein = protein;
        this.organ = organ;
    }

    Cell(Pos pos) {
        this(pos, false, null, null);
    }
}

class Grid {
    Cell[] cells;
    int width, height;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width * height];
        reset();
    }

    void reset() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x + width * y] = new Cell(new Pos(x, y));
            }
        }
    }

    Cell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return cells[x + width * y];
        }
        return null;
    }

    Cell getCell(Pos pos) {
        return getCell(pos.x, pos.y);
    }

    void setCell(Pos pos, Cell cell) {
        cells[pos.x + width * pos.y] = cell;
    }
}

class Game {
    Grid grid;
    Map<String, Integer> myProteins;
    Map<String, Integer> oppProteins;
    List<Organ> myOrgans;
    List<Organ> oppOrgans;
    Map<Integer, Organ> organMap;

    Game(int width, int height) {
        grid = new Grid(width, height);
        myProteins = new HashMap<>();
        oppProteins = new HashMap<>();
        myOrgans = new ArrayList<>();
        oppOrgans = new ArrayList<>();
        organMap = new HashMap<>();
    }

    void reset() {
        grid.reset();
        myOrgans.clear();
        oppOrgans.clear();
        organMap.clear();
    }
}

/**
 * Grow and multiply your organisms to end up larger than your opponent.
 **/
class Player {
    // Protein types
    static final String A = "A";
    static final String B = "B";
    static final String C = "C";
    static final String D = "D";

    static final String WALL = "WALL";

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // columns in the game grid
        int height = in.nextInt(); // rows in the game grid
        System.err.println("width " + width + " height " + height);
        Game game = new Game(width, height);
        Cell myLastOrgan = null;

        // game loop
        while (true) {
            game.reset();

            int entityCount = in.nextInt();
            for (int i = 0; i < entityCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt(); // grid coordinate
                String type = in.next(); // WALL, ROOT, BASIC, TENTACLE, HARVESTER, SPORER, A, B, C, D
                int owner = in.nextInt(); // 1 if your organ, 0 if enemy organ, -1 if neither
                int organId = in.nextInt(); // id of this entity if it's an organ, 0 otherwise
                String organDir = in.next(); // N,E,S,W or X if not an organ
                int organParentId = in.nextInt();
                int organRootId = in.nextInt();

                Pos pos = new Pos(x, y);
                Cell cell = null;
                if (type.equals(WALL)) {
                    cell = new Cell(pos, true, null, null);
                } else if (Arrays.asList(A, B, C, D).contains(type)) {
                    cell = new Cell(pos, false, type, null);
                } else {
                    Organ organ = new Organ(organId, owner, organParentId, organRootId, pos, type, organDir);
                    cell = new Cell(pos, false, null, organ);
                    if (owner == 1) {
                        game.myOrgans.add(organ);
                        myLastOrgan = cell;
                    } else {
                        game.oppOrgans.add(organ);
                    }
                    game.organMap.put(organId, organ);
                }

                if (cell != null) {
                    game.grid.setCell(pos, cell);
                }
            }
            int myA = in.nextInt();
            int myB = in.nextInt();
            int myC = in.nextInt();
            int myD = in.nextInt(); // your protein stock
            int oppA = in.nextInt();
            int oppB = in.nextInt();
            int oppC = in.nextInt();
            int oppD = in.nextInt(); // opponent's protein stock
            game.myProteins.put(A, myA);
            game.myProteins.put(B, myB);
            game.myProteins.put(C, myC);
            game.myProteins.put(D, myD);
            game.oppProteins.put(A, oppA);
            game.oppProteins.put(B, oppB);
            game.oppProteins.put(C, oppC);
            game.oppProteins.put(D, oppD);
            int requiredActionsCount = in.nextInt(); // your number of organisms, output an action for each one in any order
            for (int i = 0; i < requiredActionsCount; i++) {

                Cell cell = selectCellForGrow(game, myLastOrgan);
                if (myLastOrgan != null && cell != null && game.myProteins.get(A) > 0) {
                    System.out.println("GROW " + myLastOrgan.organ.id + " " + cell.pos.x + " " + cell.pos.y + " BASIC");
                } else {
                    System.out.println("WAIT");
                }
            }
        }


    }

    private static Cell selectCellForGrow(Game game, Cell myLastOrgan) {
        Pos myPos = myLastOrgan.pos;
        Cell cell = null;
        for (Cardinal card : Cardinal.values()) {
            Pos pos = getPos(game.grid.width, game.grid.height, card, myPos);
            if (pos == null ) continue;
            cell = game.grid.getCell(pos.x, pos.y);
            if (cell.isWall) {
                continue;
            } else if (cell.organ != null && cell.organ.owner != -1) {
                continue;
            }
            break;
        }

        return cell;
    }

    private static Pos getPos(int limitX, int limitY, Cardinal cardinal, Pos myPos) {
        int x = myPos.x;
        int y = myPos.y;
        switch (cardinal) {
            case N -> {
                if ((y - 1) >= 0) {
                    y = y - 1;
                } else return null;
            }
            case NE -> {
                if ((y - 1) >= 0) {
                    y = y - 1;
                } else return null;
                if ((x + 1) < limitX) {
                    x = x + 1;
                } else return null;
            }
            case E -> {
                if ((x + 1) < limitX) {
                    x = x + 1;
                } else return null;
            }
            case SE -> {
                if ((y + 1) < limitY) {
                    y = y + 1;
                } else return null;
                if ((x + 1) < limitX) {
                    x = x + 1;
                } else return null;
            }
            case S -> {
                if ((y + 1) < limitY) {
                    y = y + 1;
                } else return null;
            }
            case SW -> {
                if ((y + 1) < limitY) {
                    y = y + 1;
                } else return null;
                if ((x - 1) >= 0) {
                    x = x - 1;
                } else return null;
            }
            case W -> {
                if ((x - 1) >= 0) {
                    x = x - 1;
                } else return null;
            }
            case NW -> {
                if ((y - 1) >= 0) {
                    y = y - 1;
                } else return null;
                if ((x - 1) >= 0) {
                    x = x - 1;
                } else return null;
            }
        }

        return new Pos(x, y);
    }

    public enum Cardinal {
        N, NE, E, SE, S, SW, W, NW
    }
}