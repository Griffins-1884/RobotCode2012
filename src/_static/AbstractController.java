package _static;

/**
 * This class is the abstract controller, which is given a robot and is expected to control it.
 * 
 * It should have three subclasses in any specific year-package: AutonomousController (code only used for the autonomous portion of the competition)
 * 																 TeleopController (code only used for the teleop portion of the competition)
 * 																 DisabledController (code only used if the robot is disabled; figures out what is wrong and how to fix it)
 * 
 * @author Colin Poler
 */
public abstract class AbstractController {
	protected final AbstractRobot robot;
	public AbstractController(AbstractRobot robot) {
		this.robot = robot;
	}
	public abstract void initialize();
	public abstract void periodic();
	public abstract void continuous();
}