package preY2011.California;

import sensors.Camera;
import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, Wiring.motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public final Camera camera = new Camera(32847980237L, null);
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}