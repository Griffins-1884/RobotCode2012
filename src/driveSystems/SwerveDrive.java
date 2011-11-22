package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;
import exceptions.*;

public class SwerveDrive extends DriveSystem {
	protected MovementThread movementThread;
	protected SwerveDrive(SpeedController[] motors, double[] motorCoefficients) {
		super(motors, motorCoefficients);
		movementThread = new MovementThread();
		movementThread.run();
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
		
		movementThread.leftRotation = AngularMovement.shortestRotation(movementThread.currentLeftRotation, leftVector.direction());
		movementThread.rightRotation = AngularMovement.shortestRotation(movementThread.currentRightRotation, rightVector.direction());
		movementThread.leftPower = leftVector.magnitude() * movementThread.leftRotation.direction;
		movementThread.rightPower = rightVector.magnitude() * movementThread.rightRotation.direction;
		movementThread.newMovement = true;
	}
	protected static class AngularMovement {
		protected static AngularMovement shortestRotation(double currentAngle, double targetAngle) {
			// TODO figure out if necessary
			while(currentAngle < -Math.PI) {
				currentAngle += 2 * Math.PI;
			}
			while(currentAngle > Math.PI) {
				currentAngle -= 2 * Math.PI;
			}
			double rotationAngle = currentAngle - targetAngle;
			if(Math.abs(rotationAngle) <= Math.PI / 2) {
				double deltaAngle = targetAngle - currentAngle;
				return new AngularMovement(currentAngle + deltaAngle, deltaAngle, 1);
			} else {
				double deltaAngle = targetAngle - currentAngle - Math.PI;
				return new AngularMovement(currentAngle + deltaAngle, deltaAngle, -1);
			}
		}
		protected final double deltaAngle, targetAngle;
		protected final int direction;
		protected AngularMovement(double targetAngle, double deltaAngle, int direction) {
			while(deltaAngle < -Math.PI) {
				deltaAngle += 2 * Math.PI;
			}
			while(deltaAngle > Math.PI) {
				deltaAngle -= 2 * Math.PI;
			}
			this.targetAngle = targetAngle;
			this.deltaAngle = deltaAngle;
			this.direction = direction;
		}
	}
	protected class MovementThread extends Thread {
		protected double currentLeftRotation, currentRightRotation;
		protected double leftPower, rightPower;
		protected AngularMovement leftRotation, rightRotation;
		protected boolean newMovement = true;
		public void run() {
			while(true) {
				if(newMovement) {
					// TODO get current rotations
					double leftError = leftRotation.targetAngle - currentLeftRotation,
						   rightError = rightRotation.targetAngle - currentRightRotation;
					if(Math.abs(leftError) <= Math.PI / 100 && Math.abs(rightError) <= Math.PI / 100) {
						motors[LEFT + FRONT].set(leftPower * motorCoefficients[LEFT + FRONT]);
						motors[LEFT + BACK].set(leftPower * motorCoefficients[LEFT + BACK]);
						motors[RIGHT + FRONT].set(rightPower * motorCoefficients[RIGHT + FRONT]);
						motors[RIGHT + BACK].set(rightPower * motorCoefficients[RIGHT + BACK]);
						newMovement = false;
					} else {
						motors[LEFT + FRONT].set(0);
						motors[LEFT + BACK].set(0);
						motors[RIGHT + FRONT].set(0);
						motors[RIGHT + BACK].set(0);
						// Dividing by pi/2 sets left error between -1 and 1 (it has to be between -pi/2 and pi/2 in the first place)
						motors[LEFT + CONTROL].set(leftError * (2 / Math.PI) * motorCoefficients[LEFT + CONTROL]);
						motors[RIGHT + CONTROL].set(rightError * (2 / Math.PI) * motorCoefficients[RIGHT + CONTROL]);
					}
				}
				try {
					Thread.sleep(20);
				} catch(InterruptedException e) {}
			}
		}
	}
}