package Y2012.MiscellaneousActions;

import Y2012.Robot;
import _static.Vector;
import actions.Action;
import actions.Interval;
import actions.MultiAction;
import driveSystems.Movement;
import sensors.Gyro;

public class TurnToAngle extends Action {

	public double targetAngle;
	public static final int waitTimeInMilliseconds = 100;
	
	public TurnToAngle(double angle, MultiAction parent)
	{
		super(parent);
		this.targetAngle = angle;
	}
	
	protected void act() {
		// Angle in radians
		double currentAngle = Robot.robot.gyro.value();
		
		// Two degree tolerance
		while(Math.abs(currentAngle-targetAngle) > 0.035)
		{
			if(currentAngle < targetAngle) // turn CCW
			{
				Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), -0.45));
			}
			else // turn CW
			{
				Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0.45));
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
	
	protected void destroy() {
		
	}

	public Interval duration() {
		return new Interval(1000); // dunno
	}
	
}
