import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
		Vehicule reaper = null;
		Vehicule reaper1 = null;
		Vehicule reaper2 = null;
		Vehicule destroyer = null;
		Vehicule destroyer1 = null;
		Vehicule destroyer2 = null;
		Vehicule doof = null;
		Vehicule doof1 = null;
		Vehicule doof2 = null;
		List<Vehicule> tankers = new ArrayList<>();
		List<Vehicule> tankersPeuRemplie = new ArrayList<>();
		List<Vehicule> wrecks = new ArrayList<>();
		int diffTmp = 100000;
        int diffTmpT = 100000;
        int tour = 0;
        // game loop
        while (true) {
            tankers.clear();
            wrecks.clear();
            boolean isEpave = false;
            boolean isTanker = false;
            boolean isSkill = false;
            int myScore = in.nextInt();
            int enemyScore1 = in.nextInt();
            int enemyScore2 = in.nextInt();
            int myRage = in.nextInt();
            int enemyRage1 = in.nextInt();
            int enemyRage2 = in.nextInt();
            int unitCount = in.nextInt();      
            for (int i = 0; i < unitCount; i++) {
                int unitId = in.nextInt();
                int unitType = in.nextInt();
                int player = in.nextInt();
                float mass = in.nextFloat();
                int radius = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int extra = in.nextInt();
                int extra2 = in.nextInt();
                if(player==0){
                    if (unitType == 0){
						reaper = new Vehicule(x,y,extra,100000,myScore);
                    }
                    if (unitType == 1){
                        destroyer = new Vehicule(x,y,extra,100000,0);
                    }
					if(unitType == 2){
						doof= new Vehicule(x,y,extra,100000,0);
					}
                }else if(player==1){
                    if (unitType == 0){
                        reaper1= new Vehicule(x,y,extra,100000,enemyScore1);
                    }
                    if (unitType == 1){
                        destroyer1= new Vehicule(x,y,extra,100000,0);
                    }
					if(unitType == 2){
						doof1= new Vehicule(x,y,extra,100000,0);
					}
                }
                else if(player==2){
                    if (unitType == 0){
                        reaper2= new Vehicule(x,y,extra,100000, enemyScore2);
                    }
                    if (unitType == 1){
                        destroyer2= new Vehicule(x,y,extra,100000,0);
                    }
					if(unitType == 2){
						doof2= new Vehicule(x,y,extra,100000,0);
					}
                }
                if(!isSkill && (player==1 || player == 2)){
                    isSkill = isProche(x,y, reaper.x, reaper.y);
                }

                if (unitType == 4 && extra>1){
                    Vehicule tanker = new Vehicule(x,y,
						extra,Math.abs(reaper.x-x) + Math.abs(reaper.y-y),myScore);
                    tankers.add(tanker);
                    isEpave = true;
                }else if (unitType == 4 && extra>0){
                    Vehicule tanker = new Vehicule(x,y,
						extra,Math.abs(reaper.x-x) + Math.abs(reaper.y-y),myScore);
                    tankersPeuRemplie.add(tanker);
                    isEpave = true;
                }
                
                if (unitType == 3 && extra >1){
                    Vehicule wreck = new Vehicule(x,y,extra,
						Math.abs(destroyer.x-x) + Math.abs(destroyer.y-y),myScore);
                    wrecks.add(wreck);
                    isTanker = true;
                }
            }
            //trie collections
            Collections.sort(tankers, (t1,t2) -> t1.diff.compareTo(t2.diff));
            Collections.sort(tankersPeuRemplie, (t1,t2) -> t1.diff.compareTo(t2.diff));
            Collections.sort(wrecks, (w1,w2) -> w1.diff.compareTo(w2.diff));
            if (tankers.isEmpty()){
                tankers.addAll(tankersPeuRemplie);
            }
              // Write an action using System.out.println()
           if(!isEpave){
                System.out.println("WAIT");
            }else {
                 if (tour == 0 && myRage > 60 && reaper.distance(reaper1) > 2000
                        && reaper.score < reaper1.score 
                        && reaper2.score < reaper1.score){
                    System.out.println("SKILL "+reaper1.x +" "+reaper1.y);
                    tour = 3;
                }else if ( tour == 0 && myRage > 60 && reaper.distance(reaper2) > 2000
                        && reaper.score < reaper2.score){
                    System.out.println("SKILL "+reaper2.x +" "+reaper2.y);
                    tour = 3;
                }else {
                    System.out.println(tankers.get(0).x +" "+tankers.get(0).y+" "+300);
                    if (tour > 0){
                        tour--;
                    }
                }
            }
            if (isSkill && myRage > 60 && !tankers.isEmpty() && reaper.distance(tankers.get(0))<100){
                 System.out.println("SKILL "+reaper.x +" "+reaper.y);
            }else if(!isTanker){
                System.out.println("WAIT");
            }else{
                System.out.println(wrecks.get(0).x +" "+wrecks.get(0).y+" "+300);
            }
            int xDestination = 0;
            int yDestination = 0;
            if (!wrecks.isEmpty() && !tankers.isEmpty()){
                 xDestination = (wrecks.get(0).x+tankers.get(0).x)/2;
                 yDestination = (wrecks.get(0).y+tankers.get(0).y)/2;
            }
           
            if (myRage > 60 && reaper.distance(reaper1) > 2000
                    && reaper.score < reaper1.score 
                    && reaper2.score < reaper1.score){
                System.out.println("SKILL "+reaper1.x +" "+reaper1.y);
            }else if ( myRage > 60 && reaper.distance(reaper2) > 2000
                    && reaper.score < reaper2.score){
                System.out.println("SKILL "+reaper2.x +" "+reaper2.y);
            }else {
                System.out.println(xDestination +" "+yDestination+" "+300);
            }
            
        }
    }
	public static boolean isProche(int x,int y, int xMe, int yMe){
		double rayon = 2000;
		double point = Math.sqrt(Math.pow((x-xMe),2.0)+Math.pow((y-yMe),2.0));
		if (point < rayon) return true;
		return false;
	}
    
}
class Vehicule{
        public int x;
        public int y;
        int score;
		Integer extra;
        Integer diff;
		
		Vehicule(int x,int y,Integer extra,Integer diff,int score){
			this.x=x;
			this.y=y;
			this.extra=extra;
			this.diff=diff;
			this.score=score;
		}

        public double distance(Vehicule vehicule){
            return Math.sqrt(Math.pow((this.x-vehicule.x),2.0)+
                Math.pow((this.y-vehicule.y),2.0));
        }
    }
