package edu.virginia.cs6160.TSPArt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TSPArtCmd {
	public static void main(String[] args) {
		BufferedImage image = null;
		File inputFile = null;

		try {
			inputFile = new File(args[0]);
			image = ImageIO.read(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		TSPArtGenerator generator = new TSPArtGenerator(image, TSPSolverFactory.getSolver("NN"),
				TSPFixerFactory.getFixer("2-Opt"));
		BufferedImage resultImage = generator.getTSPArtImage();

		try {
			ImageIO.write(resultImage, "png",
					new File(inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.')) + "_TSPArt.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
