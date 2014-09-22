package de.isibboi.noise;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ColorMap {
	private final Map<Point, Integer> map = new HashMap<>();
	private final long seed;
	private final MessageDigest md5;

	public ColorMap(long seed) {
		this.seed = seed;
		MessageDigest md5 = null;
		
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.md5 = md5;
	}

	public int getColor(int x, int y) {
		return getColor(new Point(x, y));
	}

	public int getColor(Point p) {
		Integer result = map.get(p);

		if (result != null) {
			return result;
		} else {
			result = getRandomNumber(seed, p.getX(), p.getY());
			map.put(p, result);
			return result;
		}
	}

	private int getRandomNumber(long... parameters) {
		try {
			md5.reset();
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);

			for (long l : parameters) {
				data.writeLong(l);
			}

			byte[] digest = md5.digest(bytes.toByteArray());

			return digest[0] + (digest[1] << 8) + (digest[2] << 16)
					+ (digest[2] << 24);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}