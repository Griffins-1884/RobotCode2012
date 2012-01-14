package preY2011.Mecanum;

import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		DriveSystem driveSystem = new MecanumDrive(Wiring.motors, Wiring.motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}