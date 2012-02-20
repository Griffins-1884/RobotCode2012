package Y2012.bridge;

import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LimitSwitch;
import sensors.LimitSwitch.LimitSwitchListener;
import actions.Interval;
import _static.Apparatus;

public class LowerMonodent extends Apparatus.ApparatusAction implements LimitSwitchListener {
	private final LimitSwitch limitSwitch;
	public LowerMonodent(Apparatus apparatus) {
		super(apparatus);
		limitSwitch = ((Monodent) apparatus).frontSwitch;
		limitSwitch.addListener(this);
	}
	
	private boolean firstIteration = false;
	private long targetTime;
	private int waitTimeInMilliseconds = 30;
	
	protected void act() {
		
		if(!firstIteration)
		{
			firstIteration = true;
			targetTime = System.currentTimeMillis() + 1000; // act for 1 second
		}
		
		long currentTime = System.currentTimeMillis();

		if(limitSwitch.value()) {
			stop();
			return;
		}
		
		while(currentTime < targetTime)
		{
			((Monodent) apparatus).down();
			
			// Wait
			try {
					Thread.sleep(waitTimeInMilliseconds);
			} catch(InterruptedException ex) {
					ex.printStackTrace();
			}
			
			currentTime = System.currentTimeMillis();
		}
	}
	protected void _destroy() {
		limitSwitch.removeListener(this);
		((Monodent) apparatus).off();
	}
	public Interval duration() {
		return new Interval(250);
	}
	public void limitSwitch(BooleanSensorEvent ev) {
		((Monodent) apparatus).off();
		stop();
	}
}