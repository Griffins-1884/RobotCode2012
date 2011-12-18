package driveSystems;

import com.sun.squawk.util.MathUtils;

/**
 * Represents a mathematical (2-dimensional) vector, and contains methods related to vectors.
 * 
 * @author Colin Poler
 */
public class Vector {
	/**
	 * The x and y components of the vector
	 */
	public final double x, y;
	
	/**
	 * Constructs a vector from the given x and y components
	 * 
	 * @param x The x-component of the vector.
	 * @param y The y-component of the vector.
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds this vector to another vector.
	 * 
	 * @param v The vector to add.
	 * @return The vector sum
	 */
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}
	
	/**
	 * The dot product of this vector and another vector.
	 * 
	 * @param v The other vector in this operation.
	 * @return The dot product of this and v.
	 */
	public double dotProduct(Vector v) {
		return x * v.x + y * v.y;
	}
	
	/**
	 * Normalizes the vector.
	 * 
	 * @return The normalized vector.
	 */
	public Vector normalize() {
		double magnitude = magnitude();
		return new Vector(x / magnitude, y / magnitude);
	}
	
	/**
	 * Rotates the vector theta radians.
	 * 
	 * @param theta The radians to rotate the vector by.
	 * @return The rotated vectors.
	 */
	public Vector rotate(double theta) {
		double sin = Math.sin(theta), cos = Math.cos(theta);
		return new Vector(x * cos  - y * sin, x * sin + y * cos);
	}
	
	/**
	 * The magnitude of the vector.
	 * 
	 * @return The magnitude of the vector.
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * The direction of the vector.
	 * 
	 * @return The direction of the vector.
	 */
	public double direction() {
		return MathUtils.atan2(y, x);
	}
}