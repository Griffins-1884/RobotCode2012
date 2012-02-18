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
	
	public int waitTimeInMilliseconds = 40;
	
	public TravelDistance(Encoder enc, double distanceInMeters, MultiAction parent)
	{
		super(parent);
		
		this.enc = enc;
		this.distanceToTravel = distanceInMeters;
	}
	
	protected void act() {
				
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
			Robot.robot.driveSystem.move(new Movement(new Vector(-0.4, 0, 0), 0));
			
			System.out.println("Current distance: " + currentDistance);
			System.out.println("Target distance: " + targetDistance);
			
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
		Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0));
	}
	
	public Interval duration() {
		
		return new Interval(1000); // dunno
	}
	
}