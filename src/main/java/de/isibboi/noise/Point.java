package de.isibboi.noise;

public class Point {
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return x + (y << 16);
	}
}