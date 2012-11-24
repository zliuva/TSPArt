package edu.virginia.cs6160.TSPArt;

import java.util.List;

public class NNSolver implements TSPSolver {
	private List<Node> nodes;
	private DistanceMatrix distances;

	private Node getNearestNode(Node source) {
		int currentDistance = Integer.MAX_VALUE;
		Node nearestNode = null;

		for (Node node : nodes) {
			if (!node.visited) {
				int distance = distances.getDistance(source, node);
				if (currentDistance > distance) {
					currentDistance = distance;
					nearestNode = node;
				}
			}
		}

		return nearestNode;
	}

	public Tour solve(List<Node> nodes) {
		this.nodes = nodes;
		this.distances = new DistanceMatrix(nodes);

		Tour tour = new Tour();
		
		Node startNode = nodes.get(0);
		startNode.visited = true;
		
		Node nearestNode = getNearestNode(startNode);
		
		while(nearestNode != null)
		{
			Line line = new Line(startNode, nearestNode);
			tour.lines.add(line);
			nearestNode.visited = true;

			startNode = nearestNode;
			nearestNode = getNearestNode(startNode);
		}

		return tour;
	}
}
