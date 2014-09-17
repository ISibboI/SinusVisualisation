package de.isibboi.noise;

import de.isibboi.noise.function.Function;

public interface Camera {
	Vector renderPoint(double x, double y, Function f);
}