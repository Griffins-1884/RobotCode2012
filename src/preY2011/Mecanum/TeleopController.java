package preY2011.Mecanum;

import edu.wpi.first.wpilibj.Joystick;

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
		if(oneJoystick) {
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.getY(), 0), rightJoystick.getTwist()));
		} else {
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.getY() + leftJoystick.getY())/2.0, 0), rightJoystick.getY() - leftJoystick.getY()));
		}
	}
	public void continuous() {}
}