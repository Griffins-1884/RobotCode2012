package Y2012;

import Y2012.bridge.Monodent;
import Y2012.shooting.ShootingApparatus;
import driveSystems.*;

import _static.AbstractRobot;
import sensors.Camera;

public class Robot extends AbstractRobot {
	public static final Robot robot = new Robot();
	public final ShootingApparatus shootingApparatus;
	public final Monodent monodent;
	public final Camera camera;
	public Robot() {
		// Drive system
		super(new CaliforniaDrive(Wiring.driveMotors, Wiring.drivingMotorCoefficients));
		
		// Shooter
		shootingApparatus = new ShootingApparatus(Wiring.lowerBeltMotor, Wiring.upperBeltMotor, Wiring.shooterPowerMotor, null, null);
		
		// Monodent
		monodent = new Monodent(Wiring.monodentMotor, Wiring.monodentFrontSwitch, Wiring.monodentBackSwitch);
		
		// Camera
		camera = new Camera(23958672893547L, Wiring.cameraAddress, Wiring.tiltServo, Wiring.ledRing);
	}
}