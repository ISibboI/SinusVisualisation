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

public class Noise {
	private static final int SCALE = 1;
	private static final int IMAGE_WIDTH = 400 * SCALE;
	private static final int IMAGE_HEIGHT = 400 * SCALE;
	private static final int NOISE_WIDTH = 20;
	private static final int NOISE_HEIGHT = 20;
	private static final double AMBIENT_LIGHT = 0.1;

	public static void main(String[] args) throws Exception {
		Noise n = new Noise();
	}
	
	private final SecureRandom sr;
	private final long seed;
	private final MessageDigest md5;
	
	public Noise() throws Exception {
		JFrame noiseFrame = new JFrame("Noise");
		noiseFrame.getContentPane().setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		noiseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		noiseFrame.pack();
		noiseFrame.setResizable(false);
		noiseFrame.setVisible(true);
		
		sr = new SecureRandom();
		seed = sr.nextLong();
		md5 = MessageDigest.getInstance("MD5");
		
		BufferedImage noiseImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < noiseImage.getWidth(); x++) {
			for (int y = 0; y < noiseImage.getHeight(); y++) {
				noiseImage.setRGB(x, y, noiseFunction((double) x / noiseImage.getWidth() * NOISE_WIDTH,
								  (double) y / noiseImage.getHeight() * NOISE_HEIGHT));
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
	
	public int noiseFunction(double x, double y) throws Exception {
		x -= NOISE_WIDTH / 2;
		y -= NOISE_HEIGHT / 2;
		double distance = Math.sqrt(x * x + y * y);
		
		// Translate position to get an angled parallel projection effect.
		x -= Math.sin(distance) * 0.5;
		y -= Math.sin(distance) * 0.5;
		
		int blockX = (int) (x);
		int blockY = (int) (y);
	
		Color c = new Color(getRandomNumber(seed, blockX, blockY));
		
		// Calculate ascension.
		double shade = directionalDeriverative(x, y);
		
		if (shade > 1 || shade < -1) {
			System.out.println("Ascension: " + shade);
		}
		
		// Convert ascension to light level.
		shade = Math.sin(Math.PI / 4 - Math.atan(shade));
		
		if (shade > 1 || shade < 0) {
			System.out.println("Shade: " + shade);
		}
		
		// Add ambient light. Shade is between 0 and 1 here.
		shade *= 1 - AMBIENT_LIGHT;
		shade += AMBIENT_LIGHT;
		
		c = new Color((int) (c.getRed() * shade), (int) (c.getGreen() * shade), (int) (c.getBlue() * shade));
		
		return c.getRGB();
	}
	
	public double directionalDeriverative(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
	
		return Math.cos(absoluteValue) * (x + y) / absoluteValue / Math.sqrt(2);
	}
	
	public int getRandomNumber(long... parameters) throws Exception {
		md5.reset();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		
		for (long l: parameters) {
			data.writeLong(l);
		}
		
		byte[] digest = md5.digest(bytes.toByteArray());
		
		return digest[0] + (digest[1] << 8) + (digest[2] << 16) + (digest[2] << 24);
	}
}
