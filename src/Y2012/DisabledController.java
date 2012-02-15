package Y2012;

import _static.Controller;
import edu.wpi.first.wpilibj.Relay;

public class DisabledController extends Controller {
	public long startTime;
	public boolean ledRingValue;
	
	public DisabledController(Robot robot) {
		super(robot);	
	}
	public void initialize() {
		startTime = System.currentTimeMillis();
		ledRingValue = true;
		
		robot.ledRing.set(Relay.Value.kForward);
	}
	public void periodic() {
		// Flash LEDs!
		long currentTime = System.currentTimeMillis();
		
		if(currentTime-startTime >= 1000)
		{
			ledRingValue = !ledRingValue;
			
			if(ledRingValue)
				robot.ledRing.set(Relay.Value.kForward);
			else
				robot.ledRing.set(Relay.Value.kOff);
			
			startTime = currentTime;
		}
	}
	public void continuous() {}
}