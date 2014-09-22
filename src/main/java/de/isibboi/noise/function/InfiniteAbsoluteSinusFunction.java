package de.isibboi.noise.function;


public class InfiniteAbsoluteSinusFunction extends Function {
	@Override
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		return Math.abs(Math.sin(absoluteValue));
	}
}
