import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = in.nextInt(); // X coordinate, 0 is left edge
                int y = in.nextInt(); // Y coordinate, 0 is top edge
                int tileType = in.nextInt();
                map[x][y] = tileType;
            }
        }

        // game loop
        while (true) {
            int agentCount = in.nextInt(); // Total number of agents still in the game
            System.err.println("agentCount "+ agentCount);
            List<Integer> agentIds = new ArrayList<>();
            for (int i = 0; i < agentCount; i++) {
                int agentId = in.nextInt();
                agentIds.add(agentId);
                int x = in.nextInt();
                int y = in.nextInt();
                int cooldown = in.nextInt(); // Number of turns before this agent can shoot
                int splashBombs = in.nextInt();
                int wetness = in.nextInt(); // Damage (0-100) this agent has taken
                myAgents.stream().filter(agent -> agent.agentId == agentId).map(agent -> {
                    agent.x = x;
                    agent.y = y;
                    agent.shootCooldown = cooldown;
                    agent.splashBombs = splashBombs;
                    agent.soakingPower = wetness;
                    return null;
                });
                ourAgents.stream().filter(agent -> agent.agentId == agentId).map(agent -> {
                    agent.x = x;
                    agent.y = y;
                    agent.shootCooldown = cooldown;
                    agent.splashBombs = splashBombs;
                    agent.soakingPower = wetness;
                    return null;
                });
            }
            List<Agent> myAgentsLive = myAgents.stream().filter(agent -> agentIds.contains(agent.agentId)).collect(Collectors.toList());
            List<Agent> ourAgentsLive = ourAgents.stream().filter(agent -> agentIds.contains(agent.agentId)).collect(Collectors.toList());
            int myAgentCount = in.nextInt(); // Number of alive agents controlled by you
            for (int i = 0; i < myAgentCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                Agent agent = myAgentsLive.get(i);

                // One line per agent: <agentId>;<action1;action2;...> actions are "MOVE x y | SHOOT id | THROW x y | HUNKER_DOWN | MESSAGE text"
                String move = agent.agentId + "; MOVE 6 1";
                String move1 = agent.agentId + "; MOVE 6 3";
                if (!ourAgentsLive.isEmpty()) {
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
}

class Agent {
    int agentId;
    int player;
    int shootCooldown;
    int optimalRange;
    int soakingPower;
    int splashBombs;

    int x;
    int y;

    public Agent(int agentId, int player, int shootCooldown, int optimalRange, int soakingPower, int splashBombs) {
        this.agentId = agentId;
        this.player = player;
        this.shootCooldown = shootCooldown;
        this.optimalRange = optimalRange;
        this.soakingPower = soakingPower;
        this.splashBombs = splashBombs;
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
                '}';
    }
}