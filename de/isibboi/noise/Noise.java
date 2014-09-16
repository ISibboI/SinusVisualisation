package de.isibboi.noise;

import java.awt.*;
import java.awt.image.*;
import java.math.*;
import java.security.*;
import java.util.*;
import javax.swing.*;

public class Noise {
	private static final int IMAGE_WIDTH = 800;
	private static final int IMAGE_HEIGHT = 600;

	public static void main(String[] args) {
		Noise n = new Noise();
	}
	
	private final SecureRandom sr;
	private final long seed;
	
	public Noise() {
		JFrame noiseFrame = new JFrame("Noise");
		noiseFrame.getContentPane().setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		noiseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		noiseFrame.pack();
		noiseFrame.setResizable(false);
		noiseFrame.setVisible(true);
		
		sr = new SecureRandom();
		seed = sr.nextLong();
		
		BufferedImage noiseImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < noiseImage.getWidth(); x++) {
			for (int y = 0; y < noiseImage.getHeight(); y++) {
				noiseImage.setRGB(x, y, noiseFunction(x, y));
			}
		}
		
		noiseFrame.getContentPane().getGraphics().drawImage(noiseImage, 0, 0, null);
		
		System.out.println("Image shown. Finished.");
	}
	
	public int noiseFunction(double x, double y) {
		double dx = x - IMAGE_WIDTH / 2;
		double dy = y - IMAGE_HEIGHT / 2;
		double distance = Math.sqrt(dx * dx + dy * dy);
		dx /= distance;
		dy /= distance;
		
		final double trigonometricParameter = distance / 20;
		x -= dx * Math.sin(trigonometricParameter) * 5;
		y -= dy * Math.sin(trigonometricParameter) * 5;
		
		int blockX = (int) (x / 20);
		int blockY = (int) (y / 20);
	
		Random r = new Random(seed ^ (blockX << 5) ^ (blockY << 13) ^ (blockX << 31) ^ (blockY << 43));
		
		for (int i = 0; i < 10; i++) {
			r.nextLong();
		}
		
		Color c = new Color((int) r.nextInt());
		
		/*double shade = -Math.cos(trigonometricParameter);
		shade = Math.max(0, shade);
		shade *= 0.5;
		shade += 0.5;
		
		c = new Color((int) (c.getRed() * shade), (int) (c.getGreen() * shade), (int) (c.getBlue() * shade));*/
		
		return c.getRGB();
	}
}
