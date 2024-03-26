/**
 * Pattern detection
 */

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class Solution {

    /**
     * @param n Le nombre de bâtiments
     * @param buildingMap La représentation des n bâtiments
     * @return La hauteur de chaque bâtiment
     */
    public static List<Integer> buildingHeights(int n, List<String> buildingMap) {
        List<Integer> buildingSizes = new ArrayList<Integer>();
        for (int i=0 ; i<n; i++){
            buildingSizes.add(buildingMap.get(i).trim().length());
        }

        return buildingSizes;
    }

    /* Ignore and do not change the code below */
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * Try a solution
     * @param output La hauteur de chaque bâtiment
     */
    public static void trySolution(List<Integer> output) {
        System.out.println("" + gson.toJson(output));
    }

    public static void main(String args[]) {
        try (Scanner in = new Scanner(System.in)) {
            trySolution(buildingHeights(
                    gson.fromJson(in.nextLine(), new TypeToken<Integer>(){}.getType()),
                    gson.fromJson(in.nextLine(), new TypeToken<List<String>>(){}.getType())
            ));
        }
    }
    /* Ignore and do not change the code above */
}

/***********************************************************************
***********************************************************************
***********************************************************************
***********************************************************************
***********************************************************************
*/
/**
 * Pixel Rendering
 */

import java.util.*;
        import java.io.*;
        import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    final static String BLANC = ".";
    final static String NOIR = "#";
    public static void main(String args[]) {


        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        String[][] tab1 = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tab1[i][j] = BLANC;
            }
        }

        // game loop
        while (true) {
            String command = in.nextLine();
            System.err.println("command "+command);
            String[] commands = command.split("\\s+");
            System.err.println("longueur "+commands.length);
            String type = commands[0];
            System.err.println("type "+type);
            System.err.println(" coord "+commands[1]);
            int coord = Integer.parseInt(commands[1]);
            System.err.println(" coord "+coord);
            if ("C".equalsIgnoreCase(type)){
                addNoirCols(tab1, coord);
            } else{
                addBlancLine(tab1, coord);
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(tab1[i][j]);
                }
                System.out.println();
            }
        }
    }

    private static void addBlancLine(String[][] tab1, int coord) {
        for (int j = 0; j < tab1[coord].length; j++) {
            tab1[coord][j] = BLANC;
        }
    }

    private static void addNoirCols(String[][] tab1, int coord) {
        for (int i = 0; i < tab1[0].length; i++) {
            tab1[i][coord] = NOIR;
        }
    }
}


/***********************************************************************
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************
 */

/**
 * Pixel Rending
 */
-- SQL request(s)​​​​​​‌​‌​‌‌‌​‌​​‌‌​‌​​​‌‌​​‌‌​ below
        SELECT pu.NEWCOLOR color, COUNT(*) count
        FROM pixels px
        INNER JOIN pixelupdates pu ON px.id = pu.pixelId
        where not (pu.UPDATEDAT = px.FIRSTPAINTEDAT)
        group by pu.NEWCOLOR
        order by count desc

/***********************************************************************
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************
 */