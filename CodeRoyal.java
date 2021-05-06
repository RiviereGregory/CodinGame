import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int numSites = in.nextInt();
		List<Site> sites = new ArrayList<Site>();
		for (int i = 0; i < numSites; i++) {
			int siteId = in.nextInt();
			int x = in.nextInt();
			int y = in.nextInt();
			int radius = in.nextInt();
			Site site = new Site(siteId, x, y, radius);
			sites.add(site);
		}
		int nbTours = 0;
		// game loop
		while (true) {
			int gold = in.nextInt();
			int touchedSite = in.nextInt(); // -1 if none
			for (int i = 0; i < numSites; i++) {
				int siteId = in.nextInt();

				Site siteContruction = new Site(siteId);
				if (sites.contains(siteContruction)) {
					siteContruction = sites.get(sites.indexOf(siteContruction));
				}

				siteContruction.setIgnore1(in.nextInt()); // used in future
															// leagues
				siteContruction.setIgnore2(in.nextInt()); // used in future
															// leagues
				// System.err.println("siteId " + siteContruction.getSiteId());
				// System.err.println("Ignore1 " +
				// siteContruction.getIgnore1());
				// System.err.println("Ignore2 " +
				// siteContruction.getIgnore2());
				siteContruction.setStructureType(in.nextInt()); // -1 = No
																// structure, 2
																// = Barracks
				siteContruction.setOwner(in.nextInt()); // -1 = No structure, 0
														// = Friendly, 1 = Enemy
				siteContruction.setParam1(in.nextInt());
				siteContruction.setParam2(in.nextInt());
			}
			int numUnits = in.nextInt();
			List<Unit> units = new ArrayList<>();
			Unit myQueen = new Unit();
			int nbGeant = 0;
			for (int i = 0; i < numUnits; i++) {
				int x = in.nextInt();
				int y = in.nextInt();
				int owner = in.nextInt();
				int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 =
												// ARCHER
				int health = in.nextInt();
				if (owner == 0 && unitType == 2) {
					nbGeant++;
				}
				Unit unit = new Unit(x, y, owner, unitType, health);
				if (unit.myQuenn()) {
					myQueen = unit;
				}
				units.add(unit);
			}

			String ligne1 = "WAIT";
			String ligne2 = "TRAIN";
			Site siteVide = getSiteVide(sites, myQueen);

			List<Site> siteFriends = getSites(sites, TypeSite.AMI);
			Site siteTour = getSiteHazardTour(getSites(sites, TypeSite.TOUR), TypeSite.TOUR);
			Site siteTourEnnemyProche = getSitesEnnemyProche(sites, TypeSite.TOUR, myQueen);
			Unit unitProche = getUnitEnnemyProche(units, myQueen);
			int distance = Math.abs(myQueen.getX() - unitProche.getX()) + Math.abs(myQueen.getY() - unitProche.getY());

			if (siteTourEnnemyProche.getSiteId() != -42) {
				System.err.println("site enemy proche");
				int distanceNoAbs = myQueen.getX() - siteTourEnnemyProche.getX() + myQueen.getY() - siteTourEnnemyProche.getY();
				int distancesecur = 100;
				if (distanceNoAbs < 0) {
					distancesecur = -100;
				}
				Site siteLoinEnnemy = getSiteVideTour(sites, myQueen);
				int x = siteTour.getX() + distancesecur;
				int y = siteTour.getY() + distanceNoAbs;
				if (x > 0 && x < 1920 && y > 0 && y < 1000) {
					ligne1 = "MOVE " + x + " " + y;
				} else {
					if(siteLoinEnnemy.getSiteId() != 42){
						ligne1 = "BUILD " + siteLoinEnnemy.getSiteId() + " TOWER";
					}else {
						ligne1 = "MOVE " + siteTour.getX() + " " + siteTour.getY();
					}
					
				}
			} else if (siteVide.getSiteId() != -42 && (getNBSite(sites, TypeSite.CHEVALIER) == 0
					|| getNBSite(sites, TypeSite.MINE) < 2 || getNBSite(sites, TypeSite.TOUR) < 1)) {
				if (getNBSite(sites, TypeSite.MINE) < 2) {
					System.err.println("Mine");
					ligne1 = "BUILD " + siteVide.getSiteId() + " MINE";
				} else if (getNBSite(sites, TypeSite.CHEVALIER) == 0) {
					System.err.println("chevalier");
					ligne1 = "BUILD " + siteVide.getSiteId() + " BARRACKS-KNIGHT";
				} else if (getNBSite(sites, TypeSite.TOUR_TOTAL) < 4) {
					System.err.println("tour");
					ligne1 = "BUILD " + siteVide.getSiteId() + " TOWER";
				} else if (unitProche.getOwner() == 1 && distance < 350) {
					System.err.println("ennemy proche");
					int distanceNoAbs = myQueen.getX() - unitProche.getX() + myQueen.getY() - unitProche.getY();
					int distancesecur = 100;
					if (distanceNoAbs < 0) {
						distancesecur = -100;
					}
					int x = myQueen.getX() + distancesecur;
					int y = myQueen.getY() + distanceNoAbs;
					if (x > 0 && x < 1920 && y > 0 && y < 1000) {
						ligne1 = "MOVE " + x + " " + y;
					} else {
						ligne1 = "MOVE " + siteTour.getX() + " " + siteTour.getY();
					}
				}
			} else {
				System.err.println("Autre");
				ligne1 = "BUILD " + siteTour.getSiteId() + " TOWER";
			}
			int goldTmp = gold;
			for (Site siteFriend : siteFriends) {
				if (siteFriend.getStructureType() == 2 && goldTmp >= getCoutSite(siteFriend.getParam2())) {
					ligne2 += " " + siteFriend.getSiteId();
					goldTmp -= getCoutSite(siteFriend.getParam2());
				}
			}
			// First line: A valid queen action
			// Second line: A set of training instructions
			System.out.println(ligne1);
			System.out.println(ligne2);
		}
	}

	public static int getCoutSite(int param2) {
		switch (param2) {
		case 0:
			return 80;
		case 1:
			return 100;
		case 2:
			return 140;

		default:
			return 0;
		}

	}

	public static Site getSiteVide(List<Site> sites, Unit myQueen) {
		int distanceMin = 10000;
		Site sitePres = new Site();
		for (Site site : sites) {
			if (site.getOwner() == -1) {
				int distance = Math.abs(myQueen.getX() - site.getX()) + Math.abs(myQueen.getY() - site.getY());
				if (distance < distanceMin) {
					distanceMin = distance;
					sitePres = site;
				}
			}
		}
		return sitePres;
	}

	public static Site getSiteVideTour(List<Site> sites, Unit myQueen) {
		int distanceMin = 10000;
		Site sitePres = new Site();
		for (Site site : sites) {
			if (site.getOwner() == -1) {
				int distance = Math.abs(myQueen.getX() - site.getX()) + Math.abs(myQueen.getY() - site.getY());
				if (distance < distanceMin && getSiteEnnemyProche(sites, TypeSite.TOUR, site).getSiteId() == -42) {
					distanceMin = distance;
					sitePres = site;
				}
			}
		}
		return sitePres;
	}

	public static Unit getUnitEnnemyProche(List<Unit> units, Unit myQueen) {
		int distanceMin = 10000;
		Unit unitProche = new Unit();
		for (Unit unit : units) {
			if (unit.getOwner() == 1) {
				int distance = Math.abs(myQueen.getX() - unit.getX()) + Math.abs(myQueen.getY() - unit.getY());
				if (distance < distanceMin) {
					distanceMin = distance;
					unitProche = unit;
				}
			}
		}
		return unitProche;
	}

	public static Site getSiteHasard(List<Site> sites, TypeSite type) {
		if (sites.isEmpty()) {
			return new Site();
		}
		for (Site site : sites) {
			if (site.getOwner() == 0 && isTypeSite(type, site)) {
				return site;
			}
		}

		int indiceAuHasard = (int) (Math.random() * (sites.size() - 1));
		return sites.get(indiceAuHasard);
	}
	
	public static Site getSiteHazardTour(List<Site> sites, TypeSite type) {
		if (sites.isEmpty()) {
			return new Site();
		}

		int indiceAuHasard = (int) (Math.random() * (sites.size() - 1));
		return sites.get(indiceAuHasard);
	}

	public static Site getSitesEnnemyProche(List<Site> sites, TypeSite type, Unit myQueen) {
		Site SiteProche = new Site();
		for (Site site : sites) {
			if (site.getOwner() == 1 && isTypeSite(type, site)) {
				int distance = Math.abs(myQueen.getX() - site.getX()) + Math.abs(myQueen.getY() - site.getY());
				System.err.println("Sites ennemy proche reine param 2 "+site.getParam2());
				if (distance < site.getParam2()) {
					return site;
				}
			}
		}
		return SiteProche;
	}
	
	public static Site getSiteEnnemyProche(List<Site> sites, TypeSite type, Site mySite) {
		Site SiteProche = new Site();
		for (Site site : sites) {
			if (site.getOwner() == 1 && isTypeSite(type, site)) {
				int distance = Math.abs(mySite.getX() - site.getX()) + Math.abs(mySite.getY() - site.getY());
				System.err.println("Site ennemy proche site param 2 "+site.getParam2());
				if (distance < site.getParam2()) {
					return site;
				}
			}
		}
		return SiteProche;
	}

	public static List<Site> getSites(List<Site> sites, TypeSite type) {
		List<Site> listSites = new ArrayList<Site>();
		for (Site site : sites) {
			if (site.getOwner() == 0 && isTypeSite(type, site)) {
				listSites.add(site);
			}
		}
		return listSites;
	}

	public static int getNBSite(List<Site> sites, TypeSite type) {
		int nbsite = 0;
		for (Site site : sites) {
			if (site.getOwner() == 0 && isTypeSite(type, site)) {
				nbsite++;
			}
		}
		return nbsite;
	}

	private static boolean isTypeSite(TypeSite type, Site site) {
		boolean testType;
		switch (type.toString()) {
		case "TOUR":
			testType = site.getStructureType() == 1 && site.getParam1() < 700;
			break;
		case "TOUR_TOTAL":
			testType = site.getStructureType() == 1;
			break;
		case "CHEVALIER":
			testType = site.getParam2() == 0;
			break;
		case "ARCHER":
			testType = site.getParam2() == 1;
			break;
		case "GEANT":
			testType = site.getParam2() == 2;
			break;
		case "MINE":
			testType = site.getStructureType() == 0;
			break;
		default:
			testType = true;
			break;
		}
		return testType;
	}
}

