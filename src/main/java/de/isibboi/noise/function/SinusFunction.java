package de.isibboi.noise.function;

import java.math.*;
import java.util.*;

public class SinusFunction extends Function {
	public double value(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		return Math.sin(absoluteValue);
	}
	
	public double derivedValue(double x, double y) {
		double absoluteValue = Math.sqrt(x * x + y * y);
		
		return Math.cos(absoluteValue) * (x + y) / absoluteValue / Math.sqrt(2);
	}
}
