package driveSystems;

import _static.Vector;

/**
 * A movement that a DriveSystem can move in.
 * 
 * @author Colin Poler
 */
public class Movement {
	/**
	 * The translation vector of the movement. If the movement is relative, this is always relative to the robot, and will rotate with the robot. If the movement is absolute, this is always relative to the field, and will not rotate with the robot.
	 */
	public final Vector translation;
	
	/**
	 * The amount that the robot should rotate.
	 */
	public final double rotation;
	
	/**
	 * Constructs a movement from the given translation vector, and the given rotation vector, and makes it absolute depending on the parameter.
	 * 
	 * @param translation The translation vector of the movement.
	 * @param rotation The amount of rotation in the movement.
	 * @param isAbsolute Whether the movement is absolute or relative.
	 */
	public Movement(Vector translation, double rotation) {
		this.translation = translation;
		this.rotation = rotation;
	}
}