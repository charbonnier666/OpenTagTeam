package it.ott.simplepwgenerator.utility;

import java.util.ArrayList;
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

	public static ArrayList<String> preparePasswordFamiliar() {
		// get quotes
		QuoteConnection q = new QuoteConnection();
		q.execute("");
		while (q.getQuotes() == null) {
			;// wait
		}
		ArrayList<String> quotes = q.getQuotes();
		return quotes;
	}

	public static String generatePasswordFamiliar(int length, boolean sc, boolean num, ArrayList<String> quotes) {
		StringBuilder password = new StringBuilder();
		int max = length / 2;
		for (int i = 0; i < quotes.size(); i++) {
			if (quotes.get(i).length() > 1) {
				String quote = quotes.get(i);
				quote = quote.replaceAll("\\W", "");
				password.append(Character.toUpperCase(quote.charAt(0))).append(quote.substring(1));
				if (password.length() > max)
					break;
			}
		}
		for (int i = password.length(); i < length; i++) {
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