package preY2011.Mecanum;

import _static.AbstractRobot;
import _static.Controller;

public class DisabledController extends Controller {
	public DisabledController(AbstractRobot robot) {
		super(robot);
	}
	public void initialize() {}
	public void periodic() {}
	public void continuous() {}
}