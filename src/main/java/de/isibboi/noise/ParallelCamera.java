package de.isibboi.noise;

import de.isibboi.noise.function.Function;

public class ParallelCamera implements Camera {
	private static final double START_STEP = 2;
	private static final double STEP_INCREMENT = 0.002;
	private static final int MAX_STEPS = 20000;
	private static final double MIN_SEARCH_INTERVAL = 0.0001;
	
	protected final Vector position;
	protected final Vector orientation;
	protected final Vector up;
	protected final Vector right;
	
	public ParallelCamera(Vector position, Vector orientation, Vector up) {
		this.position = position;
		this.orientation = orientation.getNormalized();
		this.up = up.getNormalized();
		this.right = orientation.cross(up).getNormalized();
		
		System.out.println("Camera position: " + position);
		System.out.println("Camera orientation: " + orientation);
		System.out.println("Camera up: " + up);
		System.out.println("Camera right: " + right);
		
		if (Math.abs(orientation.dot(up)) > 1e-6) {
			throw new IllegalArgumentException("'orientation' and 'up' have to be orthogonal.");
		}
	}
	
	public Vector renderPoint(double x, double y, Function f) {
		final double startX = y * up.getX() + x * right.getX() + position.getX();
		final double startY = y * up.getY() + x * right.getY() + position.getY();
		final double startZ = y * up.getZ() + x * right.getZ() + position.getZ();
		
//		System.out.println("Render start: " + new Vector(startX, startY, startZ));
		
		final double orientationX = orientation.getX();
		final double orientationY = orientation.getY();
		final double orientationZ = orientation.getZ();
		
		return searchHit(startX, startY, startZ, orientationX,
				orientationY, orientationZ, f);
	}
	
	protected Vector searchHit(final double startX, final double startY,
			final double startZ, final double orientationX,
			final double orientationY, final double orientationZ,
			final Function f) {
		final boolean startAboveFunction = isAboveFunction(startX, startY, startZ, f);
		double currentStep = START_STEP;
		
		double lastX = startX;
		double lastY = startY;
		double lastZ = startZ;
		
		double currentX = startX + orientationX * currentStep;
		double currentY = startY + orientationY * currentStep;
		double currentZ = startZ + orientationZ * currentStep;
		
		int stepsTaken = 0;
		
		// Search for interval that has function inbetween.
		do {
			lastX = currentX;
			lastY = currentY;
			lastZ = currentZ;
			
			currentStep += STEP_INCREMENT;
			stepsTaken++;
			
			currentX += orientationX * currentStep;
			currentY += orientationY * currentStep;
			currentZ += orientationZ * currentStep;
		} while (isAboveFunction(currentX, currentY, currentZ, f) == startAboveFunction
				&& stepsTaken <= MAX_STEPS);
		
		if (isAboveFunction(currentX, currentY, currentZ, f) == startAboveFunction) {
			return null;
		}
		
		// Binary search this interval for the point where the camera ray hits the function.
		while (currentStep >= MIN_SEARCH_INTERVAL) {
			currentStep /= 2;
			
			if (isAboveFunction((lastX + currentX) / 2, (lastY + currentY) / 2
					, (lastZ + currentZ) / 2, f) == startAboveFunction) {
				// Forward.
				lastX += orientationX * currentStep;
				lastY += orientationY * currentStep;
				lastZ += orientationZ * currentStep;
			} else {
				// Backward.
				currentX -= orientationX * currentStep;
				currentY -= orientationY * currentStep;
				currentZ -= orientationZ * currentStep;
			}
		}
		
		return new Vector(lastX, lastY, lastZ);
	}
	
	private boolean isAboveFunction(double x, double y, double z, Function f) {
		return z > f.value(x, y);
	}
}