package Y2012.shooting;

import Y2012.shooting.ShootingApparatus.BeltDirection;
import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LightSensor;
import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Collect extends Apparatus.ApparatusAction implements LightSensor.LightSensorListener {
	
	public final Interval length;
	public Collect(Interval length, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		apparatus.upperSensor.addListener(this);
		this.length = length;
	}
	protected void act() {
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.UP);
		
		//if(((ShootingApparatus) apparatus).upperSensor.value())
			((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.UP);
		
		try {
			Thread.sleep(length.milliseconds);
		} catch(InterruptedException ex) {}
		stop();
	}
	public void _destroy() {
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).upperSensor.removeListener(this);
	}
	public Interval duration() {
		return length;
	}
	public void lightSensor(BooleanSensorEvent ev) {
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
	}
}