package de.isibboi.noise.function;

import java.math.*;

public class AbsoluteFunction extends Function {
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		return absoluteValue;
	}
}
