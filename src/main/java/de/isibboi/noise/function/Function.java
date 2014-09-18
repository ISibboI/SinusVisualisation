package de.isibboi.noise.function;

import de.isibboi.noise.Vector;

public abstract class Function {
	private static final double DERIVERATION_DISTANCE = 1e-5;

	public abstract double value(double x, double y);
	
	public double derivedValue(double x, double y) {
		return (value(x + DERIVERATION_DISTANCE / Math.sqrt(2), y + DERIVERATION_DISTANCE / Math.sqrt(2))
			  - value(x, y))
			  / DERIVERATION_DISTANCE;
	}
	
	public boolean inRange(double x, double y) {
		return true;
	}
	
	public Vector normal(double x, double y) {
		Vector a = new Vector(DERIVERATION_DISTANCE, 0, value(x + DERIVERATION_DISTANCE, y) - value(x, y));
		Vector b = new Vector(0, DERIVERATION_DISTANCE, value(x, y + DERIVERATION_DISTANCE) - value(x, y));
		return a.cross(b).getNormalized();
	}
}
