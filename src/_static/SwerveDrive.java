package _static;

import edu.wpi.first.wpilibj.SpeedController;
import exceptions.*;

public class SwerveDrive extends DriveSystem {
	// TODO figure out how to get these values
	private double currentLeftRotation, currentRightRotation;
	protected SwerveDrive(SpeedController[] motors, double[] motorCoefficients) {
		super(motors, motorCoefficients);
	}
	protected void checkMotors(SpeedController[] motors) {
		if(motors.length != 6) {
			throw new InvalidArrayException("Swerve drive uses 6 motors.");
		}
		super.checkMotors(motors);
	}
	// TODO add support for absolute movement
	protected void updateMovement() {
		Vector leftVector = movement.translation.add(new Vector(0, -movement.rotation)),
			   rightVector = movement.translation.add(new Vector(0, movement.rotation));
		if(leftVector.magnitude() > 1 || rightVector.magnitude() > 1) {
			double maxMagnitude = Math.max(leftVector.magnitude(), rightVector.magnitude());
			leftVector = new Vector(leftVector.x / maxMagnitude, leftVector.y / maxMagnitude);
			rightVector = new Vector(leftVector.x / maxMagnitude, leftVector.y / maxMagnitude);
		}
		RotationMovement leftRotation = RotationMovement.fastestRotation(currentLeftRotation, leftVector.direction()),
						 rightRotation = RotationMovement.fastestRotation(currentRightRotation, rightVector.direction());
		// TODO figure out how to rotate wheels the given amounts. It must be done in some kind of thread???
		motors[LEFT + FRONT].set(leftVector.magnitude() * leftRotation.direction);
		motors[LEFT + BACK].set(leftVector.magnitude() * leftRotation.direction);
		motors[RIGHT + FRONT].set(rightVector.magnitude() * rightRotation.direction);
		motors[RIGHT + BACK].set(rightVector.magnitude() * rightRotation.direction);
	}
	protected static class RotationMovement {
		public static RotationMovement fastestRotation(double currentRotation, double targetRotation) {
			double difference = currentRotation - targetRotation;
			if(Math.abs(difference * 2) <= -Math.PI) {
				return new RotationMovement(targetRotation - currentRotation, 1);
			} else {
				return new RotationMovement(targetRotation - currentRotation - Math.PI, -1);
			}
		}
		public final double rotation;
		public final int direction;
		public RotationMovement(double rotation, int direction) {
			while(rotation < -Math.PI) {
				rotation += 2 * Math.PI;
			}
			while(rotation > Math.PI) {
				rotation -= 2 * Math.PI;
			}
			this.rotation = rotation;
			this.direction = direction;
		}
	}
}