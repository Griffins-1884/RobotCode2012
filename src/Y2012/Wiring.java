package Y2012;

import sensors.*;
import driveSystems.DriveSystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import sensors.Gyro;

public class Wiring {
	public static final Jaguar[] driveMotors = new Jaguar[4];
	public static final double[] drivingMotorCoefficients = new double[4];
	public static final Relay lowerBeltMotor, upperBeltMotor;
	public static final Jaguar shooterPowerMotor;
	public static final double shooterMotorCoefficient;
	public static final LightSensor lowerLightSensor, upperLightSensor;
	public static final Relay monodentMotor;
	public static final LimitSwitch monodentFrontSwitch, monodentBackSwitch;
	public static final String cameraAddress;
	public static final Relay ledRing;
	public static final Servo tiltServo;
	public static final Gyro gyro;
	public static final Accelerometer accelerometer;
	public static final Encoder encoder;
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
		shooterMotorCoefficient = -1;
		
		lowerBeltMotor = new Relay(3);
		upperBeltMotor = new Relay(4);
			lowerLightSensor = new LightSensor("lower_shooter_sensor".hashCode(), 3);
			upperLightSensor = new LightSensor("upper_shooter_sensor".hashCode(), 4);
		
		// Monodent
		monodentMotor = new Relay(2);
			monodentFrontSwitch = new LimitSwitch("lower_monodent_switch".hashCode(), 5);
			monodentBackSwitch = new LimitSwitch("upper_monodent_switch".hashCode(), 6);
		
		// Miscellaneous
		cameraAddress = null;
			ledRing = new Relay(1);
			tiltServo = new Servo(7);
			gyro = new Gyro("gyro".hashCode(), 2);
			accelerometer = new Accelerometer("accelerometer".hashCode(), 3, 4, -1);
			encoder = new Encoder("encoder".hashCode(), 10, 11, 250, 8); // TODO check clicks and wheel radius
	}
}