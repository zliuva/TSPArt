package edu.virginia.cs6160.TSPArt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MSTTSPSolver extends AbstractMSTSolver implements TSPSolver {
	public Tour solve(List<Node> nodes) {
		Tour tour = new Tour();

		generateMST(nodes);

		List<Node> path = new ArrayList<Node>(nodes.size());
		Stack<Node> toVisit = new Stack<Node>();

		toVisit.push(nodes.get(0));

		while (!toVisit.isEmpty()) {
			Node start = toVisit.pop();
			path.add(start);

			for (Node end : nodes) {
				if (end.prev == start && !path.contains(end)) {
					toVisit.push(end);
				}
			}
		}

		for (int i = 0; i < path.size() - 1; i++) {
			tour.lines.add(new Line(path.get(i), path.get(i + 1)));
		}

		return tour;
	}
}
