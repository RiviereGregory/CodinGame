import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Made with love by AntiSquid, Illedan and Wildum. You can help children learn
 * to code while you participate by donating to CoderDojo.
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int myTeam = in.nextInt();
		int bushAndSpawnPointCount = in.nextInt(); // usefrul from wood1,
													// represents the number of
													// bushes and the number of
													// places where neutral
													// units can spawn
		List<BushAndSpawnPointCount> bushAndSpawnPointCounts = new ArrayList<>();
		for (int i = 0; i < bushAndSpawnPointCount; i++) {
			BushAndSpawnPointCount bushAndSpawn = new BushAndSpawnPointCount();
			bushAndSpawn.entityType = in.next(); // BUSH, from wood1 it can also
													// be SPAWN
			bushAndSpawn.x = in.nextInt();
			bushAndSpawn.y = in.nextInt();
			bushAndSpawn.radius = in.nextInt();
			bushAndSpawnPointCounts.add(bushAndSpawn);
		}
		int itemCount = in.nextInt(); // useful from wood2
		List<ItemCount> itemCounts = new ArrayList<>();

		for (int i = 0; i < itemCount; i++) {
			ItemCount item = new ItemCount();
			item.itemName = in.next(); // contains keywords such as BRONZE,
										// SILVER and BLADE, BOOTS connected by
										// "_" to help you sort easier
			item.itemCost = in.nextInt(); // BRONZE items have lowest cost, the
											// most expensive items are
											// LEGENDARY
			item.damage = in.nextInt(); // keyword BLADE is present if the most
										// important item stat is damage
			item.health = in.nextInt();
			item.maxHealth = in.nextInt();
			item.mana = in.nextInt();
			item.maxMana = in.nextInt();
			item.moveSpeed = in.nextInt(); // keyword BOOTS is present if the
											// most important item stat is
											// moveSpeed
			item.manaRegeneration = in.nextInt();
			item.isPotion = in.nextInt(); // 0 if it's not instantly consumed
			itemCounts.add(item);
		}

		int objetBuy = 0;
		// game loop
		while (true) {
			EntityCount myHero = new EntityCount();
			EntityCount myTower = new EntityCount();
			EntityCount myUnit = new EntityCount();
			EntityCount ennemyTower = new EntityCount();
			EntityCount ennemyHero = new EntityCount();
			EntityCount ennemyUnit = new EntityCount();
			int gold = in.nextInt();
			int enemyGold = in.nextInt();
			int roundType = in.nextInt(); // a positive value will show the
											// number of heroes that await a
											// command
			int entityCount = in.nextInt();
			List<EntityCount> entityCounts = new ArrayList<EntityCount>();
			for (int i = 0; i < entityCount; i++) {
				EntityCount entity = new EntityCount();
				entity.unitId = in.nextInt();
				entity.team = in.nextInt();
				entity.unitType = in.next(); // UNIT, HERO, TOWER, can also be
												// GROOT from wood1
				entity.x = in.nextInt();
				entity.y = in.nextInt();
				entity.attackRange = in.nextInt();
				entity.health = in.nextInt();
				entity.maxHealth = in.nextInt();
				entity.shield = in.nextInt(); // useful in bronze
				entity.attackDamage = in.nextInt();
				entity.movementSpeed = in.nextInt();
				entity.stunDuration = in.nextInt(); // useful in bronze
				entity.goldValue = in.nextInt();
				entity.countDown1 = in.nextInt(); // all countDown and mana
													// variables are useful
													// starting in bronze
				entity.countDown2 = in.nextInt();
				entity.countDown3 = in.nextInt();
				entity.mana = in.nextInt();
				entity.maxMana = in.nextInt();
				entity.manaRegeneration = in.nextInt();
				entity.heroType = in.next(); // DEADPOOL, VALKYRIE,
												// DOCTOR_STRANGE, HULK, IRONMAN
				entity.isVisible = in.nextInt(); // 0 if it isn't
				entity.itemsOwned = in.nextInt(); // useful from wood1
				entityCounts.add(entity);
			}
			if (roundType < 0) {
				System.out.println("VALKYRIE");
			} else {
				int loin = 0;
				int faible = 1900;
				for (EntityCount count : entityCounts) {
					if (count.unitType.equals("HERO") && count.team != myTeam) {
						ennemyHero = count;
					}
					if (count.unitType.equals("TOWER") && count.team != myTeam) {
						ennemyTower = count;
					}

					if (count.unitType.equals("TOWER") && count.team == myTeam) {
						myTower = count;
					}
					if (count.unitType.equals("HERO") && count.team == myTeam) {
						myHero = count;
					}
					if (count.unitType.equals("UNIT") && count.team != myTeam && count.health < faible) {
						ennemyUnit = count;
						faible = count.health;
					}
					if (count.unitType.equals("UNIT") && count.team == myTeam && myTower.distance(count) > loin) {
						myUnit = count;
						loin = myTower.distance(count);
					}
				}

				System.err.println(myHero.distance(ennemyTower));
				int diffX = 0;
				int moveX = 0;
				if (myHero.team == 0) {
					diffX = myHero.x - myUnit.x;
					moveX = myUnit.x == 0 ? myTower.x : (myUnit.x - 150);
				} else {
					diffX = myUnit.x - myHero.x;
					moveX = myUnit.x == 0 ? myTower.x : (myUnit.x + 150);
				}
				System.err.println(diffX);
				if (myHero.distance(myTower) < 200 && myHero.distance(ennemyHero) < 200) {
					System.out.println("ATTACK_NEAREST HERO");
				} else if (myHero.distance(myTower) < 200 && myHero.distance(ennemyUnit) < 200) {
					System.out.println("ATTACK " + ennemyUnit.unitId);
				} else if (ennemyHero.y < 50 || ennemyHero.y > 700 || (ennemyHero.x < 50 && ennemyHero.y < 50)) {
					System.out.println("ATTACK_NEAREST HERO");
				} else if (myHero.distance(ennemyTower) < 600 && myHero.health < (myHero.maxHealth / 2)) {
					System.out.println("MOVE " + myTower.x + " " + myTower.y);
				} else if (myHero.distance(ennemyHero) < 200
						&& (myHero.health > (myHero.maxHealth / 4) || myHero.health > (ennemyHero.health - 30))) {
					System.out.println("ATTACK_NEAREST HERO");
				} else if (myHero.health < (myHero.maxHealth / 4) && myHero.distance(myTower) > 800) {
					System.out.println("MOVE " + myTower.x + " " + myTower.y);
				} else if (diffX > -10) {
					System.out.println("MOVE " + moveX + " " + (myUnit.y == 0 ? myTower.y : myUnit.y));
				} else if (objetBuy < 4) {
					String nameItem = "";
					int maxHe = 0;
					int costObjet = 0;
					for (ItemCount objet : itemCounts) {
						if (gold > objet.itemCost && objet.isPotion != 1) {
							if (objet.health > maxHe) {
								maxHe = objet.health;
								nameItem = objet.itemName;
								costObjet = objet.itemCost;
								System.err.println("cout " + costObjet);
							}
						}
					}
					if (!nameItem.equals("")) {
						System.out.println("BUY " + nameItem);
						objetBuy++;
					} else {
						System.out.println("ATTACK " + ennemyUnit.unitId);
					}
				} else {
					System.out.println("ATTACK " + ennemyUnit.unitId);
				}
			}
		}
	}
}

