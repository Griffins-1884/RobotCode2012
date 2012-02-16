package Y2012.shooting;

import Y2012.shooting.ShootingApparatus.BeltDirection;
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
	protected void act() {
		((ShootingApparatus) apparatus).upperSensor.addListener(this);
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.UP);
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.UP);
	}
	public Interval duration() {
		return new Interval(1000); // TODO How long will shooting take?
	}
	public void _destroy() {
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.STOP);
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