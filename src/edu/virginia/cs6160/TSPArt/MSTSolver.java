package edu.virginia.cs6160.TSPArt;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class MSTSolver implements TSPSolver {
	//private List<Node> nodes;
	private DistanceMatrix distances;

	public Tour solve(List<Node> nodes) {
		//this.nodes = nodes;
		this.distances = new DistanceMatrix(nodes);

		Tour tour = new Tour();

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
					// this is equivalence of decreaseKey (note the order is important)
					Q.remove(v);
					v.key = distances.getDistance(u, v);
					Q.add(v);
					
					v.prev = u;
				}
			}
		}
		
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
