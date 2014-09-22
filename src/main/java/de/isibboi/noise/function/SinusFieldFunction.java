package de.isibboi.noise.function;


public class SinusFieldFunction extends Function {
	public double value(double x, double y) {
		return (Math.sin(Math.sqrt(2) * x) + Math.sin(Math.sqrt(2) * y)) / 2;
	}
}
