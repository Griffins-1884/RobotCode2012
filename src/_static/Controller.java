package _static;

import Y2012.Robot;

/**
 * This class should be extended by another class to control each part of a competition, with the given robot.
 * 
 * It should have three subclasses in any year-specific package: AutonomousController (code only used for the autonomous portion of the competition)
 * 																 TeleopController (code only used for the teleop portion of the competition)
 * 																 DisabledController (code only used if the robot is disabled; figures out what is wrong and how to fix it)
 * 
 * @author Colin Poler
 */
public abstract class Controller {
	/**
	 * The robot that the Controller controls.
	 */
	protected final Robot robot;
	
	/**
	 * Constructs a controller to control the given robot.
	 * 
	 * @param robot The robot that the controller controls.
	 */
	public Controller(Robot robot) {
		this.robot = robot;
	}
	
	/**
	 * This method should be overridden with any initialization code for the controller.
	 */
	public abstract void initialize();
	
	/**
	 * This method should be overridden with any periodic code (called at approximately the same rate as data arrives from the driver station: ~20ms).
	 */
	public abstract void periodic();
	
	/**
	 * This method should be overridden with any continuous code (called as fast as possible).
	 */
	public abstract void continuous();
}