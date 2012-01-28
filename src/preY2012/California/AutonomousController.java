package preY2012.California;

import edu.wpi.first.wpilibj.Watchdog;
import _static.AbstractRobot;
import _static.Controller;

public class AutonomousController extends Controller {
	public AutonomousController(AbstractRobot robot) {
		super(robot);
	}
	public void initialize() {}
	public void periodic() {
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}