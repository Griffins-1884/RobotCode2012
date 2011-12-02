package driveSystems;

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
	 * Classifies the movement as absolute or relative.
	 */
	public final boolean isAbsolute;
	
	/**
	 * Constructs a relative movement from the given translation vector, and the given rotation vector.
	 * 
	 * @param translation The translation vector of the movement.
	 * @param rotation The amount of rotation in the movement.
	 */
	public Movement(Vector translation, double rotation) {
		this(translation, rotation, false);
	}
	
	/**
	 * Constructs a movement from the given translation vector, and the given rotation vector, and makes it absolute depending on the parameter.
	 * 
	 * @param translation The translation vector of the movement.
	 * @param rotation The amount of rotation in the movement.
	 * @param isAbsolute Whether the movement is absolute or relative.
	 */
	public Movement(Vector translation, double rotation, boolean isAbsolute) {
		this.translation = translation;
		this.rotation = rotation;
		this.isAbsolute = isAbsolute;
	}
	
	/**
	 * Returns an equivalent relative movement. It is only accurate the moment it is called, because it is modified when the robot rotates.
	 * 
	 * @param angle The angle of the robot.
	 * @return The equivalent relative movement.
	 */
	public Movement asRelative(double angle) {
		if(!isAbsolute) {
			return this;
		}
		return new Movement(translation.rotate(angle), rotation, false);
	}
	
	/**
	 * Returns an equivalent absolute movement. It is only accurate the moment it is called, because it is modified when the robot rotates.
	 * 
	 * @param angle The angle of the robot.
	 * @return The equivalent absolute movement.
	 */
	public Movement asAbsolute(double angle) {
		if(isAbsolute) {
			return this;
		}
		return new Movement(translation.rotate(-angle), rotation, true);
	}
}