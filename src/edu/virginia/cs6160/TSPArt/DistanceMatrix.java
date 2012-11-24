package edu.virginia.cs6160.TSPArt;

import java.util.List;

public class DistanceMatrix {
	protected int[][] distances;
	protected List<Node> nodes;

	public DistanceMatrix(List<Node> nodes) {
		this.nodes = nodes;
		
		// pre-calculate distances
		distances = new int[nodes.size()][nodes.size()]; 
		
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				Node nodeI = nodes.get(i);
				Node nodeJ = nodes.get(j);
				
				Line tempLine = new Line(nodeI, nodeJ);
				distances[nodeI.id][nodeJ.id] = tempLine.distance;
				distances[nodeJ.id][nodeI.id] = tempLine.distance;
			}
		}
	}
	
	public int getDistance(Node i, Node j) {
		return distances[i.id][j.id];
	}
}
