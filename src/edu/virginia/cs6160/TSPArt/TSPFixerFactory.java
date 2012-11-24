package edu.virginia.cs6160.TSPArt;

public class TSPFixerFactory {
	public static TSPFixer getFixer(String fixerName) {
		if (fixerName.equalsIgnoreCase("2-Opt")) {
			return new TSP2OptFixer();
		}

		return null;
	}

}
