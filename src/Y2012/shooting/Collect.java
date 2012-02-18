package Y2012.shooting;

import Y2012.shooting.ShootingApparatus.BeltDirection;
import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LightSensor;
import _static.Apparatus;
import actions.Interval;

public class Collect extends Apparatus.ApparatusAction implements LightSensor.LightSensorListener {
	public final int totalBallsToObtain; // The number of balls we end up with
	public Collect(int totalBallsToObtain, ShootingApparatus apparatus) {
		super(apparatus);
		apparatus.upperSensor.addListener(this); // TODO do we want to just use the bottom belt?
		apparatus.lowerSensor.addListener(this);
		this.totalBallsToObtain = totalBallsToObtain;
	}
	protected void act() {
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.UP);
		
		if(!((ShootingApparatus) apparatus).upperSensor.value())
			((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.UP);
	}
	public void _destroy() {
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).upperSensor.removeListener(this);
	}
	public Interval duration() {
		return new Interval(1234);
	}
	public void lightSensor(BooleanSensorEvent ev) {
		if(((ShootingApparatus) apparatus).upperSensor.value()) {
			((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
		}
		if(((ShootingApparatus) apparatus).ballCount == totalBallsToObtain) {
			stop();
		}
	}
}