package de.isibboi.noise.function;

public abstract class Function {
	private static final double DERIVERATION_DISTANCE = 1e-5;

	public abstract double value(double x, double y);
	
	public double derivedValue(double x, double y) {
		return (value(x + DERIVERATION_DISTANCE / Math.sqrt(2), y + DERIVERATION_DISTANCE / Math.sqrt(2))
			  - value(x, y))
			  / DERIVERATION_DISTANCE;
	}
}
