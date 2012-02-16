package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.UnsupportedMovementException;

import exceptions.InvalidArrayException;
import spatial.Vector;

/**
 * A class for a california drive. It cannot move sideways or move relative to the field.
 * 
 * @author Colin Poler
 */
public class CaliforniaDrive extends DriveSystem {
	/**
	 * Constructs a california drive from the specified motors, and motor coefficients.
	 * 
	 * @param motors The motors to be used by the swerve drive.
	 * @param motorCoefficients The motor coefficients to be used by the swerve drive.
	 */
	public CaliforniaDrive(SpeedController[] motors, double[] motorCoefficients) {
		super(motors, motorCoefficients);
	}
	
	/**
	 * Checks if the array of motors is valid.
	 */
	protected void checkMotors(SpeedController[] motors) {
		if(motors.length != 4) {
			throw new InvalidArrayException("California drive uses 4 motors.");
		}
		super.checkMotors(motors);
	}
	
	/**
	 * Checks that the movement given to move() is valid. It should be overridden by a DriveSystem if it cannot move in a certain way, especially for non-holomonic drives.
	 * 
	 * @param movement The movement given to move().
	 */
	protected void checkMovement(Movement movement) {
		if(movement.translation.y != 0) {
			throw new UnsupportedMovementException("California drive cannot move sideways");
		}
	}

	/**
	 * Determines the motions this drive system is capable of.
	 * 
	 * @return The kinds of motions this drive system is capable of.
	 */
	public int capabilities() {
		return FORWARD_BACKWARD_MOTION + ROTATIONAL_MOTION;
	}
	
	public Vector oldVector = new Vector(0, 0, 0);
	
	/**
	 * Updates the motors to the most recent movement.
	 */
	protected void updateMovement() {
		double leftSpeed = movement.translation.x - movement.rotation;
		double rightSpeed = movement.translation.x + movement.rotation;
		double maxSpeed = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
		if(maxSpeed > 1) {
			leftSpeed /= maxSpeed;
			rightSpeed /= maxSpeed;
		}
		motors[LEFT + FRONT].set(leftSpeed * motorCoefficients[LEFT + FRONT]);
		motors[LEFT + BACK].set(leftSpeed * motorCoefficients[LEFT + BACK]);
		motors[RIGHT + FRONT].set(rightSpeed * motorCoefficients[RIGHT + FRONT]);
		motors[RIGHT + BACK].set(rightSpeed * motorCoefficients[RIGHT + BACK]);
		
		oldVector = movement.translation;
	}
}