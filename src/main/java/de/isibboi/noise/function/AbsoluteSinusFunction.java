package de.isibboi.noise.function;

import java.math.*;

public class AbsoluteSinusFunction extends Function {
	@Override
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		return Math.abs(Math.sin(absoluteValue));
	}
	
	@Override
	public boolean inRange(double x, double y) {
		return Math.sqrt(x * x + y * y) <= Math.PI * 5;
	}
}
