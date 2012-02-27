package Y2012.MiscellaneousActions;

import spatial.Vector;
import driveSystems.Movement;
import actions.Interval;
import Y2012.Robot;
import Y2012.shooting.ShootingApparatus;
import _static.Apparatus;
import _static.Apparatus.ApparatusAction;

public class TravelTimeAndStopShooting extends ApparatusAction {
	private long startTime, time;
	public TravelTimeAndStopShooting(Apparatus apparatus, long time) {
		super(apparatus);
		this.time = time;
		this.startTime = System.currentTimeMillis();
	}
	private boolean stop;
	protected void _destroy() {
		stop = true;
		Robot.robot.driveSystem.move(new Movement(new Vector(0,0,0), 0));
	}
	protected void act() {
		double power = ((ShootingApparatus) apparatus).previousPower;
		final int waitTime = 50;
		final double driveSpeed = 0.5; // TODO find appropriate speed
		long currentTime = System.currentTimeMillis();
		while(!stop && currentTime <= startTime + time) {
			power = 1.0 - ((currentTime - startTime) * 1.0 / time);
			((ShootingApparatus) apparatus).setPower(power);

			Robot.robot.driveSystem.move(new Movement(new Vector(driveSpeed,0,0), 0));
			
			try {
				Thread.sleep(waitTime);
			} catch(InterruptedException ex) {
				return; // Just exit
			}
		}
		stop();
	}
	public Interval duration() {
		return new Interval(time);
	}
}