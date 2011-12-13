package preY2011.Mecanum;

import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		double[] motorCoefficients = new double[4];
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}