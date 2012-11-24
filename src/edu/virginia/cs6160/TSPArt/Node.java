package edu.virginia.cs6160.TSPArt;

public class Node {
	protected int x;
	protected int y;
	protected boolean visited;
	protected int id;

	public Node(int x, int y, int id, boolean visited) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.visited = visited;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "(" + x + ", " + y + "), visited: " + visited;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node other = (Node) obj;
			return (other.x == this.x && other.y == this.y && other.visited == this.visited);
		}

		return false;
	}
}
