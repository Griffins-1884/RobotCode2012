package Y2012;

import Y2012.shooting.ShootingApparatus;
import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static final ShootingApparatus shootingApparatus;
	static {shootingApparatus = new ShootingApparatus(Wiring.lowerBeltMotor, Wiring.upperBeltMotor, Wiring.powerMotor, Wiring.angleMotor, Wiring.lowerLightSensor, Wiring.upperLightSensor);}
	public static Robot robot() {
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.driveMotors, Wiring.drivingMotorCoefficients);
		Apparatus[] apparatuses = new Apparatus[] {shootingApparatus};
		return new Robot(driveSystem, apparatuses);
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {
		super(driveSystem, apparatuses);
	}
}