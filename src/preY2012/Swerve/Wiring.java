package preY2012.Swerve;

import sensors.*;
import edu.wpi.first.wpilibj.Jaguar;

import driveSystems.DriveSystem;

public class Wiring {
	public static final Jaguar[] motors = new Jaguar[4];
	public static final double[] motorCoefficients = new double[4];
	public static final Sensor[] sensors = new Sensor[0];
	public static final Encoder[] encoders = null; // TODO I put this error here so that it wouldn't compile until the encoders are added
	static {
		motors[DriveSystem.LEFT + DriveSystem.FRONT] = new Jaguar(1);
			motorCoefficients[DriveSystem.LEFT + DriveSystem.FRONT] = 1;
		motors[DriveSystem.RIGHT + DriveSystem.FRONT] = new Jaguar(2);
			motorCoefficients[DriveSystem.RIGHT + DriveSystem.FRONT] = -1;
		motors[DriveSystem.LEFT + DriveSystem.BACK] = new Jaguar(3);
			motorCoefficients[DriveSystem.LEFT + DriveSystem.BACK] = 1;
		motors[DriveSystem.RIGHT + DriveSystem.BACK] = new Jaguar(4);
			motorCoefficients[DriveSystem.RIGHT + DriveSystem.BACK] = -1;
	}
}