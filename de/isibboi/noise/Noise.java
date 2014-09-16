package de.isibboi.noise;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class Noise {
	private static final int IMAGE_WIDTH = 800;
	private static final int IMAGE_HEIGHT = 600;

	public static void main(String[] args) {
		JFrame noiseFrame = new JFrame("Noise");
		noiseFrame.getContentPane().setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		noiseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		noiseFrame.pack();
		noiseFrame.setResizable(false);
		noiseFrame.setVisible(true);
		
		BufferedImage noiseImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < noiseImage.getWidth(); x++) {
			for (int y = 0; y < noiseImage.getHeight(); y++) {
				noiseImage.setRGB(x, y, noiseFunction(x, y));
			}
		}
		
		noiseFrame.getContentPane().getGraphics().drawImage(noiseImage, 0, 0, null);
		
		System.out.println("Image shown. Finished.");
	}
	
	public static int noiseFunction(int x, int y) {
		return 0;
	}
}
