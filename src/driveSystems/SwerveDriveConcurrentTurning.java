package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;

public class SwerveDriveConcurrentTurning extends DriveSystem {
	// TODO figure out how to get these values
	private double currentLeftRotation, currentRightRotation;
	public SwerveDriveConcurrentTurning(SpeedController[] motors, double[] motorCoefficients) {
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
			rightVector = new Vector(rightVector.x / maxMagnitude, rightVector.y / maxMagnitude);
		}
		AngularMovement leftRotation = AngularMovement.shortestRotation(currentLeftRotation, leftVector.direction()),
						 rightRotation = AngularMovement.shortestRotation(currentRightRotation, rightVector.direction());
		// TODO figure out how to rotate wheels the given amounts. It must be done in some kind of thread???
		motors[LEFT + FRONT].set(leftVector.magnitude() * leftRotation.direction * motorCoefficients[LEFT + FRONT]);
		motors[LEFT + BACK].set(leftVector.magnitude() * leftRotation.direction * motorCoefficients[LEFT + BACK]);
		motors[RIGHT + FRONT].set(rightVector.magnitude() * rightRotation.direction * motorCoefficients[RIGHT + FRONT]);
		motors[RIGHT + BACK].set(rightVector.magnitude() * rightRotation.direction * motorCoefficients[RIGHT + BACK]);
	}
	protected static class AngularMovement {
		public static AngularMovement shortestRotation(double currentAngle, double targetAngle) {
			// TODO figure out if necessary
			while(currentAngle < -Math.PI) {
				currentAngle += 2 * Math.PI;
			}
			while(currentAngle > Math.PI) {
				currentAngle -= 2 * Math.PI;
			}
			double rotationAngle = currentAngle - targetAngle;
			if(Math.abs(rotationAngle) <= Math.PI / 2) {
				return new AngularMovement(targetAngle - currentAngle, 1);
			} else {
				return new AngularMovement(targetAngle - currentAngle - Math.PI, -1);
			}
		}
		public final double rotationAngle;
		public final int direction;
		public AngularMovement(double rotationAngle, int direction) {
			while(rotationAngle < -Math.PI) {
				rotationAngle += 2 * Math.PI;
			}
			while(rotationAngle > Math.PI) {
				rotationAngle -= 2 * Math.PI;
			}
			this.rotationAngle = rotationAngle;
			this.direction = direction;
		}
	}
}