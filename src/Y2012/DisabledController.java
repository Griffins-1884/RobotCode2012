package Y2012;

import _static.Controller;

public class DisabledController extends Controller {
	public long previousTime;
	public boolean previousLEDRingValue;
	
	public DisabledController(Robot robot) {
		super(robot);	
	}
	public void initialize() {
		previousTime = System.currentTimeMillis();
		previousLEDRingValue = true;
		
		robot.camera.setLEDRing(previousLEDRingValue);
	}
	public void periodic() {
		// Flash LEDs!
		long currentTime = System.currentTimeMillis();
		
		if(currentTime-previousTime >= 1000)
		{
			previousLEDRingValue = !previousLEDRingValue;
			
			if(previousLEDRingValue) {
				robot.camera.setLEDRing(true);
			} else {
				robot.camera.setLEDRing(false);
			}
			
			previousTime = currentTime;
		}
	}
	public void continuous() {}
}