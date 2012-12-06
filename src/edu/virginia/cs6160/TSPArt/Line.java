package edu.virginia.cs6160.TSPArt;

public class Line {
	protected Node start;
	protected Node end;
	protected int distance;

	// the following is used for intersection detection
	private double a, b; // solve the equation y = a * x + b
	private boolean noSlope; // special case x = constant

	Line(Node start, Node end) {
		this.start = start;
		this.end = end;

		// the distance and line parameters are pre-calculated to save time
		// later

		this.distance = (end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y);

		if (start.x == end.x) {
			noSlope = true;
		} else {
			noSlope = false;

			a = (start.y - end.y) / (start.x - end.x);
			b = start.y - a * start.x;
		}
	}

	@Override
	public String toString() {
		return "(" + start.x + ", " + start.y + ")--(" + end.x + ", " + end.y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Line) {
			Line line = (Line) obj;
			return (start.x == line.start.x && end.x == line.end.x && start.y == line.start.y && end.y == line.end.y)
					|| (end.x == line.start.x && end.y == line.start.y && start.x == line.end.x && start.y == line.start.y);
		}

		return false;
	}

	public boolean intersects(Line line) {
		// parallel lines
		if ((this.noSlope && line.noSlope) || (this.a - line.a < 0.0001)) {
			return false;
		}

		// or else solve the equation and find intersection (x, y)
		double x = (line.b - this.b) / (this.a - line.a);
		double y = this.a * x + this.b;

		// the intersection (x, y) should be on both line segments

		return (x >= Math.min(this.start.x, this.end.x) && x >= Math.min(line.start.x, line.end.x)
				&& x <= Math.max(this.start.x, this.end.x) && x <= Math.max(line.start.x, line.end.x)
				&& y >= Math.min(this.start.y, this.end.y) && y >= Math.min(line.start.y, line.end.y)
				&& y <= Math.max(this.start.y, this.end.y) && y <= Math.max(line.start.y, line.end.y));
	}
}
