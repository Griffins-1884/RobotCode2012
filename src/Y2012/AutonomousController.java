package Y2012;

import actions.MutableActionList;
import edu.wpi.first.wpilibj.Watchdog;
import Y2012.MiscellaneousActions.TravelDistance;
import Y2012.MiscellaneousActions.TurnToAngle;
import Y2012.shooting.LineUpAndShoot;
import Y2012.shooting.Shoot;
import _static.Controller;

public class AutonomousController extends Controller {
	public final MutableActionList actions;
	public AutonomousController(Robot robot) {
		super(robot);
		actions = new MutableActionList();
		actions.add(new LineUpAndShoot(LineUpAndShoot.RectangleChoice.TOP, actions));
		actions.add(new Shoot(2, Robot.robot.shootingApparatus, actions));
		actions.add(new TurnToAngle(Robot.robot.gyro.value() + Math.PI, actions));
		actions.add(new TravelDistance(Robot.robot.encoder, 2.5, actions));
	}
	
	public void initialize() {
		robot.camera.setLEDRing(true);
		actions.start();
	}
	
	public void periodic() {
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}