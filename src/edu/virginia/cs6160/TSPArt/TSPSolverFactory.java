package edu.virginia.cs6160.TSPArt;

public class TSPSolverFactory {
	public static TSPSolver getSolver(String solverName) {
		if (solverName.equalsIgnoreCase("NN")) {
			return new NNSolver();
		}
		
		return null;
	}
}
