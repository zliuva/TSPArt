package edu.virginia.cs6160.TSPArt;

import java.util.ArrayList;

public class NNSolver implements TSPSolver {
	private ArrayList<Node> nodes;

	private Node getNearestNode(Node source) {
		int currentDistance = Integer.MAX_VALUE;
		Node nearestNode = null;

		for (Node node : nodes) {
			if (!node.visited) {
				Line line = new Line(source, node);
				if (currentDistance > line.distance) {
					currentDistance = line.distance;
					nearestNode = node;
				}
			}
		}

		return nearestNode;
	}

	public Tour solve(ArrayList<Node> nodes) {
		this.nodes = nodes;

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
