package Y2012.MiscellaneousActions;

import Y2012.Robot;
import actions.Action;
import actions.Interval;
import actions.MultiAction;
import driveSystems.Movement;
import sensors.Encoder;
import spatial.Vector;


public class TravelDistance extends Action {

	public Encoder enc;
	public double distanceToTravel; // in meters
	public boolean firstIteration = true;
	public double startDistance;
	public double targetDistance;
	
	public int waitTimeInMilliseconds = 100;
	
	public TravelDistance(Encoder enc, double distanceInMeters, MultiAction parent)
	{
		super(parent);
		
		this.enc = enc;
		this.distanceToTravel = distanceInMeters;
	}
	
	protected void act() {
		
		System.out.println("TravelDistance " + System.currentTimeMillis());
		
		if(firstIteration)
		{
			startDistance = enc.distance();
			targetDistance = startDistance + distanceToTravel;
			
			firstIteration = false;
		}
		
		double currentDistance = enc.distance(); 
		
		while(currentDistance < targetDistance)
		{
			// Move forwards
			Robot.robot.driveSystem.move(new Movement(new Vector(0.8, 0, 0), 0));
			
			// Wait
			try {
				Thread.sleep(waitTimeInMilliseconds);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
			
			currentDistance = enc.distance();
		}
		
		stop();
	}
	
	protected void destroy() {
	}
	
	public Interval duration() {
		
		return new Interval(1000); // dunno
	}
	
}