import java.util.*;

import java.io.*;
import java.lang.reflect.Array;

class Player {

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);

		// ALL CUSTOMERS INPUT: to ignore until Bronze

		int numAllCustomers = in.nextInt();
		List<Commnande> customerItems = new ArrayList<>();

		for (int i = 0; i < numAllCustomers; i++) {

			String customerItem = in.next();
			int customerAward = in.nextInt();
			Commnande commnande = new Commnande(customerAward, Arrays.asList(customerItem.split("-")));
			customerItems.add(commnande);

		}

		int numeroCommande = 0;
		for (Commnande commnande : customerItems) {

			System.err.print(numeroCommande + " ");
			for (int j = 0; j < commnande.customerItems.size(); j++) {
				System.err.print(commnande.customerItems.get(j) + " ");
			}
			System.err.println(" Prix :" + commnande.customerAward);
			numeroCommande++;
		}

		// KITCHEN INPUT
		Kitchen k = new Kitchen();
		k.init(in);

		// List<String> client = customerItems.get(0).customerItems;
		int idFraise = 0;
		int idCroissant = 0;
		int posXCroissant = 0;
		int posYCroissant = 0;
		int tourRestantCroissant = 20;
		int tourCroissant = 10;
		int boucle = 0;
		// game loop
		while (true) {
			int turnsRemaining = in.nextInt();
			// PLAYERS INPUT
			int playerX = in.nextInt();
			int playerY = in.nextInt();
			String playerItem = in.next();
			System.err.println("playerItem " + playerItem);

			int partnerX = in.nextInt();
			int partnerY = in.nextInt();
			String partnerItem = in.next();
			System.err.println("partnerItem " + partnerItem);

			int numTablesWithItems = in.nextInt();
			List<IngredientSurTable> ingredientSurTables = new ArrayList<>();
			for (int i = 0; i < numTablesWithItems; i++) {
				int tableX = in.nextInt();
				int tableY = in.nextInt();
				String item = in.next();
				IngredientSurTable ingredientSurTable = new IngredientSurTable(tableX, tableY,
						Arrays.asList(item.split("-")));
				ingredientSurTables.add(ingredientSurTable);
			}

			// oven to ignore until bronze
			String ovenContents = in.next();
			int ovenTimer = in.nextInt();
			// CURRENT CUSTOMERS INPUT
			int numCustomers = in.nextInt();
			for (int i = 0; i < numCustomers; i++) {
				String customerItem = in.next();
				int customerAward = in.nextInt();

				System.err.println(customerItem + " Prix :" + customerAward);
			}

			boolean isFraise = false;
			for (int i = 0; i < customerItems.get(0).customerItems.size(); i++) {
				if (customerItems.get(0).customerItems.get(i).contains("CHOPPED_STRAWBERRIES")
						|| customerItems.get(0).customerItems.get(i).contains("STRAWBERRIES")) {
					isFraise = true;
					idFraise = i;
					break;
				}
			}
			boolean isCroissant = false;
			for (int i = 0; i < customerItems.get(0).customerItems.size(); i++) {
				if (customerItems.get(0).customerItems.get(i).contains("CROISSANT")
						|| customerItems.get(0).customerItems.get(i).contains("DOUGH")
						|| customerItems.get(0).customerItems.get(i).contains("OVEN")) {
					isCroissant = true;
					idCroissant = i;
					break;
				}
			}

			// GAME LOGIC
			// fetch dish, then blueberries, then ice cream, then drop it at
			// first empty table
			System.err.print("commande en cours ");
			for (String string : customerItems.get(0).customerItems) {
				System.err.print(string + " ");
			}
			System.err.println(" ");
			if (customerItems.get(0).customerItems.isEmpty() && !playerItem.contains("NONE")) {				
				if (boucle > 2) {
					k.tables.stream().filter(t -> t.isEmpty()).findFirst().get().use();
				}else if (boucle > 10) {
					customerItems.remove(0);
					boucle = 0;
				}else {
					k.tables.stream().filter(t -> t.isWindow()).findFirst().get().use();
				}
				boucle++;
			} else if (customerItems.get(0).customerItems.isEmpty() && playerItem.contains("NONE")) {
				System.out.println("WAIT");
				boucle = 0;
				customerItems.remove(0);
			} else {
				boolean isNotPremier = isFraise || isCroissant;
				boolean isMulti = isFraise && isCroissant;
				System.err.println(0 + " client " + customerItems.get(0).customerItems.get(0));
				if (!isNotPremier && customerItems.get(0).customerItems.get(0).contains("DISH")
						&& !playerItem.contains("DISH")) {
					Table table = k.tables.stream().filter(t -> t.isDish()).findFirst().get();
					table.use();
				} else if (!isNotPremier && customerItems.get(0).customerItems.get(0).contains("BLUEBERRIES")
						&& !playerItem.contains("BLUEBERRIES")) {
					k.tables.stream().filter(t -> t.isBlueBerries()).findFirst().get().use();
				} else if (!isNotPremier && customerItems.get(0).customerItems.get(0).contains("ICE_CREAM")
						&& !playerItem.contains("ICE_CREAM")) {
					k.tables.stream().filter(t -> t.isIceCream()).findFirst().get().use();
				} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("CROISSANT")
						&& !playerItem.contains("DOUGH")) {
					k.tables.stream().filter(t -> t.isPate()).findFirst().get().use();
				} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("DOUGH")
						&& !playerItem.contains("NONE")) {
					k.tables.stream().filter(t -> t.isOven()).findFirst().get().use();
				} else if (isCroissant && tourCroissant <= 0
						&& customerItems.get(0).customerItems.get(idCroissant).contains("OVEN")
						&& !playerItem.contains("CROISSANT")) {
					k.tables.stream().filter(t -> t.isOven()).findFirst().get().use();
				} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("OVEN")
						&& !playerItem.contains("DISH")) {
					k.tables.stream().filter(t -> t.isDish()).findFirst().get().use();
				} else if (!isMulti && isFraise
						&& customerItems.get(0).customerItems.get(idFraise).contains("CHOPPED_STRAWBERRIES")
						&& !playerItem.contains("STRAWBERRIES")) {
					k.tables.stream().filter(t -> t.isFraise()).findFirst().get().use();
				} else if (!isMulti && isFraise
						&& customerItems.get(0).customerItems.get(idFraise).contains("STRAWBERRIES")
						&& !playerItem.contains("CHOPPED_STRAWBERRIES")) {
					k.tables.stream().filter(t -> t.isPlancheADecouper()).findFirst().get().use();
				} else {
					boolean isIngredient = false;
					if (isCroissant) {

						for (IngredientSurTable ingredientSurTable : ingredientSurTables) {
							if (ingredientSurTable.item.size() == 1 && ingredientSurTable.item.contains("CROISSANT")) {
								System.out
										.println("USE " + ingredientSurTable.tableX + " " + ingredientSurTable.tableY);
								isIngredient = true;
								break;
							}
						}
					}
					if (!isIngredient) {
						System.out.println("WAIT");
						if (customerItems.get(0).customerItems.isEmpty() && !playerItem.contains("NONE")) {
							customerItems.remove(0);
						}
					}

				}
				removeListIngredient(customerItems, idFraise, playerItem, isFraise, isCroissant, idCroissant);
				boucle = 0;
			}

			if (isCroissant && customerItems.get(0).customerItems.contains("OVEN")) {
				tourRestantCroissant--;
				tourCroissant--;
			} else {
				tourRestantCroissant = 20;
				tourCroissant = 10;
			}
		}
	}

	private static void removeListIngredient(List<Commnande> customerItems, int idFraise, String playerItem,
			boolean isFraise, boolean isCroissant, int idCroissant) {
		if (customerItems.get(0).customerItems.get(0).contains("DISH") && playerItem.contains("DISH")) {
			customerItems.get(0).customerItems.remove(0);
		} else if (customerItems.get(0).customerItems.get(0).contains("BLUEBERRIES")
				&& playerItem.contains("BLUEBERRIES")) {
			customerItems.get(0).customerItems.remove(0);
		} else if (customerItems.get(0).customerItems.get(0).contains("ICE_CREAM")
				&& playerItem.contains("ICE_CREAM")) {
			customerItems.get(0).customerItems.remove(0);
		} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("CROISSANT")
				&& playerItem.contains("DOUGH")) {
			customerItems.get(0).customerItems.remove(idCroissant);
			customerItems.get(0).customerItems.add("DOUGH");
			customerItems.get(0).etatCroissant = EtatCommande.FOUR;
		} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("DOUGH")
				&& playerItem.contains("NONE")) {
			customerItems.get(0).customerItems.remove(idCroissant);
			customerItems.get(0).customerItems.add("OVEN");
			customerItems.get(0).etatCroissant = EtatCommande.CUISSON;
		} else if (isCroissant && customerItems.get(0).customerItems.get(idCroissant).contains("OVEN")
				&& playerItem.contains("CROISSANT")) {
			customerItems.get(0).customerItems.remove(idCroissant);
			customerItems.get(0).etatCroissant = EtatCommande.CROISSANT;
		} else if (isFraise && customerItems.get(0).customerItems.get(idFraise).contains("CHOPPED_STRAWBERRIES")
				&& playerItem.contains("STRAWBERRIES")) {
			customerItems.get(0).customerItems.remove(idFraise);
			customerItems.get(0).customerItems.add("STRAWBERRIES");
			customerItems.get(0).etatFraise = EtatCommande.DECOUPE;
		} else if (isFraise && customerItems.get(0).customerItems.get(idFraise).contains("STRAWBERRIES")
				&& playerItem.contains("CHOPPED_STRAWBERRIES")) {
			customerItems.get(0).customerItems.remove(idFraise);
			customerItems.get(0).etatFraise = EtatCommande.FRAISE_DECOUPE;
		}
	}
}

