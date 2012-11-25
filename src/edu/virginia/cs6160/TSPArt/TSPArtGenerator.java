package edu.virginia.cs6160.TSPArt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPArtGenerator {
	private BufferedImage originalImage;
	private BufferedImage resultImage;
	private List<Node> nodes;
	private TSPSolver solver;
	private TSPFixer fixer;
	private boolean randomizeDots;
	
	public TSPArtGenerator(BufferedImage image, TSPSolver solver) {
		this(image, solver, null);
	}

	public TSPArtGenerator(BufferedImage image, TSPSolver solver, TSPFixer fixer) {
		this(image, solver, fixer, true);
	}
	
	public TSPArtGenerator(BufferedImage image, TSPSolver solver, TSPFixer fixer, boolean randomizeDots) {
		this.originalImage = image;
		this.solver = solver;
		this.fixer = fixer;
		this.randomizeDots = randomizeDots;

		resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resultImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, resultImage.getWidth(), resultImage.getHeight());
		g.dispose();

		nodes = new ArrayList<Node>(resultImage.getWidth() * resultImage.getHeight());
	}

	public BufferedImage getTSPArtImage() {
		BufferedImage bwImage = toBinaryImage(originalImage);
		slidingWindowPixelate(bwImage);

		// draw the dots first
		for (Node node : nodes) {
			resultImage.setRGB(node.x, node.y, Color.black.getRGB());
		}

		Tour tour = solver.solve(nodes);
		
		if (fixer != null) {
			fixer.removeIntersections(tour);
		}

		Graphics2D g = resultImage.createGraphics();
		g.setColor(Color.black);
		BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		g.setStroke(bs);

		for (Line line : tour.lines) {
			g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
		}

		return resultImage;
	}

	private BufferedImage toBinaryImage(BufferedImage image) {
		BufferedImage bwImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_BYTE_BINARY);

		Graphics2D g = (Graphics2D) bwImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bwImage;
	}

	private void slidingWindowPixelate(BufferedImage image) {
		int slidingWindow = 4;
		int threshold = 4;
		int id = 0;

		Random random = new Random(0);

		int blackRGB = Color.black.getRGB();
		for (int i = 0; i < image.getWidth(); i += slidingWindow) {
			for (int j = 0; j < image.getHeight(); j += slidingWindow) {
				int count = 0;
				boolean hasBlack = false;

				for (int k = i; k < i + slidingWindow && k < image.getWidth(); k++) {
					for (int m = j; m < j + slidingWindow && m < image.getHeight(); m++) {

						int rgb = image.getRGB(k, m);

						if (rgb == blackRGB) {
							count++;
						}

						if (count > threshold) {
							int x  = k;
							int y = m;
							
							if (randomizeDots) {
								x += random.nextInt(slidingWindow / 2);
								y += random.nextInt(slidingWindow / 2);
							}
							
							if (x >= image.getWidth()) {
								x = image.getWidth() - 1;
							}
							
							if (y >= image.getHeight()) {
								y = image.getHeight() - 1;
							}

							nodes.add(new Node(x, y, id++, false));
							hasBlack = true;
							break;
						}
					}
					
					if (hasBlack) {
						break;
					}
				}

			}
		}

	}
}
