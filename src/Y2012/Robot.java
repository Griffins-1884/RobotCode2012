package Y2012;

import Y2012.shooting.ShootingApparatus;
import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static final int SHOOTER = 0;
	public static Robot robot() {
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, Wiring.motorCoefficients);
		Apparatus[] apparatuses = new Apparatus[1];
		apparatuses[SHOOTER] = new ShootingApparatus();
		return new Robot(driveSystem, apparatuses);
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {
		super(driveSystem, apparatuses);
	}
}