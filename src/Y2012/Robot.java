package Y2012;

import Y2012.bridge.Monodent;
import Y2012.shooting.ShootingApparatus;
import driveSystems.*;

import _static.AbstractRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import sensors.*;

public class Robot extends AbstractRobot {
	public static final Robot robot = new Robot();
	public final ShootingApparatus shootingApparatus;
	public final Monodent monodent;
	public final Camera camera;
	//public final Gyro gyro;
	//public final Accelerometer accelerometer;
	//public final Encoder encoder;
	public final DriverStationEnhancedIO buttonBox;
	public final DigitalInput dipSwitch1, dipSwitch2;
	public Robot() {
		// Drive system
		super(new CaliforniaDrive(Wiring.driveMotors, Wiring.drivingMotorCoefficients));
		
		// Shooter
		shootingApparatus = new ShootingApparatus(Wiring.lowerBeltMotor, Wiring.upperBeltMotor, Wiring.shooterPowerMotor, Wiring.lowerLightSensor, Wiring.upperLightSensor);
		
		// Monodent
		monodent = new Monodent(Wiring.monodentMotor, Wiring.monodentFrontSwitch, Wiring.monodentBackSwitch);
		
		// Camera
		camera = new Camera("camera".hashCode(), Wiring.cameraAddress, Wiring.tiltServo, Wiring.ledRing);
	
		// Miscellaneous
		//gyro = Wiring.gyro;
		//accelerometer = Wiring.accelerometer;
		//encoder = Wiring.encoder;
		
		buttonBox = Wiring.buttonBox;
		dipSwitch1 = Wiring.dipSwitch1;
		dipSwitch2 = Wiring.dipSwitch2;
	}
}