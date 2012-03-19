import Y2012.Robot;
import actions.Action;
import actions.Interval;
import driveSystems.Movement;
import spatial.Vector;

public class TurnAngleTimeWise extends Action {
	private long startTime, time;
	private final double rotationalSpeed;
	
	// CCW is positive
	// CW is negative
	public TurnAngleTimeWise(long time, double rotationalSpeed) {
		super();
		this.time = time;
		this.rotationalSpeed = rotationalSpeed;
	}
	
	private boolean stop;
	
	protected void act() {
		this.startTime = System.currentTimeMillis();
		final int waitTime = 50;
		
		long currentTime = System.currentTimeMillis();
		
		while(!stop && currentTime <= startTime + time) {
			Robot.robot.driveSystem.move(new Movement(new Vector(0,0,0), rotationalSpeed));
			
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

