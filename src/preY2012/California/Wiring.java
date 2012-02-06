package preY2012.California;

import sensors.Sensor;
import sensors.LightSensor;
import edu.wpi.first.wpilibj.Jaguar;

import driveSystems.DriveSystem;

public class Wiring {
	public static final Jaguar[] motors = new Jaguar[4];
	public static final Jaguar[] controlMotors = new Jaguar[2];
	public static final double[] motorCoefficients = new double[4];
	public static final double[] controlMotorCoefficients = new double[2];
	public static final Sensor[] sensors = new Sensor[0];
	public static final String cameraAddress = null;
	public static final int ultrasonicPort = 4;
	public static final int gyroPort = 1;
	
	//public static LightSensor boxSensor = new LightSensor(42L, 1);
	
	static {
		motors[DriveSystem.LEFT + DriveSystem.FRONT] = new Jaguar(1);
			motorCoefficients[DriveSystem.LEFT + DriveSystem.FRONT] = 1;
		motors[DriveSystem.RIGHT + DriveSystem.FRONT] = new Jaguar(2);
			motorCoefficients[DriveSystem.RIGHT + DriveSystem.FRONT] = -1;
		motors[DriveSystem.LEFT + DriveSystem.BACK] = new Jaguar(3);
			motorCoefficients[DriveSystem.LEFT + DriveSystem.BACK] = 1;
		motors[DriveSystem.RIGHT + DriveSystem.BACK] = new Jaguar(4);
			motorCoefficients[DriveSystem.RIGHT + DriveSystem.BACK] = -1;
		controlMotors[0] = new Jaguar(5); // intake
			controlMotorCoefficients[0] = 1;
		controlMotors[1] = new Jaguar(6); // shooter
			controlMotorCoefficients[1] = 1;
	}
}