package _static;

import edu.wpi.first.wpilibj.SpeedController;
import exceptions.*;

public abstract class DriveSystem {
	public static int LEFT = 0,
					  RIGHT = 1,
					  FRONT = 0,
					  BACK = 2,
					  POWER = 0,
					  CONTROL = 4;
	
	protected final SpeedController[] motors;
	protected final double[] motorCoefficients;
	protected Movement movement;
	protected DriveSystem(SpeedController[] motors, double[] motorCoefficients) {
		checkMotors(motors);
		if(motors.length != motorCoefficients.length) {
			throw new InvalidArrayException("The number of motor coefficients does not match the number of motors.");
		}
		this.motors = motors;
		this.motorCoefficients = motorCoefficients;
	}
	public Movement movement() {
		return movement;
	}
	public void setMovement(Movement movement) {
		checkMovement(movement);
		this.movement = movement;
		updateMovement();
	}
	protected void checkMotors(SpeedController[] motors) {
		for(int i = 0; i < motors.length; i++) {
			if(motors[i] == null) {
				throw new InvalidArrayException("A motor has not been initiated.");
			}
		}
	}
	protected void checkMovement(Movement movement) {}
	protected abstract void updateMovement();
}