class Unit {

	public Unit() {
		super();
		this.owner = 42;
	}

	public Unit(int x, int y, int owner, int unitType, int health) {
		super();
		this.x = x;
		this.y = y;
		this.owner = owner;
		this.unitType = unitType;
		this.health = health;
	}

	private int x;
	private int y;
	private int owner;
	private int unitType;
	private int health;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getUnitType() {
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean myQuenn() {
		return owner == 0 && unitType == -1;
	}

}

class Site {

	public Site() {
		super();
		this.siteId = -42;
	}

	public Site(int siteId) {
		super();
		this.siteId = siteId;
	}

	public Site(int siteId, int x, int y, int radius) {
		super();
		this.siteId = siteId;
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	private int siteId;
	private int x;
	private int y;
	private int radius;
	private int ignore1;
	private int ignore2;
	private int structureType;
	private int owner;
	private int param1;
	private int param2;

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getIgnore1() {
		return ignore1;
	}

	public void setIgnore1(int ignore1) {
		this.ignore1 = ignore1;
	}

	public int getIgnore2() {
		return ignore2;
	}

	public void setIgnore2(int ignore2) {
		this.ignore2 = ignore2;
	}

	public int getStructureType() {
		return structureType;
	}

	public void setStructureType(int structureType) {
		this.structureType = structureType;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + siteId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (siteId != other.siteId)
			return false;
		return true;
	}
}

enum TypeSite {
	TOUR, CHEVALIER, MINE, GEANT, ARCHER, AMI, TOUR_TOTAL
}