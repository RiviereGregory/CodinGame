
// Read inputs from System.in, Write outputs to System.out.
// Your class name has to be Solution
import java.util.*;

class Solution {
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		ArrayList<Integer> number[];
		int compteur = 0;
		number = new ArrayList[3 * n];
		number[compteur] = new ArrayList<Integer>();
		number[compteur].add(in.nextInt());
		number[compteur].add(in.nextInt());
		for (int i = 1; i < n; i++) {
			int nb1 = in.nextInt();
			int nb2 = in.nextInt();
			System.err.println("nb1 " + nb1 + " - nb2 " + nb2);
			int addOk = 0;
			int compteurTmp = compteur;
			for (int j = 0; j <= compteurTmp; j++) {
				if (number[j].indexOf(nb2) != 0 && number[j].indexOf(nb2) != -1 && number[j].indexOf(nb1) == -1
						&& number[j].indexOf(nb2) != (number[j].size() - 1)) {
					System.err.println("A");
					compteur++;
					number[compteur] = new ArrayList<Integer>();
					for (int k = number[j].indexOf(nb2); k < number[j].size(); k++) {
						number[compteur].add(number[j].get(k));
					}
					number[compteur].add(0, nb1);
					addOk = 1;
				} else if (number[j].indexOf(nb2) == 0) {
					System.err.println("B");
					number[j].add(0, nb1);
					addOk = 1;
				} else if (number[j].indexOf(nb1) == (number[j].size() - 1)) {
					System.err.println("C");
					number[j].add(nb2);
					addOk = 1;
				} else if (number[j].indexOf(nb1) != (number[j].size() - 1) && number[j].indexOf(nb1) != -1
						&& number[j].indexOf(nb2) == -1 && number[j].indexOf(nb1) != 0) {
					System.err.println("D");
					compteur++;
					number[compteur] = new ArrayList<Integer>();
					for (int k = 0; k <= number[j].indexOf(nb1); k++) {
						number[compteur].add(number[j].get(k));
					}
					number[compteur].add(nb2);
					addOk = 1;
				} else if (number[j].indexOf(nb1) != -1 && number[j].indexOf(nb2) != -1
						&& number[j].indexOf(nb1) != (number[j].indexOf(nb2) - 1)) {
					System.err.println("E");
					compteur++;
					number[compteur] = new ArrayList<Integer>();
					for (int k = number[j].indexOf(nb2); k < number[j].size(); k++) {
						number[compteur].add(number[j].get(k));
					}
					number[compteur].add(0, nb1);
					addOk = 1;
				}
			}
			if (addOk == 0) {
				System.err.println("NEW");
				compteur++;
				number[compteur] = new ArrayList<Integer>();
				number[compteur].add(nb1);
				number[compteur].add(nb2);
			}
		}
		int sizeMax = 0;
		for (int j = 0; j <= compteur; j++) {
			if (sizeMax < number[j].size()) {
				sizeMax = number[j].size();
			}

			System.err.println();
			for (int ii = 0; ii < number[j].size(); ii++) {
				System.err.print(" " + number[j].get(ii));
			}
			System.err.println();

		}
		System.out.println(sizeMax);
	}
}

