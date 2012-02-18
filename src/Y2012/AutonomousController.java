package Y2012;

import actions.MutableActionList;
import edu.wpi.first.wpilibj.Watchdog;
import Y2012.MiscellaneousActions.TravelDistance;
import Y2012.MiscellaneousActions.TurnToAngle;
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
		LocationTracker l = new LocationTracker(Wiring.accelerometer, Wiring.gyro);
		actions.add(new Goto(new Location(-5, 5, 0), Math.PI / 6, l, robot.driveSystem, actions));
		//actions.add(new WaitAction(new Interval(500), actions));
		//actions.add(new Aim(1.0, Robot.robot.shootingApparatus, actions));
		//actions.add(new Shoot(2, Robot.robot.shootingApparatus, actions));
		//actions.add(new StopShooting(Robot.robot.shootingApparatus, actions));
		//actions.add(new TurnToAngle(Robot.robot.gyro.value() + 178.*Math.PI/180., actions));
		//actions.add(new TravelDistance(Robot.robot.encoder, 2.5, actions));
		
		robot.camera.setLEDRing(true);
		robot.camera.tilt(77);
		actions.start();
	}
	
	public void periodic() {
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}