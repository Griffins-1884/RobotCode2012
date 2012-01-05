package preY2011.California;

import edu.wpi.first.wpilibj.Watchdog;

import driveSystems.*;
import input.*;

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
		if(rightJoystick.getButton(9)) {
			oneJoystick = !oneJoystick;
		}
		if(oneJoystick) {
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.getForward(), 0), rightJoystick.getCounterClockwise()));
		} else {
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.getForward() + leftJoystick.getForward(), 0), rightJoystick.getForward() - leftJoystick.getForward()));
		}
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}