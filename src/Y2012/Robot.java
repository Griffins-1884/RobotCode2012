package Y2012;

import Y2012.shooting.ShootingApparatus;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static final Robot robot = new Robot();
	public final ShootingApparatus shootingApparatus;
	public Robot() {
		super(new CaliforniaDrive(Wiring.driveMotors, Wiring.drivingMotorCoefficients));
		shootingApparatus = new ShootingApparatus(Wiring.lowerBeltMotor, Wiring.upperBeltMotor, Wiring.powerMotor, Wiring.angleMotor, Wiring.lowerLightSensor, Wiring.upperLightSensor);
	}
}