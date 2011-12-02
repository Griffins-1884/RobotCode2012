package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;

/**
 * A class for MecanumDrive (holomonic drive with four motors and special wheels).
 * 
 * @author Colin Poler
 */
public class MecanumDrive extends DriveSystem {
	/**
	 * Constructs a MecanumDrive with the specified motors and motor coefficients.
	 * 
	 * @param motors The motors the MecanumDrive uses.
	 * @param motorCoefficients The motor coefficients the MecanumDrive uses.
	 */
	protected MecanumDrive(SpeedController[] motors, double[] motorCoefficients) {
		super(motors, motorCoefficients);
	}
	
	/**
	 * Checks that there are four motors, and lets the superclass check the motors as well.
	 */
	protected void checkMotors(SpeedController[] motors) {
		if(motors.length != 4) {
			throw new InvalidArrayException("Mecanum drive uses 4 motors.");
		}
		super.checkMotors(motors);
	}
	
	/**
	 * Updates the drive with the movement it has specified.
	 */
	protected void updateMovement() {
		// TODO modify for absolute movements
		double frontLeft = movement.translation.y + movement.translation.x - movement.rotation,
				   backLeft = movement.translation.y - movement.translation.x - movement.rotation,
				   frontRight = movement.translation.y - movement.translation.x + movement.rotation,
				   backRight = movement.translation.y + movement.translation.x + movement.rotation;
			double maxPowerValue = Math.max(Math.max(Math.abs(frontLeft), Math.abs(backLeft)), Math.max(Math.abs(frontRight), Math.abs(backRight)));
			if(maxPowerValue > 1) {
				frontLeft /= maxPowerValue;
				backLeft /= maxPowerValue;
				frontRight /= maxPowerValue;
				backRight /= maxPowerValue;
			}
			motors[LEFT + FRONT].set(frontLeft * motorCoefficients[LEFT + FRONT]);
			motors[LEFT + BACK].set(backLeft * motorCoefficients[LEFT + BACK]);
			motors[RIGHT + FRONT].set(frontRight * motorCoefficients[RIGHT + FRONT]);
			motors[RIGHT + BACK].set(backRight * motorCoefficients[RIGHT + BACK]);
	}
}