package Y2012;

import sensors.LightSensor;
import Y2012.shooting.ShootingApparatus;
import driveSystems.DriveSystem;
import edu.wpi.first.wpilibj.Jaguar;

public class Wiring {
	public static final Jaguar[] driveMotors = new Jaguar[4];
	public static final double[] drivingMotorCoefficients = new double[4];
	public static final Jaguar[] shootingMotors = new Jaguar[4];
	public static final double[] shootingMotorCoefficients = new double[4];
	public static final LightSensor[] lightSensors = new LightSensor[2];
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
		
		shootingMotors[ShootingApparatus.CONVEYOR + ShootingApparatus.LOWER] = new Jaguar(5);
			shootingMotorCoefficients[ShootingApparatus.CONVEYOR + ShootingApparatus.LOWER] = 1; // TODO check this
		shootingMotors[ShootingApparatus.CONVEYOR + ShootingApparatus.UPPER] = new Jaguar(6);
			shootingMotorCoefficients[ShootingApparatus.CONVEYOR + ShootingApparatus.UPPER] = 1; // TODO check this
		shootingMotors[ShootingApparatus.SHOOTING + ShootingApparatus.POWER] = new Jaguar(7);
			shootingMotorCoefficients[ShootingApparatus.SHOOTING + ShootingApparatus.POWER] = 1; // TODO check this
		shootingMotors[ShootingApparatus.SHOOTING + ShootingApparatus.ANGLE] = new Jaguar(8);
			shootingMotorCoefficients[ShootingApparatus.SHOOTING + ShootingApparatus.ANGLE] = 1; // TODO check this
		
		lightSensors[ShootingApparatus.LOWER] = new LightSensor("upper_sensor".hashCode(), 1); // TODO update for wiring
		lightSensors[ShootingApparatus.UPPER] = new LightSensor("lower_sensor".hashCode(), 2); // TODO update for wiring
	}
}