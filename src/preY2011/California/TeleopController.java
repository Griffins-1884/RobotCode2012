package preY2011.California;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Watchdog;

import driveSystems.*;

import _static.*;

public class TeleopController extends Controller {
	private final Joystick leftJoystick, rightJoystick;
	private boolean oneJoystick = false;
	public TeleopController(AbstractRobot robot) {
		super(robot);
		leftJoystick = new Joystick(1);
		rightJoystick = new Joystick(2);
	}
	public void initialize() {}
	public void periodic() {
		if(rightJoystick.getRawButton(9)) {
			oneJoystick = !oneJoystick;
		}
		if(oneJoystick) {
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.getY(), 0), rightJoystick.getTwist()));
		} else {
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.getY() + leftJoystick.getY())/2.0, 0), rightJoystick.getY() - leftJoystick.getY()));
		}
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}