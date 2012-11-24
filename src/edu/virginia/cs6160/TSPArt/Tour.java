package edu.virginia.cs6160.TSPArt;

import java.util.ArrayList;
import java.util.List;

public class Tour {
	protected List<Line> lines;

	public Tour() {
		lines = new ArrayList<Line>();
	}

	public int getDistance() {
		int distance = 0;

		for (Line line : lines) {
			distance += line.distance;
		}

		return distance;
	}

	@Override
	public String toString() {
		String temp = ":";

		for (Line line : lines) {
			temp += line.toString();
			temp += "|";
		}
		
		return temp;
	}
}