class Kitchen {
	List<Table> tables;

	public Kitchen() {
		tables = new ArrayList<Table>();
	}

	public void init(Scanner in) {
		in.nextLine();
		for (int i = 0; i < 7; i++) {
			String kitchenLine = in.nextLine();
			for (int j = 0; j < kitchenLine.length(); j++) {
				char c = kitchenLine.charAt(j);
				TableType t = TableType.get(c);
				if (t != null) {
					tables.add(new Table(t, j, i));
				}
			}
			System.err.println(kitchenLine);
		}
	}
}

class Position {
	int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// @TODO
	public int distance(Position p) {
		return 0;
	}
}

class Commnande {
	int customerAward;
	List<String> customerItems = new ArrayList<>();
	List<String> commandeOrigninale = new ArrayList<>();
	EtatCommande etatCroissant = EtatCommande.PATE;
	EtatCommande etatFraise = EtatCommande.FRAISE;

	public Commnande(int customerAward, List<String> customerItems) {
		this.customerAward = customerAward;
		this.customerItems.addAll(customerItems);
		this.commandeOrigninale.addAll(customerItems);
	}

}

enum EtatCommande {

	PATE, FOUR, CUISSON, CROISSANT, FRAISE, DECOUPE, FRAISE_DECOUPE;
}

