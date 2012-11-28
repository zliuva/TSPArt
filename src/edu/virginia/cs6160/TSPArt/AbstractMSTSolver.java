package edu.virginia.cs6160.TSPArt;

import java.util.List;
import java.util.PriorityQueue;

public abstract class AbstractMSTSolver {
	private DistanceMatrix distances;

	protected void generateMST(List<Node> nodes) {
		this.distances = new DistanceMatrix(nodes);

		// Prim's algorithm
		boolean[] used = new boolean[nodes.size()];

		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).key = Integer.MAX_VALUE;
			used[i] = false;
		}

		nodes.get(0).key = 0;
		nodes.get(0).prev = null;

		PriorityQueue<Node> Q = new PriorityQueue<Node>();

		for (Node node : nodes) {
			Q.add(node);
		}

		while (!Q.isEmpty()) {
			Node u = Q.poll();
			used[u.id] = true;

			for (Node v : nodes) {
				if (!used[v.id] && distances.getDistance(u, v) < v.key) {
					// this is equivalence of decreaseKey (note the order is
					// important)
					Q.remove(v);
					v.key = distances.getDistance(u, v);
					Q.add(v);

					v.prev = u;
				}
			}
		}
	}

}
