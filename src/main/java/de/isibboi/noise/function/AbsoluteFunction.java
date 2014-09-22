package de.isibboi.noise.function;


public class AbsoluteFunction extends Function {
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		return absoluteValue;
	}
}
