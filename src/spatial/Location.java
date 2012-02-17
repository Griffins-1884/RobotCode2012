package spatial;

import com.sun.squawk.util.MathUtils;

public class Location {
	/**
	 * The x component of the location (lengthwise on field)
	 */
	public final double x;
	
	/**
	 * The y component of the location (widthwise on field)
	 */
	public final double y;
	
	/**
	 * The z component of the location (height above field)
	 */
	public final double z;
	
	
	/**
	 * Constructs a location from the given x and y components
	 * 
	 * @param x The x-component of the location.
	 * @param y The y-component of the location.
	 */
	public Location(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets a vector to another location.
	 * 
	 * @return A vector to the specified location.
	 */
	public Vector vectorTo(Location l) {
		return new Vector(l.x - x, l.y - y, l.z - z);
	}
	
	/**
	 * Gets the distance to another location.
	 * 
	 * @return The distance to the specified location.
	 */
	public double distanceTo(Location l) {
		return vectorTo(l).magnitude();
	}
	
	/**
	 * The direction of the vector.
	 * 
	 * @return The direction of the vector.
	 */
	public double directionTo(Location l) {
		return vectorTo(l).horizontalDirection();
	}
	
	/**
	 * Represents this location in a string.
	 * 
	 * @return A string representing this location.
	 */
	public String toString() {
		return "X: " + x + "\nY:" + y + "\nZ:" + z;
	}
}