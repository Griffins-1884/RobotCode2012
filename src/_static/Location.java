package _static;

import com.sun.squawk.util.MathUtils;

public class Location {
	/**
	 * The x and y components of the location
	 */
	public final double x, y;
	
	/**
	 * The rotation of the location.
	 */
	public final double rotation;
	
	/**
	 * Constructs a location from the given x and y components
	 * 
	 * @param x The x-component of the location.
	 * @param y The y-component of the location.
	 */
	public Location(double x, double y, double rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}
	
	/**
	 * Gets the distance to another location.
	 * 
	 * @return The distance to the specified location.
	 */
	public double distanceTo(Location l) {
		return Math.sqrt((l.x - x) * (l.x - x) + (l.y - y) * (l.y - y));
	}
	
	/**
	 * The direction of the vector.
	 * 
	 * @return The direction of the vector.
	 */
	public double directionTo(Location l) {
		return MathUtils.atan2(l.y - y, l.x - x);
	}
}