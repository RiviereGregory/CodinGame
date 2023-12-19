import java.util.*;
import java.util.stream.Collectors;

// Define the data structures as records
record Vector(int x, int y) {
}

record FishDetail(int color, int type) {
}

record Fish(int fishId, Vector pos, Vector speed, FishDetail detail) {
}

record Drone(int droneId, Vector pos, boolean dead, int battery, List<Integer> scans) {
}

record RadarBlip(int fishId, String dir) {
}

class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Map<Integer, FishDetail> fishDetails = new HashMap<>();

        int fishCount = in.nextInt();
        for (int i = 0; i < fishCount; i++) {
            int fishId = in.nextInt();
            int color = in.nextInt();
            int type = in.nextInt();
            fishDetails.put(fishId, new FishDetail(color, type));
        }

        boolean returnSurface = false;

        // game loop
        while (true) {
            List<Integer> myScans = new ArrayList<>();
            List<Integer> foeScans = new ArrayList<>();
            Map<Integer, Drone> droneById = new HashMap<>();
            List<Drone> myDrones = new ArrayList<>();
            List<Drone> foeDrones = new ArrayList<>();
            List<Fish> visibleFishes = new ArrayList<>();
            Map<Integer, List<RadarBlip>> myRadarBlips = new HashMap<>();

            int myScore = in.nextInt();
            int foeScore = in.nextInt();

            int myScanCount = in.nextInt();
            for (int i = 0; i < myScanCount; i++) {
                int fishId = in.nextInt();
                myScans.add(fishId);
            }

            int foeScanCount = in.nextInt();
            for (int i = 0; i < foeScanCount; i++) {
                int fishId = in.nextInt();
                foeScans.add(fishId);
            }


            int myDroneCount = in.nextInt();
            for (int i = 0; i < myDroneCount; i++) {
                int droneId = in.nextInt();
                int droneX = in.nextInt();
                int droneY = in.nextInt();
                boolean dead = in.nextInt() == 1;
                int battery = in.nextInt();
                Vector pos = new Vector(droneX, droneY);
                Drone drone = new Drone(droneId, pos, dead, battery, new ArrayList<>());
                droneById.put(droneId, drone);
                myDrones.add(drone);
                myRadarBlips.put(droneId, new ArrayList<>());
            }

            int foeDroneCount = in.nextInt();
            for (int i = 0; i < foeDroneCount; i++) {
                int droneId = in.nextInt();
                int droneX = in.nextInt();
                int droneY = in.nextInt();
                boolean dead = in.nextInt() == 1;
                int battery = in.nextInt();
                Vector pos = new Vector(droneX, droneY);
                Drone drone = new Drone(droneId, pos, dead, battery, new ArrayList<>());
                droneById.put(droneId, drone);
                foeDrones.add(drone);
            }

            int droneScanCount = in.nextInt();
            for (int i = 0; i < droneScanCount; i++) {
                int droneId = in.nextInt();
                int fishId = in.nextInt();
                droneById.get(droneId).scans().add(fishId);
            }

            int visibleFishCount = in.nextInt();
            for (int i = 0; i < visibleFishCount; i++) {
                int fishId = in.nextInt();
                int fishX = in.nextInt();
                int fishY = in.nextInt();
                int fishVx = in.nextInt();
                int fishVy = in.nextInt();
                Vector pos = new Vector(fishX, fishY);
                Vector speed = new Vector(fishVx, fishVy);
                FishDetail detail = fishDetails.get(fishId);
                visibleFishes.add(new Fish(fishId, pos, speed, detail));
            }

            int myRadarBlipCount = in.nextInt();
            for (int i = 0; i < myRadarBlipCount; i++) {
                int droneId = in.nextInt();
                int fishId = in.nextInt();
                String radar = in.next();
                myRadarBlips.get(droneId).add(new RadarBlip(fishId, radar));
            }


            for (Drone drone : myDrones) {
                int light = visibleFishCount != 0 ? 1 : 0;
                if (returnSurface) {
                    System.out.println(String.format("MOVE %d %d %d", drone.pos().x(), 400, 0));
                    if (drone.pos().y() < 500) {
                        returnSurface = false;
                    }
                } else {
                    int x = drone.pos().x();
                    int y = drone.pos().y();
                    List<RadarBlip> radarBlips = myRadarBlips.get(drone.droneId());
                    Vector result = getTarget(myScans, visibleFishes, x, y, radarBlips);
                    System.out.println(String.format("MOVE %d %d %d", result.x(), result.y(), light));
                }

                if (light == 1) {
                    returnSurface = true;
                }

            }
        }
    }

    private static Vector getTarget(List<Integer> myScans, List<Fish> visibleFishes, int x, int y, List<RadarBlip> radarBlips) {
        int targetX = 5000;
        int targetY = 5000;
        if (!radarBlips.isEmpty()) {
            String direction = "NOT";
            RadarBlip radarBlipSelect = null;
            for (RadarBlip radarBlip : radarBlips) {
                if (!myScans.contains(radarBlip.fishId())) {
                    radarBlipSelect = radarBlip;
                    direction = radarBlipSelect.dir();
                    break;
                }
            }

            Optional<Fish> fishSelect = Optional.empty();
            if (!visibleFishes.isEmpty()) {
                List<Fish> fishList = visibleFishes.stream()
                        .filter(xFish -> !!myScans.contains(xFish.fishId()))
                        .collect(Collectors.toList());

                if (!fishList.isEmpty()) {
                    Random rand = new Random();
                    fishSelect = Optional.ofNullable(fishList.get(rand.nextInt(fishList.size())));
                }
            }

            switch (direction) {
                case "TL" -> {
                    targetX = Math.max(x - 500, 0);
                    targetY = Math.max(y - 500, 0);
                }
                case "TR" -> {
                    targetX = Math.min(x + 500, 10000);
                    targetY = Math.max(y - 500, 0);
                }
                case "BL" -> {
                    targetX = Math.max(x - 500, 0);
                    targetY = Math.min(y + 500, 10000);
                }
                case "BR" -> {
                    targetX = Math.min(x + 500, 10000);
                    targetY = Math.min(y + 500, 10000);
                }
                default -> {
                    targetX = 5000;
                    targetY = 5000;
                }
            }

            if (fishSelect.isPresent()) {
                targetX = fishSelect.get().pos().x();
                targetY = fishSelect.get().pos().y();
            }
        }

        Vector result = new Vector(targetX, targetY);
        return result;
    }
}