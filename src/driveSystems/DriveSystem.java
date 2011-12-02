package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;

/**
 * This class defines an abstract drive system, to allow various drive systems to be controlled in the same way.
 * 
 * @author Colin Poler
 */
public abstract class DriveSystem {
	/**
	 * Defines the different motors, so that they can be easily identified.
	 */
	public final static int LEFT = 0,
					  RIGHT = 1,
					  FRONT = 0,
					  BACK = 2,
					  POWER = 0,
					  CONTROL = 4;
	
	/**
	 * The motors used by the DriveSystem.
	 */
	protected final SpeedController[] motors;
	
	/**
	 * The coefficients that the motor powers are multiplied by when their speeds are set.
	 */
	protected final double[] motorCoefficients;
	
	/**
	 * The movement in which the DriveSystem is currently moving.
	 */
	protected Movement movement;
	
	/**
	 * Constructs a Drive system with the given motors, and the given motor coefficients. The motor coefficients should be used to correct imbalances between the physical motors, and negate motors that turn opposite to what they should.
	 * 
	 * @param motors The motors that the drive system uses.
	 * @param motorCoefficients The motor coefficients that the drive system uses.
	 */
	public DriveSystem(SpeedController[] motors, double[] motorCoefficients) {
		checkMotors(motors);
		if(motors.length != motorCoefficients.length) {
			throw new InvalidArrayException("The number of motor coefficients does not match the number of motors.");
		}
		this.motors = motors;
		this.motorCoefficients = motorCoefficients;
		this.movement = new Movement(new Vector(0, 0), 0);
		updateMovement();
	}
	
	/**
	 * Returns the current movement.
	 * 
	 * @return The current movement.
	 */
	public Movement movement() {
		return movement;
	}
	
	/**
	 * Move the robot with the specified movement.
	 * 
	 * @param movement The movement the robot should move with.
	 */
	public void move(Movement movement) {
		checkMovement(movement);
		this.movement = movement;
		updateMovement();
	}
	
	/**
	 * Checks the given motors for any null values, and throws an error if one is. This should be overridden to check that the right number of motors is supplied.
	 * 
	 * @param motors The motors that were passed to the constructor.
	 */
	protected void checkMotors(SpeedController[] motors) {
		for(int i = 0; i < motors.length; i++) {
			if(motors[i] == null) {
				throw new InvalidArrayException("A motor has not been initiated.");
			}
		}
	}
	
	/**
	 * Checks that the movement given to move() is valid. It should be overridden by a DriveSystem if it cannot move in a certain way, especially for non-holomonic drives.
	 * 
	 * @param movement The movement given to move().
	 */
	protected void checkMovement(Movement movement) {}
	
	/**
	 * The method to update the drive's motors with the current movement. The movement can be assumed to be valid, because it has already been checked.
	 */
	protected abstract void updateMovement();
}