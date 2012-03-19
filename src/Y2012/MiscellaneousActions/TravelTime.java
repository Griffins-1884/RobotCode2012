/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Y2012.MiscellaneousActions;

import Y2012.Robot;
import actions.Action;
import actions.Interval;
import driveSystems.Movement;
import spatial.Vector;

public class TravelTime extends Action {
	private long startTime, time;
	private final double driveSpeed;
	
	public TravelTime(long time, double speed) {
		super();
		this.time = time;
		this.driveSpeed = speed;
	}
	
	private boolean stop;
	
	protected void act() {
		this.startTime = System.currentTimeMillis();
		final int waitTime = 50;
		
		long currentTime = System.currentTimeMillis();
		
		while(!stop && currentTime <= startTime + time) {
			Robot.robot.driveSystem.move(new Movement(new Vector(driveSpeed,0,0), 0));
			
			try {
				Thread.sleep(waitTime);
			} catch(InterruptedException ex) {
				return; // Just exit
			}
			
			currentTime = System.currentTimeMillis();
		}
		Robot.robot.driveSystem.move(new Movement(new Vector(0,0,0), 0));
	}
	
	protected void destroy() {
		stop = true;
		Robot.robot.driveSystem.move(new Movement(new Vector(0,0,0), 0));
	}
	
	public Interval duration() {
		return new Interval(time);
	}

}
