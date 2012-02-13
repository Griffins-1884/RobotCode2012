package Y2012;

import driveSystems.Movement;
import input.Joystick;
import edu.wpi.first.wpilibj.Watchdog;

import _static.*;

public class TeleopController extends Controller {
	public final Joystick leftJoystick, rightJoystick;
	public final Joystick[] leftJoystickConfiguration, rightJoystickConfiguration, doubleJoysticksConfiguration;
	public Joystick[] currentJoystickConfiguration;
	public TeleopController(Robot robot) {
		super(robot);
		leftJoystick = new Joystick(1);
		rightJoystick = new Joystick(2);
		leftJoystickConfiguration = new Joystick[] {leftJoystick};
		rightJoystickConfiguration = new Joystick[] {rightJoystick};
		doubleJoysticksConfiguration = new Joystick[] {leftJoystick, rightJoystick};
		currentJoystickConfiguration = doubleJoysticksConfiguration;
	}
	public void initialize() {}
	public void periodic() {
		joystickConfiguration();
		drive();
		monodent();
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
	
	private boolean previousSingleLeftButtonState = false, previousSingleRightButtonState = false;
	public void joystickConfiguration() {
		boolean singleLeftButtonState = leftJoystick.button(6) || leftJoystick.button(7) || leftJoystick.button(10) || leftJoystick.button(11);
		boolean singleRightButtonState = rightJoystick.button(6) || rightJoystick.button(7) || rightJoystick.button(10) || rightJoystick.button(11);
		
		if(singleLeftButtonState == previousSingleLeftButtonState && singleRightButtonState == previousSingleRightButtonState) {
			return; // If the buttons haven't changed, don't do anything
		}
		
		if(singleLeftButtonState && singleRightButtonState) { // Both buttons are pressed, switch to double joysticks
			currentJoystickConfiguration = doubleJoysticksConfiguration;
		} else if(singleLeftButtonState) { // Switch to left joystick, or to both if already on left joystick
			if(currentJoystickConfiguration == leftJoystickConfiguration) {
				currentJoystickConfiguration = doubleJoysticksConfiguration;
			} else {
				currentJoystickConfiguration = leftJoystickConfiguration;
			}
		} else if(singleRightButtonState) { // Switch to right joystick, or to both if already on right joystick
			if(currentJoystickConfiguration == rightJoystickConfiguration) {
				currentJoystickConfiguration = doubleJoysticksConfiguration;
			} else {
				currentJoystickConfiguration = rightJoystickConfiguration;
			}
		}
		previousSingleLeftButtonState = singleLeftButtonState;
		previousSingleRightButtonState = singleRightButtonState;
	}
	
	public void drive() {
		if(currentJoystickConfiguration.length == 1) { // Single joystick
			double rotation = (currentJoystickConfiguration[0].right() + currentJoystickConfiguration[0].clockwise()) / 2;
			robot.driveSystem.move(new Movement(new Vector(currentJoystickConfiguration[0].forward(), 0, 0), rotation));
		} else if(currentJoystickConfiguration.length == 2) { // Double joystick
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((currentJoystickConfiguration[1].forward() + currentJoystickConfiguration[0].forward()) / 2.0, 0, 0), (currentJoystickConfiguration[1].forward() - currentJoystickConfiguration[0].forward()) / 2.0));
		}
	}

	private boolean previousMonodentButtonState = false;
	public void monodent() {
		
	}
}