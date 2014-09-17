package de.isibboi.noise;

import java.math.*;

public class Vector {
	private final double x;
	private final double y;
	private final double z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public Vector cross(Vector v) {
		return new Vector(y * v.z - z * v.y,
						  z * v.x - x * v.z,
						  x * v.y - y * v.x);
	}
	
	public Vector getNormalized() {
		double length = getLength();
		
		return new Vector(x / length, y / length, z / length);
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	
	public Vector multiplyScalar(double s) {
		return new Vector(x * s, y * s, z * s);
	}
}
