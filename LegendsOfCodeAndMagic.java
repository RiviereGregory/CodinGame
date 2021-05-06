
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

		// game loop
		while (true) {
			Joueur joueurActif = new Joueur();
			getJoueur(in, joueurActif);
			System.err.println(joueurActif);
			Joueur joueurAdversaire = new Joueur();
			getJoueur(in, joueurAdversaire);
			System.err.println(joueurAdversaire);
			int opponentHand = in.nextInt();
			int cardCount = in.nextInt();
			List<Card> cardsMainJoueurActif = new ArrayList<>();
			List<Card> cardsPlateauJoueurAdversaire = new ArrayList<>();
			List<Card> cardsPlateauJoueurActif = new ArrayList<>();

			for (int i = 0; i < cardCount; i++) {
				Card card = new Card();
				card.cardNumber = in.nextInt();
				card.instanceId = in.nextInt();
				card.location = in.nextInt();
				card.cardType = in.nextInt();
				card.cost = in.nextInt();
				card.attack = in.nextInt();
				card.defense = in.nextInt();
				card.abilities = in.next();
				card.myHealthChange = in.nextInt();
				card.opponentHealthChange = in.nextInt();
				card.cardDraw = in.nextInt();
				// System.err.println(card);
				switch (card.location) {
				case 0:
					cardsMainJoueurActif.add(card);
					break;
				case 1:
					cardsPlateauJoueurActif.add(card);
					break;
				case -1:
					cardsPlateauJoueurAdversaire.add(card);
					break;
				}
			}

			if (joueurActif.playerMana == 0) {
				System.out.println(choixCarte(cardsMainJoueurActif));
			} else {
				String choix = choixCarte(cardsMainJoueurActif, cardsPlateauJoueurActif, joueurActif.playerMana,
						cardsPlateauJoueurAdversaire);
				System.out.println(choix.isEmpty() ? "PASS" : choix);
			}
		}
	}

	private static void getJoueur(Scanner in, Joueur joueurActif) {
		joueurActif.playerHealth = in.nextInt();
		joueurActif.playerMana = in.nextInt();
		joueurActif.playerDeck = in.nextInt();
		joueurActif.playerRune = in.nextInt();
	}

	private static String choixCarte(List<Card> cardsMainJoueurActif) {
		int pickValue = 0;
		Card cardRef = cardsMainJoueurActif.get(0);
		int coutRef = coutCarte(cardRef);
		for (int i = 1; i < cardsMainJoueurActif.size(); i++) {
			Card cardFor = cardsMainJoueurActif.get(i);
			int coutFor = coutCarte(cardFor);
			if (coutRef > coutFor || (coutRef == coutFor && cardRef.attack < cardFor.attack)) {
				pickValue = i;
				coutRef = coutFor;
				cardRef = cardFor;
			}

		}

		return "PICK " + pickValue;
	}

	private static String choixCarte(List<Card> cardsMainJoueurActif, List<Card> cardsPlateauJoueurActif,
			int playerMana, List<Card> cardsPlateauJoueurAdversaire) {
		StringBuilder sortie = new StringBuilder("");
		int i = 0;
		for (Card card : cardsPlateauJoueurAdversaire) {
			System.err.println("Plateau adversaire");
			System.err.println(card);
		}
		List<Card> defenseCards = cardsPlateauJoueurAdversaire;
		List<String> listPickId = new ArrayList<>();
		List<String> listPick = new ArrayList<>();
		for (Card card : cardsPlateauJoueurActif) {
			System.err.println("Plateau Moi");
			System.err.println(card);
			int id2 = -1;

			Iterator<Card> iterator = defenseCards.iterator();
			while (iterator.hasNext()) {
				Card card2 = iterator.next();
				if (card2.abilities.substring(3, 4).equals("G")) {
					id2 = card2.instanceId;
					iterator.remove();
					break;
				}
			}
			if (id2 == -1) {
				while (iterator.hasNext()) {
					Card card2 = iterator.next();
					if (card2.defense <= card.attack) {
						id2 = card2.instanceId;
						iterator.remove();
						break;
					}
				}
			}
			if (id2 != -1) {
				listPickId.add("ATTACK " + card.instanceId + " " + id2);
			} else {
				listPick.add("ATTACK " + card.instanceId + " " + id2);
			}

		}

		for (Card card : cardsMainJoueurActif) {
			System.err.println("Main");
			System.err.println(card);
			if (card.cost < playerMana) {
				sortie.append("SUMMON " + card.instanceId);
				i++;
				break;
			}
		}
		int longeurListe = listPick.size() + listPickId.size() + 1;
		for (String string : listPickId) {
			if (i > 0 && i < longeurListe) {
				sortie.append(";");
			}
			sortie.append(string);
			i++;
		}
		for (String string : listPick) {
			if (i > 0 && i < longeurListe) {
				sortie.append(";");
			}
			sortie.append(string);
			i++;
		}

		return sortie.toString();
	}

	private static int coutCarte(Card card) {
		int cout = 0;
		if (card.attack != 0) {
			cout = card.cost / card.attack;
		}

		return cout;
	}
}

class Joueur {
	int playerHealth;
	int playerMana;
	int playerDeck;
	int playerRune;

	@Override
	public String toString() {
		return "Joueur [playerHealth=" + playerHealth + ", playerMana=" + playerMana + ", playerDeck=" + playerDeck
				+ ", playerRune=" + playerRune + "]";
	}

}

class Card {
	int cardNumber;
	int instanceId;
	int location;
	int cardType;
	int cost;
	int attack;
	int defense;
	String abilities;
	int myHealthChange;
	int opponentHealthChange;
	int cardDraw;

	@Override
	public String toString() {
		return "Card [cardNumber=" + cardNumber + ", instanceId=" + instanceId + ", location=" + location
				+ ", cardType=" + cardType + ", cost=" + cost + ", attack=" + attack + ", defense=" + defense
				+ ", abilities=" + abilities + ", myHealthChange=" + myHealthChange + ", opponentHealthChange="
				+ opponentHealthChange + ", cardDraw=" + cardDraw + "]";
	}

}
