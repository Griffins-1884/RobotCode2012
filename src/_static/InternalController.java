package _static;

import preY2012.California.*;

/**
 * This class internally calls methods from a year's controllers.
 * 
 * @author Colin Poler
 */
public class InternalController extends edu.wpi.first.wpilibj.IterativeRobot {
	private Robot robot;
	private AutonomousController autonomousController;
	private TeleopController teleopController;
	private DisabledController disabledController;
	
	/**
	 * Initializes the InternalController.
	 */
	public void robotInit() {
		robot = Robot.robot();
		autonomousController = new AutonomousController(robot);
		teleopController = new TeleopController(robot);
		disabledController = new DisabledController(robot);
	}
	
	/**
	 * Calls the AutonomousController's initialize() method.
	 */
	public void autonomousInit() {
		autonomousController.initialize();
	}
	
	/**
	 * Calls the AutonomousController's periodic() method.
	 */
	public void autonomousPeriodic() {
		autonomousController.periodic();
	}
	
	/**
	 * Calls the AutonomousController's continuous() method.
	 */
	public void autonomousContinuous() {
		autonomousController.continuous();
	}
	
	/**
	 * Calls the TeleopController's initialize() method.
	 */
	public void teleopInit() {
		teleopController.initialize();
	}
	
	/**
	 * Calls the TeleopController's periodic() method.
	 */
	public void teleopPeriodic() {
		teleopController.periodic();
	}
	
	/**
	 * Calls the TeleopController's continuous() method.
	 */
	public void teleopContinuous() {
		teleopController.continuous();
	}
	
	/**
	 * Calls the DisabledController's initialize() method.
	 */
	public void disabledInit() {
		disabledController.initialize();
	}
	
	/**
	 * Calls the DisabledController's periodic() method.
	 */
	public void disabledPeriodic() {
		disabledController.periodic();
	}
	
	/**
	 * Calls the DisabledController's continuous() method.
	 */
	public void disabledContinuous() {
		disabledController.continuous();
	}
}