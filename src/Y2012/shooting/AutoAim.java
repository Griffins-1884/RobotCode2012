package Y2012.shooting;

import spatial.Location;
import spatial.Vector;
import actions.Action;
import actions.ActionListener;
import actions.Interval;
import actions.MultiAction;

public class AutoAim extends Action implements ActionListener {
	private Aim aimAction;
	
	// We have a regression line to convert from velocity to jaguar input
	public static final double regressionLineSlope = 0.1;
	public static final double regressionLineYInt = 0.01;
	
	public static final double ANGLE = 60.*Math.PI/180.; // the angle the balls actually leave at, with
	// respect to the horizontal.
	public static final double GRAV_CONSTANT = 9.8;
	
	public static final double BOX_HEIGHT = 0.5; // Height of box ABOVE FLOOR in meters
	
	public AutoAim(Vector location, ShootingApparatus apparatus, MultiAction parent) {
		super(parent);
		
		double distanceAlongFloor = location.horizontalProjection().magnitude();
		
		double muzzleVelocity = distanceAlongFloor/Math.cos(ANGLE) * 
				Math.sqrt(GRAV_CONSTANT / 
				( 2*(distanceAlongFloor*Math.tan(ANGLE) - (location.z - BOX_HEIGHT)) ) );
		
		double power = findJagInput(muzzleVelocity);
		
		if(Math.abs(power) > 1)
		{
			System.out.println("Can't make shot, need too high of a jaguar input");
			power = 1;
		}
		
		aimAction = new Aim(power, apparatus, parent);
	}
	
	/* Finds the jaguar input that will produce the required muzzle velocity.
	 * 
	 * @param velocity The muzzle velocity required in m/s
	 * 
	 * @return The jaguar input required to produce this muzzle velocity.
	 */
	public static double findJagInput(double velocity)
	{
		return regressionLineSlope*velocity + regressionLineYInt;
	}
	
	protected void act() {
		aimAction.addListener(this);
		aimAction.start();
	}
	public Interval duration() {
		return aimAction.duration();
	}
	public void destroy() {
		aimAction.stop();
	}
	public void actionCompleted(Action source) {
		stop();
	}
}