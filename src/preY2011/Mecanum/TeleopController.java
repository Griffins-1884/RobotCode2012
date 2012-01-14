package preY2011.Mecanum;

import edu.wpi.first.wpilibj.Watchdog;

import driveSystems.*;

import _static.*;
import input.*;

public class TeleopController extends Controller {
	private final Joystick joystick;
	public TeleopController(AbstractRobot robot) {
		super(robot);
		joystick = new AdjustableJoystick(1);
	}
	public void initialize() {}
	public void periodic() {
		double rotation = joystick.counterClockwise() / 2;
		if(joystick.button(4) && !joystick.button(5)) {
			rotation = -1;
		} else if(joystick.button(5)) {
			rotation = 1;
		}
		robot.driveSystem.move(new Movement(new Vector(joystick.forward(), joystick.right()), rotation));
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}