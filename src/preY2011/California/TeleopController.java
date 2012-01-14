package preY2011.California;

import edu.wpi.first.wpilibj.Watchdog;

import driveSystems.*;

import _static.*;
import input.*;

public class TeleopController extends Controller {
	private final Joystick leftJoystick, rightJoystick;
	private boolean oneJoystick = false;
	public TeleopController(AbstractRobot robot) {
		super(robot);
		leftJoystick = new AdjustableJoystick(1);
		rightJoystick = new AdjustableJoystick(2);
	}
	public void initialize() {}
	private boolean previousButton2State = false;
	public void periodic() {
		if((leftJoystick.button(2) || rightJoystick.button(2)) && !previousButton2State) {
			oneJoystick = !oneJoystick;
			previousButton2State = true;
		} else if(!leftJoystick.button(2) && !rightJoystick.button(2) && previousButton2State) {
			previousButton2State = false;
		}
		if(oneJoystick) {
			double rotation = rightJoystick.right() / 2;
			if(rightJoystick.button(4)) {
				rotation *= 2;
			}
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0), rotation));
		} else {
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.forward() + leftJoystick.forward()) / 2.0, 0), (rightJoystick.forward() - leftJoystick.forward()) / 2.0));
		}
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}