package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;
import exceptions.*;

public class MecanumDrive extends DriveSystem {
	protected MecanumDrive(SpeedController[] motors, double[] motorCoefficients) {
		super(motors, motorCoefficients);
	}
	protected void checkMotors(SpeedController[] motors) {
		if(motors.length != 4) {
			throw new InvalidArrayException("Mecanum drive uses 4 motors.");
		}
		super.checkMotors(motors);
	}
	protected void updateMovement() {
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