package de.isibboi.noise.function;


public class InversedInputSinusFunction extends Function {
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		
		return Math.sin(100 / absoluteValue);
	}
}
