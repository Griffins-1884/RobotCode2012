package spatial;

import com.sun.squawk.util.MathUtils;

/**
 * Represents a mathematical (2-dimensional) vector, and contains methods related to vectors.
 * 
 * @author Colin Poler
 */
public class Vector {
	/**
	 * The x, y and z components of the vector
	 */
	public final double x, y, z;
	
	/**
	 * Constructs a vector from the given x and y components
	 * 
	 * @param x The x-component of the vector.
	 * @param y The y-component of the vector.
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Adds this vector to another vector.
	 * 
	 * @param v The vector to add.
	 * @return The vector sum
	 */
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}
	
	/**
	 * The dot product of this vector and another vector.
	 * 
	 * @param v The other vector in this operation.
	 * @return The dot product of this and v.
	 */
	public double dotProduct(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	/**
	 * Normalizes the vector.
	 * 
	 * @return The normalized vector.
	 */
	public Vector normalize() {
		double magnitude = magnitude();
		return new Vector(x / magnitude, y / magnitude, z / magnitude);
	}
	
	/**
	 * Rotates the vector theta radians.
	 * 
	 * @param theta The radians to rotate the vector by.
	 * @return The rotated vectors.
	 */
	public Vector rotateHorizontal(double theta) {
		double sin = Math.sin(theta), cos = Math.cos(theta);
		return new Vector(x * cos  - y * sin, x * sin + y * cos, z);
	}
	
	/**
	 * The magnitude of the vector.
	 * 
	 * @return The magnitude of the vector.
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Gets the horizontal vector, removing the vertical component.
	 * 
	 * @return A horizontal vector.
	 */
	public Vector horizontalProjection() {
		return new Vector(x, y, 0);
	}
	
	/**
	 * The direction of the vector.
	 * 
	 * @return The direction of the vector.
	 */
	public double horizontalDirection() {
		return MathUtils.atan2(y, x);
	}
	
	/**
	 * Represents this vector in a string.
	 * 
	 * @return A string representing this vector.
	 */
	public String toString() {
		return "X: " + x + "\nY:" + y + "\nZ:" + z;
	}
}