import java.util.*;

class Solution {

	public static void main(String args[]) {
		int nbCoup = 100000;
		Bender bender = new Bender();
		bender.nbcoup = nbCoup;
		initJeu(bender);
		moteurJeu(bender);
		if (bender.compteur < bender.nbcoup - 1) {
			for (Direction reponse : bender.message) {
				System.out.println(reponse);
			}
		} else {
			System.out.println("LOOP");
		}

	}

	private static void initJeu(Bender bender) {
		Scanner in = new Scanner(System.in);
		int ligne = in.nextInt();
		int colonne = in.nextInt();
		String line = in.nextLine();
		bender.teleport = new int[2][2];
		int telportnb = 0;
		bender.carte = new Character[ligne][colonne];
		for (int i = 0; i < ligne; i++) {
			line = in.nextLine();
			for (int j = 0; j < colonne; j++) {
				bender.carte[i][j] = line.charAt(j);
				if (bender.carte[i][j].equals('@')) {
					bender.posX = i;
					bender.posY = j;
				}
				if (bender.carte[i][j].equals('T') && telportnb == 0) {
					bender.teleport[0][0] = i;
					bender.teleport[0][1] = j;
					telportnb = 1;
				} else if (bender.carte[i][j].equals('T') && telportnb == 1) {
					bender.teleport[1][0] = i;
					bender.teleport[1][1] = j;
				}
				System.err.print(bender.carte[i][j]);
			}
			System.err.println();
		}
	}

	private static void moteurJeu(Bender bender) {
		while (bender.compteur < bender.nbcoup && !bender.stop) {
			Character sud = bender.carte[bender.posX + 1][bender.posY];
			Character est = bender.carte[bender.posX][bender.posY + 1];
			Character nord = bender.carte[bender.posX - 1][bender.posY];
			Character ouest = bender.carte[bender.posX][bender.posY - 1];
			Direction message = null;
			
			switch (bender.orientation) {
			case 'S':				
				bender.compteur++;
				if (!(sud.equals('#') || sud.equals('X'))) {
					message = Direction.SOUTH;
					bender.posX += 1;
					bender.orientation = 'S';
					bender.boucle = 0;
				}else if(bender.modeCasseur && !(sud.equals('#'))){
					message = Direction.SOUTH;
					bender.posX += 1;
					bender.orientation = 'S';
					bender.boucle = 0;
					if (sud.equals('X')) {
						bender.carte[bender.posX][bender.posY] = ' ';
					}
				}else if(bender.boucle == 1){
					if(bender.modeInverseur){
						bender.orientation = 'W';
					}else {
						bender.orientation = 'E';
					}					
				}else {
					bender.boucle = 1;
					if(bender.modeInverseur){
						bender.orientation = 'W';
					}else {
						bender.orientation = 'E';
					}					
				}
				break;
			case 'E':
				bender.compteur++;
				if (!(est.equals('#') || est.equals('X'))) {
					message = Direction.EAST;
					// System.err.println("EAST");
					bender.posY += 1;
					bender.orientation = 'E';
					bender.boucle = 0;
				}else if(bender.modeCasseur && !(est.equals('#'))){
					message = Direction.EAST;
					bender.posY += 1;
					bender.orientation = 'E';
					bender.boucle = 0;
					if (est.equals('X')) {
						bender.carte[bender.posX][bender.posY] = ' ';
					}
				}else if(bender.boucle == 1){
					if(bender.modeInverseur){
						bender.orientation = 'S';
					}else {
						bender.orientation = 'N';
					}					
				}else {
					bender.boucle = 1;
					if(bender.modeInverseur){
						bender.orientation = 'W';
					}else {
						bender.orientation = 'S';
					}
				}
				break;
			case 'N':
				bender.compteur++;
				if (!(nord.equals('#') || nord.equals('X'))) {
					message = Direction.NORTH;
					bender.posX -= 1;
					bender.orientation = 'N';
					bender.boucle = 0;
				}else if(bender.modeCasseur && !(nord.equals('#'))){
					message = Direction.NORTH;
					bender.posX -= 1;
					bender.orientation = 'N';
					bender.boucle = 0;
					if (nord.equals('X')) {
						bender.carte[bender.posX][bender.posY] = ' ';
					}
				}else if(bender.boucle == 1){
					if(bender.modeInverseur){
						bender.orientation = 'E';
					}else {
						bender.orientation = 'W';
					}					
				}else {
					bender.boucle = 1;
					if(bender.modeInverseur){
						bender.orientation = 'W';
					}else {
						bender.orientation = 'S';
					}
				}
				break;
			case 'W':
				bender.compteur++;
				if (!(ouest.equals('#') || ouest.equals('X'))) {
					message = Direction.WEST;
					bender.posY -= 1;
					bender.orientation = 'W';
					bender.boucle = 0;
				}else if(bender.modeCasseur && !(ouest.equals('#'))){
					message = Direction.WEST;
					bender.posY -= 1;
					bender.orientation = 'W';
					bender.boucle = 0;
					if (ouest.equals('X')) {
						bender.carte[bender.posX][bender.posY] = ' ';
					}
				}else if(bender.boucle == 1){
					if(bender.modeInverseur){
						bender.orientation = 'N';
					}else {
						bender.orientation = 'S';
					}					
				}else {
					bender.boucle = 1;
					if(bender.modeInverseur){
						bender.orientation = 'N';
					}else {
						bender.orientation = 'S';
					}
				}
				break;
			default:
				break;
			}

			if (message != null) {
				bender.message.add(message);
			}
			if(bender.boucle==0){
				bender.getEtat();
			}
		}

	}

}

enum Direction {
	SOUTH, EAST, WEST, NORTH
}

class Bender {
	List<Direction> message = new ArrayList<>();
	int compteur = 0;
	int nbcoup;
	int posX;
	int posY;
	Character[][] carte;
	int teleport[][];
	Character orientation = 'S';
	boolean modeCasseur = false;
	boolean modeInverseur = false;
	boolean stop = false;
	int boucle = 0;

	void getEtat() {
		switch (this.carte[this.posX][this.posY]) {
		case 'I':
			if (this.boucle == 0) {
				if (!this.modeInverseur) {
					this.modeInverseur = true;
				} else if (this.modeInverseur) {
					this.modeInverseur = false;
				}
			}
			break;
		case 'T':
			if (this.teleport[0][0] == this.posX && this.teleport[0][1] == this.posY) {
				this.posX = this.teleport[1][0];
				this.posY = this.teleport[1][1];
			} else {
				this.posX = this.teleport[0][0];
				this.posY = this.teleport[0][1];
			}
			break;
		case 'B':
			if (!this.modeCasseur) {
				this.modeCasseur = true;
			} else {
				this.modeCasseur = false;
			}
			break;
		case 'X':
			if (this.modeCasseur) {
				this.carte[this.posX][this.posY] = ' ';
			}
			break;
		case 'S':
			this.orientation  = 'S';
			break;
		case 'N':
			this.orientation  = 'N';
			break;
		case 'E':
			this.orientation  = 'E';
			break;
		case 'W':
			this.orientation  = 'W';
			break;
		case '$':
			this.stop = true;
			break;
		}
	}

}
