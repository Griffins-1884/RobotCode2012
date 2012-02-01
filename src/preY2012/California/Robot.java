package preY2012.California;

import sensors.Camera;
import sensors.Ultrasonic;
import _static.*;
import driveSystems.*;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, Wiring.motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public final Camera camera = new Camera(32847980237L, null);
        public final Ultrasonic ultrasonic = new Ultrasonic(32847980238L, 4); // analog slot 4
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}