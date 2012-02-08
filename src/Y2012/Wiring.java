package Y2012;

import sensors.LightSensor;
import sensors.LimitSwitch;
import driveSystems.DriveSystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;

public class Wiring {
	public static final Jaguar[] driveMotors = new Jaguar[4];
	public static final double[] drivingMotorCoefficients = new double[4];
	public static final Relay lowerBeltMotor, upperBeltMotor;
	public static final Jaguar powerMotor, angleMotor;
	public static final LightSensor lowerLightSensor, upperLightSensor;
	public static final Relay monodentMotor;
	public static final LimitSwitch monodentLowerSwitch, monodentUpperSwitch;
	static {
		driveMotors[DriveSystem.LEFT + DriveSystem.FRONT] = new Jaguar(1); // TODO update for wiring
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.FRONT] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.FRONT] = new Jaguar(2); // TODO update for wiring
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.FRONT] = -1;
		driveMotors[DriveSystem.LEFT + DriveSystem.BACK] = new Jaguar(3); // TODO update for wiring
			drivingMotorCoefficients[DriveSystem.LEFT + DriveSystem.BACK] = 1;
		driveMotors[DriveSystem.RIGHT + DriveSystem.BACK] = new Jaguar(4); // TODO update for wiring
			drivingMotorCoefficients[DriveSystem.RIGHT + DriveSystem.BACK] = -1;
		
		powerMotor = new Jaguar(5); // TODO update for wiring
		angleMotor = new Jaguar(6); // TODO update for wiring
		lowerBeltMotor = new Relay(1); // TODO update for wiring
		upperBeltMotor = new Relay(2); // TODO update for wiring
			lowerLightSensor = new LightSensor("lower_shooter_sensor".hashCode(), 1); // TODO update for wiring
			upperLightSensor = new LightSensor("upper_shooter_sensor".hashCode(), 2); // TODO update for wiring
		
		monodentMotor = new Relay(3); // TODO update for wiring
			monodentLowerSwitch = new LimitSwitch("lower_monodent_switch".hashCode(), 3);
			monodentUpperSwitch = new LimitSwitch("upper_monodent_switch".hashCode(), 4);
	}
}