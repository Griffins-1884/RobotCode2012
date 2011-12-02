package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;
import sensors.AnalogSensor.AnalogSensorEvent;
import sensors.Encoder;
import sensors.Encoder.EncoderListener;

public class SwerveDrive extends DriveSystem implements EncoderListener {
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
	
	protected final Encoder[] encoders;
	protected AngularMovement leftRotation, rightRotation;
	protected double currentLeftRotation, currentRightRotation;
	protected double leftPower, rightPower;
	public boolean isConcurrentTurning = false;
	public SwerveDrive(SpeedController[] motors, double[] motorCoefficients, Encoder[] encoders) {
		super(motors, motorCoefficients);
		if(encoders.length != 2 || encoders[0] == null || encoders[1] == null) {
			throw new InvalidArrayException("Two encoders must be supplied for a swerve drive");
		}
		this.encoders = encoders;
		encoders[LEFT].addListener(this);
		encoders[RIGHT].addListener(this);
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
		
		leftRotation = AngularMovement.shortestRotation(currentLeftRotation, leftVector.direction());
		rightRotation = AngularMovement.shortestRotation(currentRightRotation, rightVector.direction());
		leftPower = leftVector.magnitude() * leftRotation.direction;
		rightPower = rightVector.magnitude() * rightRotation.direction;
		currentLeftRotation = encoders[LEFT].value();
		currentRightRotation = encoders[LEFT].value();
		encoder(new AnalogSensorEvent(null, 0, 0));
	}
	public synchronized void encoder(AnalogSensorEvent ev) {
		if(ev.source().equals(encoders[LEFT])) {
			currentLeftRotation = ev.currentValue;
		} else if(ev.source().equals(encoders[RIGHT])) {
			currentRightRotation = ev.currentValue;
		}
		double leftError = leftRotation.targetAngle - currentLeftRotation,
				   rightError = rightRotation.targetAngle - currentRightRotation;
			if(Math.abs(leftError) <= Math.PI / 100 && Math.abs(rightError) <= Math.PI / 100) {
				motors[LEFT + FRONT].set(leftPower * motorCoefficients[LEFT + FRONT]);
				motors[LEFT + BACK].set(leftPower * motorCoefficients[LEFT + BACK]);
				motors[RIGHT + FRONT].set(rightPower * motorCoefficients[RIGHT + FRONT]);
				motors[RIGHT + BACK].set(rightPower * motorCoefficients[RIGHT + BACK]);
			} else {
				// TODO either finish or delete
				if(!isConcurrentTurning) {
					motors[LEFT + FRONT].set(0);
					motors[LEFT + BACK].set(0);
					motors[RIGHT + FRONT].set(0);
					motors[RIGHT + BACK].set(0);
				}
				// Dividing by pi/2 sets left error between -1 and 1 (it has to be between -pi/2 and pi/2 in the first place)
				motors[LEFT + CONTROL].set(leftError * (2 / Math.PI) * motorCoefficients[LEFT + CONTROL]);
				motors[RIGHT + CONTROL].set(rightError * (2 / Math.PI) * motorCoefficients[RIGHT + CONTROL]);
			}
	}
}