package preY2012.Swerve;

import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		DriveSystem driveSystem = new LimitedAngleSwerveDrive(Wiring.motors, Wiring.motorCoefficients, Wiring.encoders);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}