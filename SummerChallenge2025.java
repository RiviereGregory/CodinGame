import java.util.*;

/**
 * Win the water fight by controlling the most territory, or out-soak your opponent!
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myId = in.nextInt(); // Your player id (0 or 1)
        int agentDataCount = in.nextInt(); // Total number of agents in the game
        List<Agent> myAgents = new ArrayList<>();
        List<Agent> ourAgents = new ArrayList<>();
        for (int i = 0; i < agentDataCount; i++) {
            int agentId = in.nextInt(); // Unique identifier for this agent
            int player = in.nextInt(); // Player id of this agent
            int shootCooldown = in.nextInt(); // Number of turns between each of this agent's shots
            int optimalRange = in.nextInt(); // Maximum manhattan distance for greatest damage output
            int soakingPower = in.nextInt(); // Damage output within optimal conditions
            int splashBombs = in.nextInt(); // Number of splash bombs this can throw this game
            Agent agent = new Agent(agentId, player, shootCooldown, optimalRange, soakingPower, splashBombs);
            if (myId == player) {
                myAgents.add(agent);
            } else {
                ourAgents.add(agent);
            }

        }
        int width = in.nextInt(); // Width of the game map
        int height = in.nextInt(); // Height of the game map
        int map[][] = new int[width][height];
        List<Pos> protection = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = in.nextInt(); // X coordinate, 0 is left edge
                int y = in.nextInt(); // Y coordinate, 0 is top edge
                int tileType = in.nextInt();
                if (tileType > 1) {
                    protection.add(new Pos(x, y));
                }
                map[x][y] = tileType;
            }
        }

        // game loop
        while (true) {
            int agentCount = in.nextInt(); // Total number of agents still in the game
            System.err.println("agentCount " + agentCount);
            List<Agent> myAgentsLive = new ArrayList<>();
            List<Agent> ourAgentsLive = new ArrayList<>();
            for (int i = 0; i < agentCount; i++) {
                int agentId = in.nextInt();
                // System.err.println("agentId " + agentId);
                int x = in.nextInt();
                int y = in.nextInt();
                // System.err.println("x " + x + ",y " + y);
                int cooldown = in.nextInt(); // Number of turns before this agent can shoot
                int splashBombs = in.nextInt();
                int wetness = in.nextInt(); // Damage (0-100) this agent has taken
                Optional<Agent> first = myAgents.stream().filter(agent -> agent.agentId == agentId).map(agent -> {
                    agent.x = x;
                    agent.y = y;
                    agent.shootCooldown = cooldown;
                    agent.splashBombs = splashBombs;
                    agent.soakingPower = wetness;
                    // System.err.println("Myagent " + agent);
                    return agent;
                }).findFirst();
                first.ifPresent(myAgentsLive::add);

                Optional<Agent> ourfirst = ourAgents.stream().filter(agent -> agent.agentId == agentId).map(agent -> {
                    agent.x = x;
                    agent.y = y;
                    agent.shootCooldown = cooldown;
                    agent.splashBombs = splashBombs;
                    agent.soakingPower = wetness;
                    // System.err.println("Ouragent " + agent);
                    return agent;
                }).findFirst();
                ourfirst.ifPresent(ourAgentsLive::add);

            }

            int myAgentCount = in.nextInt(); // Number of alive agents controlled by you
            System.err.println("myAgentCount " + myAgentCount);
            System.err.println("ourAgentsLive size " + ourAgentsLive.size());
            System.err.println("myAgentsLive size " + myAgentsLive.size());
            for (int i = 0; i < myAgentCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                Agent agent = myAgentsLive.get(i);
                System.err.println("agent " + agent);

                // One line per agent: <agentId>;<action1;action2;...> actions are "MOVE x y | SHOOT id | THROW x y | HUNKER_DOWN | MESSAGE text"
                String move = agent.agentId + "; MOVE 6 1";
                String move1 = agent.agentId + "; MOVE 6 3";
                if (!protection.isEmpty()) {
                    Optional<Pos> posPlusPres =
                            protection.stream().min(Comparator.comparing(element -> Distance(element.x, element.y, agent.x, agent.y)));
                    Pos pos = getPos(posPlusPres.get().x, posPlusPres.get().y, width, height);
                    int x = pos.x;
                    int y = pos.y;
                    System.err.println("x " + posPlusPres.get().x + ",y " + posPlusPres.get().y);
                    System.err.println("Pour agent " + agent.agentId + " x " + agent.x + ",y " + agent.y);

                    if (!ourAgentsLive.isEmpty()) {
                        Set<Agent> ourAgentNotFree = new HashSet<>();
                        for (Agent agentOur : ourAgentsLive) {
                            for (Agent agentMy : myAgentsLive) {
                                if (((agentOur.x - 3 < agentMy.x) && (agentOur.x + 3 > agentMy.x) &&
                                        (agentOur.y - 3 < agentMy.y) && (agentOur.y + 3 > agentMy.y))) {
                                    ourAgentNotFree.add(agentOur);
                                }
                            }
                        }
                        List<Agent> ourAgentFree = ourAgentsLive.stream().filter(element -> !ourAgentNotFree.contains(element)).toList();
                        System.err.println("ourAgentNotFree : " + ourAgentNotFree.size() + " ourAgentsLive : " + ourAgentsLive.size());
                        System.err.println("ourAgentFree : " + ourAgentFree.size() + " ourAgentsLive : " + ourAgentsLive.size());

                        Agent ourAgentShoot = ourAgentFree.stream().min(Comparator.comparing(element -> Distance(element.x, element.y, agent.x, agent.y))).orElse(ourAgentsLive.get(0));
                        System.err.println("ourAgentShoot " + ourAgentShoot);

                        double distanceOurAgentShoot = Distance(ourAgentShoot.x, ourAgentShoot.y, agent.x, agent.y);
                        String moveAction = "; MOVE " + x + " " + y;
                        String shootAction = "";
                        Set<Agent> ourAgentAround = new HashSet<>();
                        for (Agent agentOur : ourAgentFree) {
                            if (((agentOur.x - 3 < ourAgentShoot.x) && (agentOur.x + 3 > ourAgentShoot.x) &&
                                    (agentOur.y - 3 < ourAgentShoot.y) && (agentOur.y + 3 > ourAgentShoot.y))) {
                                ourAgentAround.add(agentOur);
                            }
                        }

                        int xMean = ourAgentShoot.x;
                        int yMean = ourAgentShoot.y;
                        if (!ourAgentAround.isEmpty()) {
                            int xMax = ourAgentAround.stream().max(Comparator.comparing(Agent::getX)).get().x;
                            int yMax = ourAgentAround.stream().max(Comparator.comparing(Agent::getY)).get().y;
                            int xMin = ourAgentAround.stream().min(Comparator.comparing(Agent::getX)).get().x;
                            int yMin = ourAgentAround.stream().min(Comparator.comparing(Agent::getY)).get().y;
                            xMean = Math.round((xMax + xMin) / 2);
                            yMean = Math.round((yMax + yMin) / 2);
                        }

                        String throwAction = "";
                        if (agent.splashBombs > 0 && distanceOurAgentShoot > 1) {
                            moveAction = "; MOVE " + xMean + " " + yMean;

                            double distance = Distance(xMean, yMean, agent.x, agent.y);
                            boolean isBomb = distance > 2 && distance < 5;

                            if (isBomb) {
                                System.err.println("Bomb (" + xMean + "," + yMean + ")");
                                throwAction = ";THROW " + xMean + " " + yMean;
                                shootAction = "";
                            }

                        } else {
                            moveAction = "; MOVE " + ourAgentShoot.x + " " + ourAgentShoot.y;
                            shootAction = "; SHOOT " + ourAgentShoot.agentId;
                        }

                        System.out.println(agent.agentId + moveAction + shootAction + throwAction);
                    } else {
                        System.out.println(agent.agentId + "; MOVE " + x + " " + y);
                    }

                } else if (!ourAgentsLive.isEmpty()) {
                    Optional<Agent> first = ourAgentsLive.stream().filter(ag -> ag.soakingPower > 0).findFirst();
                    if (!first.isEmpty()) {
                        String shoot = agent.agentId + "; SHOOT " + ourAgentsLive.get(0).agentId;
                        System.out.println(shoot);
                    } else {
                        System.out.println(move);
                    }

                } else if (i % 2 == 0) {
                    System.out.println(move);
                } else {
                    System.out.println(move1);
                }
            }
        }
    }

    static public double sqr(double a) {
        return a * a;
    }

    static public Pos getPos(int x, int y, int width, int height) {
        if (x > 0 && x < width / 2) {
            x--;
        } else if (x < width) {
            x++;
        } else if (y > 0 && y < height / 2) {
            y--;
        } else if (y < height) {
            y++;
        }
        x = (x > 0) ? ((x < width) ? x : 0) : 0;
        y = (y > 0) ? ((y < width) ? x : 0) : 0;
        return new Pos(x, y);
    }

    static public double Distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(sqr(y2 - y1) + sqr(x2 - x1));
    }
}

class Pos {
    int x;
    int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

class Agent {
    int agentId;
    int player;
    int shootCooldown;
    int optimalRange;
    int soakingPower;
    int splashBombs;

    Integer x;
    Integer y;

    public Agent(int agentId, int player, int shootCooldown, int optimalRange, int soakingPower, int splashBombs) {
        this.agentId = agentId;
        this.player = player;
        this.shootCooldown = shootCooldown;
        this.optimalRange = optimalRange;
        this.soakingPower = soakingPower;
        this.splashBombs = splashBombs;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId=" + agentId +
                ", player=" + player +
                ", shootCooldown=" + shootCooldown +
                ", optimalRange=" + optimalRange +
                ", soakingPower=" + soakingPower +
                ", splashBombs=" + splashBombs +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

}