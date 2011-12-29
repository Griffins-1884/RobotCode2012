package driveSystems;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;
import sensors.AnalogSensor.AnalogSensorEvent;
import sensors.Encoder;
import sensors.Encoder.EncoderListener;

/**
 * A class designed to control a Swerve Drive.
 * 
 * @author Colin Poler
 */
public class SwerveDrive extends DriveSystem implements EncoderListener {
	/**
	 * An angular movement designed for the motors controlling the direction of the wheels.
	 * 
	 * @author Colin Poler
	 */
	protected static class AngularMovement {
		/**
		 * Finds the shortest angular movement between the specified angles.
		 * 
		 * @param currentAngle The current angle of the wheel.
		 * @param targetAngle The target angle of the wheel.
		 * @return The shortest angular movement.
		 */
		protected static AngularMovement shortestRotation(double currentAngle, double targetAngle) {
			// TODO figure out if necessary
			while(currentAngle < -Math.PI) {
				currentAngle += 2 * Math.PI;
			}
			while(currentAngle > Math.PI) {
				currentAngle -= 2 * Math.PI;
			}

                        // atan2(y, x) produces angles on the range (-PI, PI]
                        // Readjust targetAngle so it is between 0 (right) and PI (left)

                        int wheelDirection = 1;

                        if(targetAngle < 0)
                        {
                            targetAngle += Math.PI;
                            wheelDirection *= -1;
                        }

			double deltaAngle = targetAngle - currentAngle;
			return new AngularMovement(currentAngle + deltaAngle, 
                                deltaAngle, wheelDirection);
		}
		
		/**
		 * The angle change for this move.
		 */
		protected final double deltaAngle;
		
		/**
		 * The target angle for this move.
		 */
		protected final double targetAngle;
		
		/**
		 * Whether the wheels should go backwards or forwards after turning.
		 */
		protected final int direction;
		
		/**
		 * Constructs an angular movement from the specified target angle, delta angle and direction [See description of direction field].
		 * 
		 * @param targetAngle The target angle.
		 * @param deltaAngle The angle change.
		 * @param direction The direction of the wheels.
		 */
		protected AngularMovement(double targetAngle, double deltaAngle, int direction) {
			this.targetAngle = targetAngle;
			this.deltaAngle = deltaAngle;
			this.direction = direction;
		}
	}
	
	/**
	 * The encoders for the two control motors.
	 */
	protected final Encoder[] encoders;
	
	/**
	 * The current angular movements for the left side.
	 */
	protected AngularMovement leftRotation;
	
	/**
	 * The current angular movements for the right side.
	 */
	protected AngularMovement rightRotation;
	
	/**
	 * The current rotation of the left control motor.
	 */
	protected double currentLeftRotation;
	
	/**
	 * The current rotation of the right control motor.
	 */
	protected double currentRightRotation;
	
	/**
	 * The current power for the left wheels.
	 */
	protected double leftPower;
	
	/**
	 * The current power for the right wheels.
	 */
	protected double rightPower;
	
	/**
	 * Whether the robot should drive while the wheels are turning.
	 */
	public boolean isConcurrentTurning = false;
	
	/**
	 * Constructs a swerve drive from the specified motors, motor coefficients and encoders.
	 * 
	 * @param motors The motors to be used by the swerve drive.
	 * @param motorCoefficients The motor coefficients to be used by the swerve drive.
	 * @param encoders The encoders to be used by the swerve drive.
	 */
	public SwerveDrive(SpeedController[] motors, double[] motorCoefficients, Encoder[] encoders) {
		super(motors, motorCoefficients);
		if(encoders.length != 2 || encoders[0] == null || encoders[1] == null) {
			throw new InvalidArrayException("Two encoders must be supplied for a swerve drive");
		}
		this.encoders = encoders;
		encoders[LEFT].addListener(this);
		encoders[RIGHT].addListener(this);
	}
	
	/**
	 * Checks if the array of motors is valid.
	 */
	protected void checkMotors(SpeedController[] motors) {
		if(motors.length != 6) {
			throw new InvalidArrayException("Swerve drive uses 6 motors.");
		}
		super.checkMotors(motors);
	}
	
	/**
	 * Updates the movement of the drive with the current movement.
	 */
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
	
	/**
	 * Updates the motors so that the desired motion is achieved.
	 */
	public synchronized void encoder(AnalogSensorEvent ev) {

                // N.B. Adding PI/2 since (when we start) encoders have value 0,
                // but wheels are facing forward (angle = pi/2)
		currentLeftRotation = encoders[LEFT].value() + Math.PI/2;
		currentRightRotation = encoders[RIGHT].value() + Math.PI/2;

		double leftError = leftRotation.targetAngle - currentLeftRotation,
				   rightError = rightRotation.targetAngle - currentRightRotation;
			if(isConcurrentTurning || Math.abs(leftError) <= Math.PI / 100 && Math.abs(rightError) <= Math.PI / 100) {
				motors[LEFT + FRONT].set(leftPower * motorCoefficients[LEFT + FRONT]);
				motors[LEFT + BACK].set(leftPower * motorCoefficients[LEFT + BACK]);
				motors[RIGHT + FRONT].set(rightPower * motorCoefficients[RIGHT + FRONT]);
				motors[RIGHT + BACK].set(rightPower * motorCoefficients[RIGHT + BACK]);
			} else {
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