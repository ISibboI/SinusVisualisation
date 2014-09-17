package de.isibboi.noise.function;

import java.math.*;

public class InversedInputSinusFunction extends Function {
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		
		return Math.sin(100 / absoluteValue);
	}
}
