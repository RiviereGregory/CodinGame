import java.util.*;

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
        List<Integer> numberY = List.of(500, 9400);
        List<Integer> numberX = List.of(600, 2000, 3000, 5000, 6000, 7000, 8000);

        Map<Integer, List<Integer>> numberXForDrone = new HashMap<Integer, List<Integer>>() {{
            put(0, List.of(600, 2000, 3000, 5000, 6000, 7000, 8000));
            put(1, List.of(2000, 3000, 5000, 6000, 7000, 8000, 600));
            put(2, List.of(3000, 5000, 6000, 7000, 8000, 600, 2000));
            put(3, List.of(5000, 6000, 7000, 8000, 600, 2000, 3000));
            put(4, List.of(6000, 7000, 8000, 600, 2000, 3000, 5000));
            put(5, List.of(7000, 8000, 600, 2000, 3000, 5000, 6000));
            put(6, List.of(8000, 600, 2000, 3000, 5000, 6000, 7000));
            put(7, List.of(600, 2000, 3000, 5000, 6000, 7000, 8000));
        }};

        int fishCount = in.nextInt();
        for (int i = 0; i < fishCount; i++) {
            int fishId = in.nextInt();
            int color = in.nextInt();
            int type = in.nextInt();
            fishDetails.put(fishId, new FishDetail(color, type));
        }

        int tourX = 0;
        int tourY = 0;
        int[] posY = {numberY.get(tourY), numberY.get(tourY), numberY.get(tourY), numberY.get(tourY)};
        int[] posX = {numberX.get(tourX), numberX.get(tourX), numberX.get(tourX), numberX.get(tourX)};

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
                int x = drone.pos().x();
                int y = drone.pos().y();
                System.err.println("drone.droneId() = " + drone.droneId());
                System.err.println("X = " + x);
                System.err.println("posX = " + posX);
                System.err.println("Y = " + y);
                System.err.println("posY = " + posY);
                System.err.println("tourX = " + tourX);
                System.err.println("tourY = " + tourY);
                if (x == posX[drone.droneId()]) {
                    tourX = (tourX + 1) % 7;
                    posX[drone.droneId()] = numberXForDrone.get(drone.droneId()).get(tourX);
                } else if (y == posY[drone.droneId()]) {
                    tourY = (tourY + 1) % 2;
                    posY[drone.droneId()] = numberY.get(tourY);
                }

                System.out.println(String.format("MOVE %d %d %d", posX[drone.droneId()], posY[drone.droneId()], light));

            }
        }
    }
}