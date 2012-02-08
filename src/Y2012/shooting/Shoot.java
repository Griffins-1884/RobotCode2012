package Y2012.shooting;

import sensors.LightSensor;
import sensors.BooleanSensor.BooleanSensorEvent;
import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Shoot extends Apparatus.ApparatusAction implements LightSensor.LightSensorListener {
	private int balls, ballsShot;
	public Shoot(int balls, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.balls = balls;
		ballsShot = 0;
	}
	public void act() {
		((ShootingApparatus) apparatus).upperSensor.addListener(this);
		((ShootingApparatus) apparatus).setUpperBelt(true);
		((ShootingApparatus) apparatus).setLowerBelt(true);
	}
	public Interval duration() {
		return new Interval(1000); // TODO How long will shooting take?
	}
	public void _destroy() {
		((ShootingApparatus) apparatus).setUpperBelt(false);
		((ShootingApparatus) apparatus).setLowerBelt(false);
		((ShootingApparatus) apparatus).upperSensor.removeListener(this);
	}
	public void lightSensor(BooleanSensorEvent ev) {
		if(ev.source.value()) {
			ballsShot++;
		}
		if(ballsShot == balls) {
			stop();
		}
	}
}