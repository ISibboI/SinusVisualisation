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
	private static final double PROJECTION_DISPLACEMENT = 0.5;
	private static final double AMBIENT_LIGHT = 0.1;
	
	private final SecureRandom sr;
	private final long seed;
	private final MessageDigest md5;
	private final Function f;
	
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
		md5 = MessageDigest.getInstance("MD5");
		this. f = f;
		
		
		
		int realNoiseWidth = NOISE_WIDTH * noiseScale;
		int realNoiseHeight = NOISE_HEIGHT * noiseScale;
		
		for (int x = 0; x < noiseImage.getWidth(); x++) {
			for (int y = 0; y < noiseImage.getHeight(); y++) {
				noiseImage.setRGB(x, y, noiseFunction((double) x / noiseImage.getWidth() * realNoiseWidth - (realNoiseWidth / 2),
								  (double) y / noiseImage.getHeight() * realNoiseHeight - (realNoiseHeight / 2)));
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
		double value = f.value(x, y);
		
		// Translate position to get an angled parallel projection effect.
		x += value * PROJECTION_DISPLACEMENT;
		y += value * PROJECTION_DISPLACEMENT;
		
		int blockX = doubleToInt(x);
		int blockY = doubleToInt(y);
	
		Color c = new Color(getRandomNumber(seed, blockX, blockY));
		
		// Calculate ascension.
		double ascension = f.derivedValue(x, y);
		
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
		
		c = new Color((int) (c.getRed() * shade), (int) (c.getGreen() * shade), (int) (c.getBlue() * shade));
		
		return c.getRGB();
	}
	
	private int getRandomNumber(long... parameters) throws Exception {
		md5.reset();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		
		for (long l: parameters) {
			data.writeLong(l);
		}
		
		byte[] digest = md5.digest(bytes.toByteArray());
		
		return digest[0] + (digest[1] << 8) + (digest[2] << 16) + (digest[2] << 24);
	}
	
	private int doubleToInt(double d) {
		if (d >= 0) {
			return (int) d;
		} else {
			return -((int) (1 - d));
		}
	}
}
