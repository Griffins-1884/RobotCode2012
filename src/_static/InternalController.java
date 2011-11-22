package _static;

import y2011.*;

public final class InternalController extends edu.wpi.first.wpilibj.IterativeRobot {
	private Robot robot;
	private AutonomousController autonomousController;
	private TeleopController teleopController;
	private DisabledController disabledController;
	public void robotInit() {
		robot = new Robot();
		autonomousController = new AutonomousController(robot);
		teleopController = new TeleopController(robot);
		disabledController = new DisabledController(robot);
	}
	public void autonomousInit() {
		autonomousController.initialize();
	}
	public void autonomousPeriodic() {
		autonomousController.periodic();
	}
	public void autonomousContinuous() {
		autonomousController.continuous();
	}
	public void teleopInit() {
		teleopController.initialize();
	}
	public void teleopPeriodic() {
		teleopController.periodic();
	}
	public void teleopContinuous() {
		teleopController.continuous();
	}
	public void disabledInit() {
		disabledController.initialize();
	}
	public void disabledPeriodic() {
		disabledController.periodic();
	}
	public void disabledContinuous() {
		disabledController.continuous();
	}
}