class IngredientSurTable {
	int tableX;
	int tableY;
	List<String> item = new ArrayList<>();

	public IngredientSurTable(int tableX, int tableY, List<String> item) {
		this.tableX = tableX;
		this.tableY = tableY;
		this.item.addAll(item);
	}
}

class Table {
	TableType type;
	int x, y;

	public Table(TableType t, int x, int y) {
		type = t;
		this.x = x;
		this.y = y;
	}

	public void error() {
		System.err.println("Table of type " + type + " at " + x + " " + y);
	}

	public boolean isDish() {
		return isTableType(TableType.PLATES);
	}

	public boolean isBlueBerries() {
		return isTableType(TableType.BLUEBERRY);
	}

	public boolean isIceCream() {
		return isTableType(TableType.ICE_CREAM);
	}

	public boolean isFraise() {
		return isTableType(TableType.STRAWBERRIES);
	}

	public boolean isPlancheADecouper() {
		return isTableType(TableType.CHOPPED);
	}

	public boolean isOven() {
		return isTableType(TableType.OVEN);
	}

	public boolean isPate() {
		return isTableType(TableType.PATE);
	}

	public boolean isEmpty() {
		return isTableType(TableType.EMPTY);
	}

	public boolean isWindow() {
		return isTableType(TableType.WINDOW);
	}

	private boolean isTableType(TableType t) {
		return type.equals(t);
	}

	public void use() {
		System.out.println("USE " + x + " " + y);
	}
}

enum TableType {

	BLUEBERRY, ICE_CREAM, PLATES, STRAWBERRIES, CROISSANT, PATE, OVEN, CHOPPED, EMPTY, WINDOW;

	static TableType get(Character c) {

		switch (c) {
		case 'D':
			return PLATES;
		case 'B':
			return BLUEBERRY;
		case 'I':
			return ICE_CREAM;
		case 'S':
			return STRAWBERRIES;
		case 'H':
			return PATE;
		case 'C':
			return CHOPPED;
		case 'O':
			return OVEN;
		case 'W':
			return WINDOW;
		case '#':
			return EMPTY;
		}
		return null;
	}
}