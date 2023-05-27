import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


class Player {
    enum CardType {
        TRAINING, CODING, DAILY_ROUTINE, TASK_PRIORITIZATION, ARCHITECTURE_STUDY, CONTINUOUS_INTEGRATION, CODE_REVIEW, REFACTORING, BONUS, TECHNICAL_DEBT
    }

    private static int ZONES_COUNT = 8;
    static final String MOVE_PHASE = "MOVE";
    static final String GIVE_PHASE = "GIVE_CARD";
    static final String THROW_PHASE = "THROW_CARD";
    static final String PLAY_PHASE = "PLAY_CARD";
    static final String RELEASE_PHASE = "RELEASE";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        class Application {
            public int id;
            public int[] resources = new int[8];

            public Application(String data) {
                String[] applicationDetails = data.split(" ");
                id = Integer.parseInt(applicationDetails[1]);
                for (int resourceId = 2; resourceId < applicationDetails.length; ++resourceId) {
                    resources[resourceId - 2] = Integer.parseInt(applicationDetails[resourceId]);
                }
            }

            public int nbCarteManque(int[] myHands) {
                int nbCarteManque = 0;
                System.err.print(id + "//");
                for (int resourceId = 0; resourceId < resources.length; resourceId++) {
                    System.err.print(resourceId + "- " + myHands[resourceId] + " " + resources[resourceId] + " * ");
                    if (myHands[resourceId] != resources[resourceId] && resources[resourceId] > myHands[resourceId]) {
                        nbCarteManque += resources[resourceId] - myHands[resourceId];
                    }
                }
                System.err.println("");
                return nbCarteManque;
            }

            public List<Integer> positionCarteManque(int[] myHands) {
                List<Integer> positions = new ArrayList<>();
                for (int resourceId = 0; resourceId < resources.length; resourceId++) {
                    if (resources[resourceId] > myHands[resourceId]) {
                        positions.add(resourceId);
                    }
                }
                return positions;
            }
        }


        while (true) {
            //read game phase
            String gamePhase = scanner.nextLine();

            //read applications details
            int applicationsCount = Integer.parseInt(scanner.nextLine());
            Application[] applications = new Application[applicationsCount];
            for (int i = 0; i < applicationsCount; ++i) {
                String applicationData = scanner.nextLine();
                applications[i] = new Application(applicationData);
            }

            //read players details
            String[] playerDetails = scanner.nextLine().split(" ");
            int myLocation = Integer.parseInt(playerDetails[0]);
            int myScore = Integer.parseInt(playerDetails[1]);
            int myPermanentDailyRoutineCards = Integer.parseInt(playerDetails[2]);
            int myPermanentArchitectureStudyCards = Integer.parseInt(playerDetails[3]);

            playerDetails = scanner.nextLine().split(" ");
            int opponentLocation = Integer.parseInt(playerDetails[0]);
            int opponentScore = Integer.parseInt(playerDetails[1]);
            int opponentPermanentDailyRoutineCards = Integer.parseInt(playerDetails[2]);
            int opponentPermanentArchitectureStudyCards = Integer.parseInt(playerDetails[3]);


            //read player cards
            int[] myCardsInHand = new int[10];
            int[] myDrawPile = new int[10];
            int[] myDiscardPile = new int[10];
            int[] myAutomatedCards = new int[10];

            int cardLocationsCount = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < cardLocationsCount; i++) {
                String cardsData = scanner.nextLine();
                System.err.println(cardsData); //cards details
                String[] cardsDetails = cardsData.split(" ");
                int[] cards = new int[10];
                String cardsLocation = cardsDetails[0]; // the location of the card list. It can be HAND, DRAW, DISCARD or OPPONENT_CARDS (AUTOMATED and OPPONENT_AUTOMATED will appear in later leagues)
                if (cardsLocation.equals("HAND")) {
                    cards = myCardsInHand;
                } else if (cardsLocation.equals("DRAW")) {
                    cards = myDrawPile;
                } else if (cardsLocation.equals("DISCARD")) {
                    cards = myDiscardPile;
                } else if (cardsLocation.equals("AUTOMATED")) {
                    cards = myAutomatedCards;
                }
                for (int j = 1; j < cardsDetails.length; ++j) {
                    cards[j - 1] = Integer.parseInt(cardsDetails[j]);
                }
            }
            //read possible moves
            int movesCount = Integer.parseInt(scanner.nextLine());
            String[] moves = new String[movesCount];
            for (int i = 0; i < movesCount; ++i) {
                moves[i] = scanner.nextLine();
            }
            int release = applications[0].id;
            int minCarte = applications[0].nbCarteManque(myCardsInHand);
            int idRelease = 0;
            for (int i = 1; i < applicationsCount; ++i) {
                if (minCarte > applications[i].nbCarteManque(myCardsInHand)) {
                    minCarte = applications[i].nbCarteManque(myCardsInHand);
                    release = applications[i].id;
                    idRelease = i;
                }
            }
            List<Integer> positionCarteManque = applications[idRelease].positionCarteManque(myCardsInHand);
            Optional<Integer> first = positionCarteManque.stream().filter(i -> i > myLocation).findFirst();
            int moveId = myLocation + 1 > 7 ? 0 : myLocation + 1;
            if (!positionCarteManque.isEmpty()) {
                moveId = first.orElse(positionCarteManque.get(0));
            }

            switch (gamePhase) {
                case MOVE_PHASE:

                    System.out.println("MOVE " + moveId);
                    break;
                case GIVE_PHASE:
                    //will appear in Wood 1 League
                    System.out.println("RANDOM");
                    break;
                case THROW_PHASE:
                    //will appear in Bronze League
                    System.out.println("RANDOM");
                    break;
                case PLAY_PHASE:
                    //will appear in Wood 1 League
                    System.out.println("RANDOM");
                    break;
                case RELEASE_PHASE:
                    System.out.println("RELEASE " + release);

                    break;
                default:
                    System.out.println("RANDOM");
                    break;
            }
        }
    }
}
