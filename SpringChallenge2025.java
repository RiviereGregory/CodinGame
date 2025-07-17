import java.util.*;

class Player {
    static final int SIZE = 3;
    static final int MOD = 1 << 30;
    static int total = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int depth = in.nextInt();
        int[][] board = new int[SIZE][SIZE];
        int empty = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = in.nextInt();
                if (board[i][j] == 0) empty++;
            }
        }
        simulate(board, depth, empty);
        System.out.println(total);
    }

    static void simulate(int[][] board, int depth, int empty) {
        if (depth == 0 || empty == 0) {
            total = (int) ((total + computeHash(board)) % MOD);
            return;
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0) continue;

                List<int[]> neighbors = getNeighbors(board, i, j);

                if (neighbors.size() >= 2) {
                    // Générer toutes les combinaisons valides de captures
                    List<List<int[]>> combinations = new ArrayList<>();
                    generateCombinations(neighbors, 0, new ArrayList<>(), combinations);

                    boolean hasCapture = false;
                    for (List<int[]> combo : combinations) {
                        int sum = 0;
                        for (int[] n : combo) sum += board[n[0]][n[1]];
                        if (sum > 0 && sum <= 6) {
                            hasCapture = true;
                            int[][] newBoard = copy(board);
                            for (int[] n : combo) newBoard[n[0]][n[1]] = 0;
                            newBoard[i][j] = sum;
                            simulate(newBoard, depth - 1, empty - combo.size());
                        }
                    }

                    if (hasCapture) continue; // capture est obligatoire
                }

                // Aucun combo valide → placement sans capture
                int[][] newBoard = copy(board);
                newBoard[i][j] = 1;
                simulate(newBoard, depth - 1, empty - 1);
            }
        }
    }

    static List<int[]> getNeighbors(int[][] board, int x, int y) {
        int[] dx = {-1, 1, 0, 0}, dy = {0, 0, -1, 1};
        List<int[]> result = new ArrayList<>();
        for (int d = 0; d < 4; d++) {
            int nx = x + dx[d], ny = y + dy[d];
            if (0 <= nx && nx < SIZE && 0 <= ny && ny < SIZE && board[nx][ny] > 0)
                result.add(new int[]{nx, ny});
        }
        return result;
    }

    static void generateCombinations(List<int[]> elements, int index, List<int[]> current, List<List<int[]>> result) {
        if (current.size() >= 2)
            result.add(new ArrayList<>(current));
        for (int i = index; i < elements.size(); i++) {
            current.add(elements.get(i));
            generateCombinations(elements, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    static int computeHash(int[][] board) {
        long hash = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                hash = hash * 10 + board[i][j];
        return (int) (hash % MOD);
    }

    static int[][] copy(int[][] board) {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
        return newBoard;
    }
}
