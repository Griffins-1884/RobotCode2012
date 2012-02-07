package Y2012.shooting;

import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LightSensor;
import actions.Interval;
import actions.MultiAction;

public class Collect extends ShootingApparatus.ShootingApparatusAction implements LightSensor.LightSensorListener {
	public final Interval length;
	public Collect(Interval length, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		apparatus.upperSensor.addListener(this);
		this.length = length;
	}
	protected void act() {
		apparatus.setLowerBelt(true);
		apparatus.setUpperBelt(true);
		try {
			Thread.sleep(length.milliseconds);
		} catch(InterruptedException ex) {}
		stop();
	}
	public void _destroy() {
		apparatus.setLowerBelt(false);
		apparatus.setUpperBelt(false);
		apparatus.upperSensor.removeListener(this);
	}
	public Interval duration() {
		return length;
	}
	public void lightSensor(BooleanSensorEvent ev) {
		apparatus.setUpperBelt(false);
	}
}