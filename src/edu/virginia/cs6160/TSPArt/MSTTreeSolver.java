package edu.virginia.cs6160.TSPArt;

import java.util.List;

public class MSTTreeSolver extends AbstractMSTSolver implements TSPSolver {
	public Tour solve(List<Node> nodes) {
		Tour tour = new Tour();

		generateMST(nodes);

		for (Node start : nodes) {
			for (Node end : nodes) {
				if (end.prev == start) {
					tour.lines.add(new Line(start, end));
				}
			}
		}

		return tour;
	}
}
