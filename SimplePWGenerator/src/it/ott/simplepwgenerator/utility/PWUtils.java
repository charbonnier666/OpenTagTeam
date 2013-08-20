package it.ott.simplepwgenerator.utility;

import java.util.Random;

public final class PWUtils {
	public static String generatePassword(int length, boolean sc, boolean num) {
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < length; i++) {
			Random r = new Random();
			int randomAB = r.nextInt(26);
			int randomSC = r.nextInt(28);
			int randomNUM = r.nextInt(10);
			if (!sc && !num) {
				int random = r.nextInt(2);
				if (random == 0)
					password.append(Costants.CAPITALLETTERS.charAt(randomAB));
				else if (random == 1)
					password.append(Costants.LETTERS.charAt(randomAB));
			} else if (!sc && num) {
				int random = r.nextInt(3);
				if (random == 0)
					password.append(Costants.CAPITALLETTERS.charAt(randomAB));
				else if (random == 1)
					password.append(Costants.LETTERS.charAt(randomAB));
				else if (random == 2)
					password.append(Costants.NUMBERS.charAt(randomNUM));
			} else if (!num && sc) {
				int random = r.nextInt(3);
				if (random == 0)
					password.append(Costants.CAPITALLETTERS.charAt(randomAB));
				else if (random == 1)
					password.append(Costants.LETTERS.charAt(randomAB));
				else if (random == 2)
					password.append(Costants.SPECIALCHARS.charAt(randomSC));
			} else {
				int random = r.nextInt(4);
				if (random == 0)
					password.append(Costants.CAPITALLETTERS.charAt(randomAB));
				else if (random == 1)
					password.append(Costants.LETTERS.charAt(randomAB));
				else if (random == 2)
					password.append(Costants.NUMBERS.charAt(randomNUM));
				else if (random == 3)
					password.append(Costants.SPECIALCHARS.charAt(randomSC));
			}
		}
		return password.toString();
	}
}
