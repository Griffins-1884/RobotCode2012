package Y2012.shooting;

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
		((ShootingApparatus) apparatus).setLowerBelt(true);
		((ShootingApparatus) apparatus).setUpperBelt(true);
		try {
			Thread.sleep(length.milliseconds);
		} catch(InterruptedException ex) {}
		stop();
	}
	public void _destroy() {
		((ShootingApparatus) apparatus).setLowerBelt(false);
		((ShootingApparatus) apparatus).setUpperBelt(false);
		((ShootingApparatus) apparatus).upperSensor.removeListener(this);
	}
	public Interval duration() {
		return length;
	}
	public void lightSensor(BooleanSensorEvent ev) {
		((ShootingApparatus) apparatus).setUpperBelt(false);
	}
}