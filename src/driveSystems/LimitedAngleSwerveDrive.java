package driveSystems;

import _static.Vector;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.SpeedController;

import exceptions.InvalidArrayException;
import exceptions.UnsupportedMovementException;
import sensors.AnalogSensor.AnalogSensorEvent;
import sensors.Encoder;
import sensors.Encoder.EncoderListener;

/**
 * A class designed to control a Swerve Drive.
 * 
 * @author Colin Poler
 */
public class LimitedAngleSwerveDrive extends DriveSystem implements EncoderListener {
	/**
	 * This constant describes how sharp the curve should be when correcting rotations of the wheels. Higher numbers mean sharper curves. To visualize, graph log((R-1)x+1)/log(R)
	 */
	protected static final double ROTATIONAL_CONSTANT = 10;
	
	/**
	 * The encoders for the two control motors.
	 */
	protected final Encoder[] encoders;
	
	/**
	 * The desired rotation for the left side.
	 */
	protected double targetLeftRotation;
	
	/**
	 * The desired rotation for the right side.
	 */
	protected double targetRightRotation;
	
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
	public LimitedAngleSwerveDrive(SpeedController[] motors, double[] motorCoefficients, Encoder[] encoders) {
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
		Vector leftVector = movement.translation.add(new Vector(-movement.rotation, 0, 0)),
			   rightVector = movement.translation.add(new Vector(movement.rotation, 0, 0));
		
		if(leftVector.magnitude() > 1 || rightVector.magnitude() > 1) {
			double maxMagnitude = Math.max(leftVector.magnitude(), rightVector.magnitude());
			leftVector = new Vector(leftVector.x / maxMagnitude, leftVector.y / maxMagnitude, 0);
			rightVector = new Vector(rightVector.x / maxMagnitude, rightVector.y / maxMagnitude, 0);
		}
		
		targetLeftRotation = leftVector.horizontalDirection();
		targetRightRotation = rightVector.horizontalDirection();
		leftPower = leftVector.magnitude();
		rightPower = rightVector.magnitude();
		while(targetLeftRotation > Math.PI / 2) {
			targetLeftRotation -= Math.PI;
			leftPower *= -1;
		}
		while(targetLeftRotation < -Math.PI / 2) {
			targetLeftRotation += Math.PI;
			leftPower *= -1;
		}
		while(targetRightRotation > Math.PI / 2) {
			targetRightRotation -= Math.PI;
			rightPower *= -1;
		}
		while(targetRightRotation < -Math.PI / 2) {
			targetRightRotation += Math.PI;
			rightPower *= -1;
		}
		currentLeftRotation = encoders[LEFT].value();
		currentRightRotation = encoders[LEFT].value();
		encoder(new AnalogSensorEvent(null, 0, 0));
	}
	
	/**
	 * Updates the motors so that the desired motion is achieved.
	 */
	public synchronized void encoder(AnalogSensorEvent ev) {
		currentLeftRotation = encoders[LEFT].value();
		currentRightRotation = encoders[RIGHT].value();
		
		// Safety for when rotation might be too much
		if(Math.abs(currentLeftRotation) > Math.PI / 2 || Math.abs(currentRightRotation) > Math.PI / 2) {
			throw new UnsupportedMovementException("Wheels turned too far.");
		}
		
		double leftError = targetLeftRotation - currentLeftRotation,
				   rightError = targetRightRotation - currentRightRotation;
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
		
		// Divide by pi sets left error between -1 and 1 (it has to be between -pi and pi in the first place). Using ln((R-1)x+1)/ln(R) creates a curve.
		// Use absolute value of errors to get a valid number from the log function.
		double leftRotationalValue = MathUtils.log1p((ROTATIONAL_CONSTANT - 1) * (Math.abs(leftError) / Math.PI)) / MathUtils.log(ROTATIONAL_CONSTANT) * motorCoefficients[LEFT + CONTROL];
		double rightRotationalValue = MathUtils.log1p((ROTATIONAL_CONSTANT - 1) * (Math.abs(rightError) / Math.PI)) / MathUtils.log(ROTATIONAL_CONSTANT) * motorCoefficients[RIGHT + CONTROL];
		
		motors[LEFT + CONTROL].set(((leftError > 0) ? 1 : -1) * leftRotationalValue);
		motors[RIGHT + CONTROL].set(((rightError > 0) ? 1 : -1) * rightRotationalValue);
	}
}