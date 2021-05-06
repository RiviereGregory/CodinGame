import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		// game loop
		Potion potionMax = null;
		while (true) {
			int actionCount = in.nextInt(); // the number of spells and recipes
			List<Potion> potions = new ArrayList<Potion>();
			for (int i = 0; i < actionCount; i++) {
				int actionId = in.nextInt(); // the unique ID of this spell or
												// recipe
				String actionType = in.next(); // in the first league: BREW;
												// later: CAST, OPPONENT_CAST,
												// LEARN, BREW
				int delta0 = in.nextInt(); // tier-0 ingredient change
				int delta1 = in.nextInt(); // tier-1 ingredient change
				int delta2 = in.nextInt(); // tier-2 ingredient change
				int delta3 = in.nextInt(); // tier-3 ingredient change
				int price = in.nextInt();
				int tomeIndex = in.nextInt();
				int taxCount = in.nextInt();
				boolean castable = in.nextInt() != 0;
				boolean repeatable = in.nextInt() != 0;

				Potion potion = new Potion(actionId, actionType, delta0, delta1, delta2, delta3, price, tomeIndex,
						taxCount, castable, repeatable);
				potions.add(potion);
				// System.err.println(potion);
			}
			Inventaire monInventaire = null;
			Inventaire adversaireInventaire = null;
			for (int i = 0; i < 2; i++) {
				int inv0 = in.nextInt(); // tier-0 ingredients in inventory
				int inv1 = in.nextInt();
				int inv2 = in.nextInt();
				int inv3 = in.nextInt();
				int score = in.nextInt(); // amount of rupees
				if (i == 0) {
					monInventaire = new Inventaire(inv0, inv1, inv2, inv3, score);
				} else {
					adversaireInventaire = new Inventaire(inv0, inv1, inv2, inv3, score);
				}
			}
			int maxPrice = 0;
			int id = -1;
			int idSort = -1;
			boolean sortEpuise = false;
			int idSortPotion = -1;

			if (potionMax == null) {
				potionMax = potions.stream().filter(p -> p.actionType.equals("BREW"))
						.max(Comparator.comparing(Potion::getCout)).get();
			} else if (!potions.contains(potionMax)){
				potionMax = potions.stream().filter(p -> p.actionType.equals("BREW"))
						.max(Comparator.comparing(Potion::getCout)).get();
			}

			System.err.println("Max " + potionMax);

			for (Potion potion : potions) {
				boolean potionPossible = monInventaire.inv0 + potion.delta0 >= 0;
				potionPossible &= monInventaire.inv1 + potion.delta1 >= 0;
				potionPossible &= monInventaire.inv2 + potion.delta2 >= 0;
				potionPossible &= monInventaire.inv3 + potion.delta3 >= 0;
				if (potionPossible && maxPrice < potion.price && potion.actionType.equals("BREW")) {
					maxPrice = potion.price;
					id = potion.actionId;
					potionMax = null;
				}
			}

			if (potionMax != null) {
				List<Potion> casts = potions.stream().filter(p -> p.actionType.equals("CAST"))
						.collect(Collectors.toList());
				int val0 = monInventaire.inv0 + potionMax.delta0;
				int val1 = monInventaire.inv1 + potionMax.delta1;
				int val2 = monInventaire.inv2 + potionMax.delta2;
				int val3 = monInventaire.inv3 + potionMax.delta3;
				System.err.println("val0 " + val0);
				System.err.println("val1 " + val1);
				System.err.println("val2 " + val2);
				System.err.println("val3 " + val3);
				if (val0 >= 0 && val1 >= 0 && val2 >= 0 && val3 >= 0) {
					id = potionMax.actionId;
					maxPrice = potionMax.price;
				} else {
					for (Potion potion : casts) {
						int resVal0 = monInventaire.inv0 + potion.delta0;
						int resVal1 = monInventaire.inv1 + potion.delta1;
						int resVal2 = monInventaire.inv2 + potion.delta2;
						int resVal3 = monInventaire.inv3 + potion.delta3;

						boolean potionPossible = resVal0 >= 0 && resVal1 >= 0;
						potionPossible &= resVal2 >= 0 && resVal3 >= 0;

						if (val0 < 0 && potion.delta0 > 0) {
							if (potionPossible) {
								idSortPotion = potion.actionId;
								if (!potion.castable) {
									sortEpuise = true;
								}
							} else {
								RetourChoix choix = findIngredient(monInventaire, casts, 0, resVal1, resVal2, resVal3);
								idSortPotion = choix.idSort;
								sortEpuise = choix.isSortEpuise;
							}
						} else if (val1 < 0 && potion.delta1 > 0) {
							if (potionPossible) {
								idSortPotion = potion.actionId;
								if (!potion.castable) {
									sortEpuise = true;
								}
							} else {
								RetourChoix choix = findIngredient(monInventaire, casts, resVal0, 0, resVal2, resVal3);
								idSortPotion = choix.idSort;
								sortEpuise = choix.isSortEpuise;
							}
						} else if (val2 < 0 && potion.delta2 > 0) {
							if (potionPossible) {
								idSortPotion = potion.actionId;
								if (!potion.castable) {
									sortEpuise = true;
								}
							} else {
								RetourChoix choix = findIngredient(monInventaire, casts, resVal0, resVal1, 0, resVal3);
								idSortPotion = choix.idSort;
								sortEpuise = choix.isSortEpuise;
							}
						} else if (val3 < 0 && potion.delta3 > 0) {
							if (potionPossible) {
								idSortPotion = potion.actionId;
								if (!potion.castable) {
									sortEpuise = true;
								}
							} else {
								RetourChoix choix = findIngredient(monInventaire, casts, resVal0, resVal1, resVal2, 0);
								idSortPotion = choix.idSort;
								sortEpuise = choix.isSortEpuise;
							}
						}

					}
				}

			}

			List<Potion> learns = potions.stream().filter(p -> p.actionType.equals("LEARN"))
					.collect(Collectors.toList());
			Potion learn = learns.stream().findAny().get();
			final int idsortLearn = idSort;
			final int idsortLearn2 = idSortPotion;
			boolean inventairePlein = false;
			if (idsortLearn != -1) {
				Potion cast = potions.stream().filter(p -> p.isMe(idsortLearn)).findFirst().get();
				inventairePlein = (cast.getCout() + monInventaire.getTotal()) > 9;
			} else if (idsortLearn2 != -1) {
				Potion cast2 = potions.stream().filter(p -> p.isMe(idsortLearn2)).findFirst().get();
				inventairePlein = (cast2.getCout() + monInventaire.getTotal()) > 9;
			}

			System.err.println("Mon inventaire " + monInventaire.getTotal());
			System.err.println("learn.tomeIndex " + learn.tomeIndex);
			System.err.println("monInventaire.inv0 " + monInventaire.inv0);
			String result = "WAIT";
			if (maxPrice > 0) {
				result = "BREW " + id;
			} else if (idSort != -1 && !inventairePlein) {
				result = "CAST " + idSort;
			} else if (sortEpuise) {
				result = "REST";
			} else if (idSortPotion != -1 && !inventairePlein) {
				result = "CAST " + idSortPotion;
			} else if ((monInventaire.inv0 - learn.tomeIndex) >= 0) {
				result = "LEARN " + learn.actionId;
			}
			// in the first league: BREW <id> | WAIT; later: BREW <id> | CAST
			// <id> [<times>] | LEARN <id> | REST | WAIT
			System.out.println(result);
		}
	}

	static RetourChoix findIngredient(Inventaire monInventaire, List<Potion> casts, int val0, int val1, int val2,
			int val3) {
		boolean sortEpuise = false;
		int idSortPotion = -1;
		for (Potion potion : casts) {
			int resVal0 = monInventaire.inv0 + potion.delta0;
			int resVal1 = monInventaire.inv1 + potion.delta1;
			int resVal2 = monInventaire.inv2 + potion.delta2;
			int resVal3 = monInventaire.inv3 + potion.delta3;

			boolean potionPossible = resVal0 >= 0 && resVal1 >= 0;
			potionPossible &= resVal2 >= 0 && resVal3 >= 0;

			if (val0 < 0 && potion.delta0 > 0) {
				if (potionPossible) {
					idSortPotion = potion.actionId;
					if (!potion.castable) {
						sortEpuise = true;
					}
				} else {
					RetourChoix choix = findIngredient(monInventaire, casts, 0, resVal1, resVal2, resVal3);
					idSortPotion = choix.idSort;
					sortEpuise = choix.isSortEpuise;
				}
			} else if (val1 < 0 && potion.delta1 > 0) {
				if (potionPossible) {
					idSortPotion = potion.actionId;
					if (!potion.castable) {
						sortEpuise = true;
					}
				} else {
					RetourChoix choix = findIngredient(monInventaire, casts, resVal0, 0, resVal2, resVal3);
					idSortPotion = choix.idSort;
					sortEpuise = choix.isSortEpuise;
				}
			} else if (val2 < 0 && potion.delta2 > 0) {
				if (potionPossible) {
					idSortPotion = potion.actionId;
					if (!potion.castable) {
						sortEpuise = true;
					}
				} else {
					RetourChoix choix = findIngredient(monInventaire, casts, resVal0, resVal1, 0, resVal3);
					idSortPotion = choix.idSort;
					sortEpuise = choix.isSortEpuise;
				}
			} else if (val3 < 0 && potion.delta3 > 0) {
				if (potionPossible) {
					idSortPotion = potion.actionId;
					if (!potion.castable) {
						sortEpuise = true;
					}
				} else {
					RetourChoix choix = findIngredient(monInventaire, casts, resVal0, resVal1, resVal2, 0);
					idSortPotion = choix.idSort;
					sortEpuise = choix.isSortEpuise;
				}
			}

		}
		System.err.println("idSortPotion " + idSortPotion + " sortEpuise " + sortEpuise);

		return new RetourChoix(idSortPotion, sortEpuise);
	}

}

