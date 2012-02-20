package Y2012.MiscellaneousActions;

import Y2012.Robot;
import actions.Action;
import actions.Interval;
import driveSystems.Movement;
import spatial.Vector;

public class TurnToAngle extends Action {

	public double targetAngle;
	public static final int waitTimeInMilliseconds = 24;
	
	public TurnToAngle(double angle)
	{
		this.targetAngle = angle;
	}
	
	private boolean firstIteration = false;
	
	protected void act() {
		// Angle in radians
		if(!firstIteration)
		{
			Robot.robot.gyro.reset();
			firstIteration = true;
		}
		
		double currentAngle = 0;
		
		// Two degree tolerance
		while(Math.abs(currentAngle-targetAngle) > 0.03)
		{
			if(stop) {
				return;
			}
			//System.out.println("Current angle: " + currentAngle*180./Math.PI + " degrees");
					
			if(currentAngle < targetAngle) // turn CCW
			{
				Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0.45));
			}
			else // turn CW
			{
				break;
				//Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), -0.45));
			}
						
			try {
				Thread.sleep(waitTimeInMilliseconds);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
			
			currentAngle = Robot.robot.gyro.value();
		}
		
		stop();
	}

	private boolean stop = false;
	protected void destroy() {
		stop = true;
		Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0));
	}

	public Interval duration() {
		return new Interval(1000); // dunno
	}
	
}
