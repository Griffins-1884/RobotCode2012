package Y2012;

import sensors.LightSensor;
import Y2012.shooting.ShootingApparatus;
import driveSystems.DriveSystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;

public class Wiring {
	public static final Jaguar[] driveMotors = new Jaguar[4];
	public static final double[] drivingMotorCoefficients = new double[4];
	public static final Relay lowerBeltMotor, upperBeltMotor;
	public static final Jaguar powerMotor, angleMotor;
	public static final LightSensor lowerLightSensor, upperLightSensor;
	public static final int[] sensors = new int[0];
	static {
		driveMotors[DriveSystem.LEFT + DriveSystem.FRONT] = new Jaguar(1);
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.FRONT] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.FRONT] = new Jaguar(2);
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.FRONT] = -1;
		driveMotors[DriveSystem.LEFT + DriveSystem.BACK] = new Jaguar(3);
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.BACK] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.BACK] = new Jaguar(4);
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.BACK] = -1;
		
		powerMotor = new Jaguar(5);
		angleMotor = new Jaguar(6);
		lowerBeltMotor = new Relay(7);
		upperBeltMotor = new Relay(8);
		
		lowerLightSensor = new LightSensor("upper_sensor".hashCode(), 1); // TODO update for wiring
		upperLightSensor = new LightSensor("lower_sensor".hashCode(), 2); // TODO update for wiring
	}
}