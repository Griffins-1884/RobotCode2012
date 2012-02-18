package Y2012.MiscellaneousActions;

import Y2012.Robot;
import actions.Action;
import actions.Interval;
import actions.MultiAction;
import driveSystems.Movement;
import spatial.Vector;

public class TurnToAngle extends Action {

	public double targetAngle;
	public static final int waitTimeInMilliseconds = 40;
	
	public TurnToAngle(double angle, MultiAction parent)
	{
		super(parent);
		this.targetAngle = angle;
	}
	
	protected void act() {
		// Angle in radians
		double currentAngle = Robot.robot.gyro.value();
		
		// Two degree tolerance
		while(Math.abs(currentAngle-targetAngle) > 0.03)
		{
			System.out.println("Current angle: " + currentAngle*180./Math.PI + " degrees");
					
			if(currentAngle < targetAngle) // turn CCW
			{
				Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0.3));
			}
			else // turn CW
			{
				break;
				//Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), -0.3));
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
		Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0));
	}

	public Interval duration() {
		return new Interval(1000); // dunno
	}
	
}
