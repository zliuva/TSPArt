package edu.virginia.cs6160.TSPArt;

public interface TSPFixer {
	public void removeIntersections(Tour tour);
	public void removeIntersections(Tour tour, int pass);
}
