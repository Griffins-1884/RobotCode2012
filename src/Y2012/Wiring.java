package Y2012;

import sensors.LightSensor;
import sensors.LimitSwitch;
import driveSystems.DriveSystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;

public class Wiring {
	public static final Jaguar[] driveMotors = new Jaguar[4];
	public static final double[] drivingMotorCoefficients = new double[4];
	public static final Relay lowerBeltMotor, upperBeltMotor;
	public static final Jaguar shooterPowerMotor;
//	public static final LightSensor lowerLightSensor, upperLightSensor;
	public static final Relay monodentMotor;
//	public static final LimitSwitch monodentLowerSwitch, monodentUpperSwitch;
	public static final String cameraAddress;
	public static final Relay ledRing;
	public static final Servo tiltServo;
	static {
		// Drive system
		driveMotors[DriveSystem.LEFT + DriveSystem.FRONT] = new Jaguar(1);
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.FRONT] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.FRONT] = new Jaguar(2);
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.FRONT] = -1;
		driveMotors[DriveSystem.LEFT + DriveSystem.BACK] = new Jaguar(3);
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.BACK] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.BACK] = new Jaguar(4);
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.BACK] = -1;
		
		// Shooter
		shooterPowerMotor = new Jaguar(5);
		lowerBeltMotor = new Relay(3);
		upperBeltMotor = new Relay(4);
//			lowerLightSensor = new LightSensor("lower_shooter_sensor".hashCode(), 3); // TODO update for wiring
//			upperLightSensor = new LightSensor("upper_shooter_sensor".hashCode(), 4); // TODO update for wiring
		
		// Monodent
		monodentMotor = new Relay(2);
//			monodentLowerSwitch = new LimitSwitch("lower_monodent_switch".hashCode(), 2); // TODO update for wiring
//			monodentUpperSwitch = new LimitSwitch("upper_monodent_switch".hashCode(), 1); // TODO update for wiring
		
		// Miscellaneous
		cameraAddress = null;
			ledRing = new Relay(1);
			tiltServo = new Servo(7);
	}
}