
/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

public class NameSurferEntry implements NameSurferConstants {
	/* Constructor: NameSurferEntry(line) */
	/**
	 * Creates a new NameSurferEntry from a data line as it appears in the data
	 * file. Each line begins with the name, which is followed by integers giving
	 * the rank of that name for each decade.
	 */
	public NameSurferEntry(String line) {
		line = line + ' ';
		int x = line.indexOf(' ');
		int y = line.length();
		name = line.substring(0, x);
		String theRestOfALine = line.substring(x + 1, y);
		for (int i = 0; i < NDECADES; i++) {
			int a = theRestOfALine.indexOf(' ');
			int b = theRestOfALine.length();
			String rank = theRestOfALine.substring(0, a);
			this.rank[i] = Integer.parseInt(rank);
			theRestOfALine = theRestOfALine.substring(a + 1, b);
		}
		entry = name;
		for (int i = 0; i < NDECADES; i++) {
			if (i == 0) {
				entry = entry + " [" + rank[i];
				continue;
			}
			if (i == NDECADES - 1) {
				entry = entry + ' ' + rank[i] + ']';
				continue;
			}
			entry = entry + ' ' + rank[i];
		}
	}

	/* Method: getName() */
	/**
	 * Returns the name associated with this entry.
	 */
	public String getName() {
		return name;
	}

	/* Method: getRank(decade) */
	/**
	 * Returns the rank associated with an entry for a particular decade. The decade
	 * value is an integer indicating how many decades have passed since the first
	 * year in the database, which is given by the constant START_DECADE. If a name
	 * does not appear in a decade, the rank value is 0.
	 */
	public int getRank(int decade) {
		return rank[decade];
	}

	/* Method: toString() */
	/**
	 * Returns a string that makes it easy to see the value of a NameSurferEntry.
	 */
	public String toString() {
		return entry;
	}

	private String entry;
	private String name;
	private int[] rank = new int[NDECADES];
}
