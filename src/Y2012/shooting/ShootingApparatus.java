package Y2012.shooting;

import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LightSensor;
import edu.wpi.first.wpilibj.Jaguar;
import _static.Apparatus;

public class ShootingApparatus extends Apparatus implements LightSensor.LightSensorListener {
	public static final int CONVEYOR = 0, SHOOTING = 2, LOWER = 0, UPPER = 1, POWER = 0, ANGLE = 1;
	public final Jaguar[] motors;
	public final double[] motorCoefficients;
	public final LightSensor[] lightSensors;
	private int ballCount = 2; // We start out with two balls
	private double currentPower = 0.0;
	public ShootingApparatus(Jaguar[] motors, double[] motorCoefficients, LightSensor[] lightSensors) {
		this.motors = motors;
		this.motorCoefficients = motorCoefficients;
		this.lightSensors = lightSensors;
	}
	public void setPower(double power) {
		motors[SHOOTING + POWER].set(power);
		try {
			Thread.sleep((long) (2000 * Math.abs(power - currentPower))); // TODO figure out delay for wheels to spin up. Is it proportional to the change in voltage? Is it two seconds from 0 to full speed?
		} catch(InterruptedException e) {}
		currentPower = power;
	}
	public void shoot(int ballsToShoot) {
		int originalBallCount = ballCount;
		while(ballCount > 0 && ballCount > originalBallCount - ballsToShoot) {
			motors[CONVEYOR + LOWER].set(1);
			motors[CONVEYOR + UPPER].set(1);
		}
		motors[CONVEYOR + LOWER].set(0);
		motors[CONVEYOR + UPPER].set(0);
	}
	public void lightSensor(BooleanSensorEvent ev) {
		if(ev.source.equals(lightSensors[LOWER])) {
			if(ev.currentValue) { // Light is now being received, ie: a ball went past the sensor
				
			} else { // Light is not being received, ie: a ball came in front of the sensor
				ballCount++;
			}
		} else if(ev.source.equals(lightSensors[UPPER])) {
			if(ev.currentValue) { // Light is now being received, ie: a ball went past the sensor
				ballCount--;
			} else { // Light is not being received, ie: a ball got in front of the sensor
				
			}
		}
	}
}