class BushAndSpawnPointCount {
	String entityType; // BUSH, from wood1 it can also be SPAWN
	int x;
	int y;
	int radius;
}

class ItemCount {
	String itemName; // contains keywords such as BRONZE, SILVER and BLADE,
						// BOOTS connected by "_" to help you sort easier
	int itemCost; // BRONZE items have lowest cost, the most expensive items are
					// LEGENDARY
	int damage; // keyword BLADE is present if the most important item stat is
				// damage
	int health;
	int maxHealth;
	int mana;
	int maxMana;
	int moveSpeed; // keyword BOOTS is present if the most important item stat
					// is moveSpeed
	int manaRegeneration;
	int isPotion;
}

class EntityCount {
	int unitId;
	int team;
	String unitType; // UNIT, HERO, TOWER, can also be GROOT from wood1
	int x;
	int y;
	int attackRange;
	int health;
	int maxHealth;
	int shield; // useful in bronze
	int attackDamage;
	int movementSpeed;
	int stunDuration; // useful in bronze
	int goldValue;
	int countDown1; // all countDown and mana variables are useful starting in
					// bronze
	int countDown2;
	int countDown3;
	int mana;
	int maxMana;
	int manaRegeneration;
	String heroType; // DEADPOOL, VALKYRIE, DOCTOR_STRANGE, HULK, IRONMAN
	int isVisible; // 0 if it isn't
	int itemsOwned; // useful from wood1

	int distance(EntityCount entityCount) {
		return (int) Math.sqrt(Math.pow((entityCount.x - x), 2) + Math.pow((entityCount.y - y), 2));
	}
}