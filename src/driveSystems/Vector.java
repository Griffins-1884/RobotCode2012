package driveSystems;

public class Vector {
	public final double x, y;
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}
	public Vector normalize() {
		double magnitude = magnitude();
		return new Vector(x / magnitude, y / magnitude);
	}
	public Vector rotate(double theta) {
		double sin = Math.sin(theta), cos = Math.cos(theta);
		return new Vector(x * cos  - y * sin, x * sin + y * cos);
	}
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
	public double direction() {
		return Math.atan2(y, x);
	}
}