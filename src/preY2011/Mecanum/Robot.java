package preY2011.Mecanum;

import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
                // LB: Shouldn't this be Wiring.motorCoefficients?
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, Wiring.motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}