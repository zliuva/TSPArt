package edu.virginia.cs6160.TSPArt;

public class TSP2OptFixer implements TSPFixer {
	private void fixIntersection(Tour tour, int a, int b) {
		// make sure a < b so we don't have to do a lot of ifs
		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}

		// make a copy of lines from a to b ( this array is oversized so we can
		// use the same indices between temp and tour )
		Line[] tempLines = new Line[tour.lines.size()];

		// now the magic happens
		// instead of using "real" 2-opt, we use a heuristic where we always
		// connect the start points and end points, so we can also reverse the
		// direction of the lines in between (so that no subgraphs are created,
		// since the TSP still traverse in one go)
		Node startA = tour.lines.get(a).start;
		Node endA = tour.lines.get(a).end;
		Node startB = tour.lines.get(b).start;
		Node endB = tour.lines.get(b).end;

		tour.lines.set(a, new Line(startA, startB));
		for (int i = a + 1; i < b; i++) {
			tempLines[i] = new Line(tour.lines.get(i).end, tour.lines.get(i).start);
		}
		for (int i = a + 1; i < b; i++) {
			tour.lines.set(i, tempLines[b + a - i]);
		}
		tour.lines.set(b, new Line(endA, endB));
	}
	
	public void removeIntersections(Tour tour) {
		removeIntersections(tour, 1);
	}

	public void removeIntersections(Tour tour, int pass) {
		boolean found = true;

		while (found && pass-- > 0) {
			found = false;
			for (int i = 0; i < tour.lines.size(); i++) {
				for (int j = 0; j < tour.lines.size(); j++) {
					if (i == j || i == j + 1 || j == i + 1) {
						continue;
					}

					Line line1 = tour.lines.get(i);
					Line line2 = tour.lines.get(j);

					if (line1.equals(line2)) {
						continue;
					}

					if (line1.intersects(line2)) {
						found = true;
						System.out.println("Fixing " + i + ", " + j);
						fixIntersection(tour, i, j);
					}
				}
			}
		}
	}

}
