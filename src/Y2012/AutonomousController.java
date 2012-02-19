package Y2012;

import actions.MutableActionList;
import edu.wpi.first.wpilibj.Watchdog;
import Y2012.MiscellaneousActions.TravelDistance;
import Y2012.MiscellaneousActions.TurnToAngle;
import Y2012.bridge.LowerMonodent;
import Y2012.shooting.Aim;
import Y2012.shooting.LineUpAndAim;
import Y2012.shooting.Shoot;
import Y2012.shooting.StopShooting;
import _static.Controller;
import actions.Interval;
import actions.WaitAction;
import spatial.Goto;
import spatial.Location;
import spatial.LocationTracker;

public class AutonomousController extends Controller {
	public static MutableActionList actions;
	public AutonomousController(Robot robot) {
		super(robot);
	}
	
	public void initialize() {
		
		Robot.robot.gyro.reset();
		
		actions = new MutableActionList();
		
		// around 7 cm of momentum!
		
		actions.add(new Aim(1.0, Robot.robot.shootingApparatus));
		actions.add(new Shoot(2, Robot.robot.shootingApparatus));
		actions.add(new StopShooting(Robot.robot.shootingApparatus));
		actions.add(new TravelDistance(Robot.robot.encoder, -1.5));
		actions.add(new TurnToAngle(Robot.robot.gyro.value() + 165.*Math.PI/180.)); // off by 15 degrees
		actions.add(new TravelDistance(Robot.robot.encoder, 1.5/*2.1336*/));
		actions.add(new WaitAction(new Interval(500)));
		actions.add(new LowerMonodent(Robot.robot.monodent));
		
		robot.camera.setLEDRing(true);
		robot.camera.tilt(77);
		actions.start();
	}
	
	public void periodic() {
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}