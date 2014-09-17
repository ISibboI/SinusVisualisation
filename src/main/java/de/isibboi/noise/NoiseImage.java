package de.isibboi.noise;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.math.*;
import java.nio.*;
import java.security.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

import de.isibboi.noise.function.Function;

public class NoiseImage {
	private static final int IMAGE_WIDTH = 400;
	private static final int IMAGE_HEIGHT = 400;
	private static final int NOISE_WIDTH = 20;
	private static final int NOISE_HEIGHT = 20;
	private static final double AMBIENT_LIGHT = 0.1;
	
	private final SecureRandom sr;
	private final long seed;
	private final Function f;
	private final Camera c;
	private final ColorMap colorMap;
	
	private boolean ascensionOverflow = false;
	private boolean shadeOverflow = false;
	
	public NoiseImage(Function f, int scale, int noiseScale) throws Exception {
		BufferedImage noiseImage = new BufferedImage(IMAGE_WIDTH * scale, IMAGE_HEIGHT * scale, BufferedImage.TYPE_INT_RGB);
		
		JFrame noiseFrame = new JFrame("Noise");
		noiseFrame.getContentPane().setPreferredSize(new Dimension(noiseImage.getWidth(), noiseImage.getHeight()));
		noiseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		noiseFrame.pack();
		noiseFrame.setResizable(false);
		noiseFrame.setVisible(true);
		
		sr = new SecureRandom();
		seed = sr.nextLong();
		this.f = f;
		
//		c = new ParallelCamera(new Vector(20, 20, 20),
//				new Vector(-1, -1, -1),
//				new Vector(-0.5, 1, -0.5));
//		c = new ParallelCamera(new Vector(20, 20, Math.sqrt(2) * 20),
//				new Vector(-1, -1, -Math.sqrt(2)),
//				new Vector(-0.5, 2.5, -Math.sqrt(2)));
		Vector cameraPosition = new Vector(1, 1, Math.sqrt(2))
				.multiplyScalar(12);
		c = new PerspectiveCamera(cameraPosition,
				cameraPosition.multiplyScalar(-1),
				new Vector(-0.5, 2.5, -Math.sqrt(2)),
				cameraPosition.multiplyScalar(2.8));
		
		colorMap = new ColorMap(seed);
		
		final int realNoiseWidth = NOISE_WIDTH * noiseScale;
		final int realNoiseHeight = NOISE_HEIGHT * noiseScale;
		
		int oldPercent = 0;
		int count = 0;
		final int pixelAmount = noiseImage.getWidth() * noiseImage.getHeight();
		for (int x = 0; x < noiseImage.getWidth(); x++) {
			for (int y = 0; y < noiseImage.getHeight(); y++) {
				noiseImage.setRGB(x, y, noiseFunction((double) x / noiseImage.getWidth() * realNoiseWidth - (realNoiseWidth / 2),
								  (double) y / noiseImage.getHeight() * realNoiseHeight - (realNoiseHeight / 2)));
				
				count++;
				int newPercent = 100 * count / pixelAmount;
				
				if (oldPercent != newPercent) {
					System.out.println(newPercent + "%");
					oldPercent = newPercent;
				}
			}
		}
		
		try {
			ImageIO.write(noiseImage, "png", new File("noise.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		noiseFrame.getContentPane().getGraphics().drawImage(noiseImage, 0, 0, null);
		
		System.out.println("Image shown. Finished.");
	}
	
	public int noiseFunction(final double x, final double y) throws Exception {
		Vector hit = c.renderPoint(x, y, f);
		
		if (hit == null) {
			return 0;
		}
		
//		System.out.println("(x, y): (" + x + ", " + y + "), hit: ("
//				+ hit.getX() + ", " + hit.getY() + ")");
		
		if (!f.inRange(hit.getX(), hit.getY())) {
			return 0;
		}
		
		int blockX = doubleToInt(hit.getX());
		int blockY = doubleToInt(hit.getY());
	
		Color c = new Color(colorMap.getColor(blockX, blockY));
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		
		// Some color modifications.
		if (hit.getZ() > 0.3) {
			double factor = Math.pow(0.25, hit.getZ() - 0.3);
			red = (int) (red * factor);
			green = 255 - (int) ((255 - green) * factor);
		}
		
		if (hit.getZ() < 0.3) {
			double factor = Math.pow(8, hit.getZ() - 0.3);
			green = (int) (green * factor);
			red = 255 - (int) ((255 - red) * factor);
		}
		
		blue /= 6;
		
		// Calculate ascension.
		double ascension = f.derivedValue(hit.getX(), hit.getY());
		
		if (ascension > 1) {
			if (!ascensionOverflow) {
				System.out.println("Ascension: " + ascension);
				ascensionOverflow = true;
			}
			
			ascension = 1;
		}
		
		// Convert ascension to light level.
		double shade = Math.sin(Math.PI / 4 + Math.atan(ascension));
		
		if ((shade > 1 || shade < 0) && !shadeOverflow) {
			System.out.println("Shade: " + shade);
			shadeOverflow = true;
		}
		
		// Add ambient light. Shade is between 0 and 1 here.
		shade *= 1 - AMBIENT_LIGHT;
		shade += AMBIENT_LIGHT;
		
		c = new Color((int) (red * shade), (int) (green * shade), (int) (blue * shade));
		
//		System.exit(0);
		
		return c.getRGB();
	}
	
	private int doubleToInt(double d) {
		if (d >= 0) {
			return (int) d;
		} else {
			return -((int) (1 - d));
		}
	}
}
