package Y2012;

import Y2012.bridge.Monodent;
import Y2012.shooting.ShootingApparatus;
import driveSystems.*;

import _static.AbstractRobot;
import edu.wpi.first.wpilibj.Relay;
import sensors.Camera;

public class Robot extends AbstractRobot {
	public static final Robot robot = new Robot();
	public final ShootingApparatus shootingApparatus;
	public final Monodent monodent;
	public final Relay ledRing;
	public final Camera camera;
	public Robot() {		
		super(new CaliforniaDrive(Wiring.driveMotors, Wiring.drivingMotorCoefficients));
		
		ledRing = Wiring.ledRing;
		
		camera = new Camera(32847980237L, Wiring.cameraAddress, Wiring.cameraTiltServo);
		
		shootingApparatus = new ShootingApparatus(Wiring.lowerBeltMotor, Wiring.upperBeltMotor, Wiring.powerMotor, Wiring.lowerLightSensor, Wiring.upperLightSensor);
		monodent = new Monodent(Wiring.monodentMotor, Wiring.monodentLowerSwitch, Wiring.monodentUpperSwitch);
	}
}