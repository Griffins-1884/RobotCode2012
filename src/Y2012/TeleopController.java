package Y2012;

import edu.wpi.first.wpilibj.Watchdog;

import _static.*;

public class TeleopController extends Controller {
	public TeleopController(AbstractRobot robot) {
		super(robot);
	}
	public void initialize() {}
	public void periodic() {
		
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}