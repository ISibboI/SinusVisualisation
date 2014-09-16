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
				noiseImage.setRGB(x, y, noiseFunction(x, y));
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
		double dx = x - IMAGE_WIDTH / 2;
		double dy = y - IMAGE_HEIGHT / 2;
		double distance = Math.sqrt(dx * dx + dy * dy);
		
		final double trigonometricParameter = distance / 10 / SCALE;
		x -= Math.sin(trigonometricParameter) * 2.5 * SCALE;
		y -= Math.sin(trigonometricParameter) * 2.5 * SCALE;
		
		int blockX = (int) (x / 10 / SCALE);
		int blockY = (int) (y / 10 / SCALE);
	
		Color c = new Color(getRandomNumber(seed, blockX, blockY));
		
		double shade = Math.cos(trigonometricParameter);
		shade = -Math.min(0, shade);
		shade *= 0.35;
		shade += 0.65;
		
		c = new Color((int) (c.getRed() * shade), (int) (c.getGreen() * shade), (int) (c.getBlue() * shade));
		
		return c.getRGB();
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
