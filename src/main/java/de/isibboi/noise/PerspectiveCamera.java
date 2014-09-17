package de.isibboi.noise;

import de.isibboi.noise.function.Function;

public class PerspectiveCamera extends ParallelCamera {
	private final Vector pov;
	
	public PerspectiveCamera(Vector position, Vector orientation,
			Vector up, Vector pov) {
		super(position, orientation, up);
		
		this.pov = pov;
	}
	
	public Vector renderPoint(double x, double y, Function f) {
		final double startX = y * up.getX() + x * right.getX() + position.getX();
		final double startY = y * up.getY() + x * right.getY() + position.getY();
		final double startZ = y * up.getZ() + x * right.getZ() + position.getZ();
		
//		System.out.println("Render start: " + new Vector(startX, startY, startZ));
		
		double orientationX = startX - pov.getX();
		double orientationY = startY - pov.getY();
		double orientationZ = startZ - pov.getZ();
		double orientationLength = Math.sqrt(orientationX * orientationX
				+ orientationY * orientationY + orientationZ * orientationZ);
		orientationX /= orientationLength;
		orientationY /= orientationLength;
		orientationZ /= orientationLength;
		
		return searchHit(startX, startY, startZ, orientationX,
				orientationY, orientationZ, f);
	}
}