class RetourChoix {
	int idSort;
	boolean isSortEpuise;

	public RetourChoix(int idSort, boolean isSortEpuise) {
		this.idSort = idSort;
		this.isSortEpuise = isSortEpuise;
	}

}

class Inventaire {
	int inv0;
	int inv1;
	int inv2;
	int inv3;
	int score;

	public Inventaire(int inv0, int inv1, int inv2, int inv3, int score) {
		this.inv0 = inv0;
		this.inv1 = inv1;
		this.inv2 = inv2;
		this.inv3 = inv3;
		this.score = score;
	}

	int getTotal() {
		return inv0 + inv1 + inv2 + inv3;
	}

}

class Potion {
	int actionId;
	String actionType;
	int delta0;
	int delta1;
	int delta2;
	int delta3;
	int price;
	int tomeIndex;
	int taxCount;
	boolean castable;
	boolean repeatable;

	public Potion(int actionId, String actionType, int delta0, int delta1, int delta2, int delta3, int price,
			int tomeIndex, int taxCount, boolean castable, boolean repeatable) {
		this.actionId = actionId;
		this.actionType = actionType;
		this.delta0 = delta0;
		this.delta1 = delta1;
		this.delta2 = delta2;
		this.delta3 = delta3;
		this.price = price;
		this.tomeIndex = tomeIndex;
		this.taxCount = taxCount;
		this.castable = castable;
		this.repeatable = repeatable;
	}

	int getPrice() {
		return price;
	}

	int getCout() {
		return delta0 + delta1 + delta2 + delta3;
	}

	boolean isMe(int actionId) {
		return this.actionId == actionId;
	}

	@Override
	public String toString() {
		return "Potion [actionId=" + actionId + ", actionType=" + actionType + ", delta0=" + delta0 + ", delta1="
				+ delta1 + ", delta2=" + delta2 + ", delta3=" + delta3 + ", price=" + price + ", tomeIndex=" + tomeIndex
				+ ", taxCount=" + taxCount + ", castable=" + castable + ", repeatable=" + repeatable + "]";
	}

}