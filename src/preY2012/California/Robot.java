package preY2012.California;

import sensors.*;
import _static.*;
import driveSystems.*;
import edu.wpi.first.wpilibj.Relay;

import _static.AbstractRobot;

public class Robot extends AbstractRobot {
	public static Robot robot() {
		DriveSystem driveSystem = new CaliforniaDrive(Wiring.motors, Wiring.motorCoefficients);
		return new Robot(driveSystem, new Apparatus[] {});
	}
	public final Camera camera = new Camera(32847980237L, Wiring.cameraAddress);
	public final Ultrasonic ultrasonic = new Ultrasonic(32847980238L, Wiring.ultrasonicPort); // analog slot 4
	public final Gyro gyro = new Gyro(345698349L, Wiring.gyroPort); // analog slot 3
	public final Relay monodent = new Relay(8);
	public Robot(DriveSystem driveSystem, Apparatus[] apparatuses) {super(driveSystem, apparatuses);}
}