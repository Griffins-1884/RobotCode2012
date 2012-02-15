package Y2012;

import actions.MutableActionList;
import edu.wpi.first.wpilibj.Watchdog;
import _static.Controller;
import edu.wpi.first.wpilibj.Relay;

public class AutonomousController extends Controller {
	public final MutableActionList actions;
	public AutonomousController(Robot robot) {
		super(robot);
		actions = new MutableActionList();
	}
	
	public void initialize() {
		robot.ledRing.set(Relay.Value.kForward);
	}
	
	public void periodic() {
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}