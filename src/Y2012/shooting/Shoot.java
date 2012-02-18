package Y2012.shooting;

import Y2012.shooting.ShootingApparatus.BeltDirection;
import sensors.LightSensor;
import sensors.BooleanSensor.BooleanSensorEvent;
import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Shoot extends Apparatus.ApparatusAction implements LightSensor.LightSensorListener {
	public static long DELAY = 1000;
	private DelayBelts thread = null;
	private int ballsShot = 0;
	private int balls = 0;
	public Shoot(int balls, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.balls = balls;
	}
	
	protected void act() {
		((ShootingApparatus) apparatus).upperSensor.addListener(this);
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.UP);
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.UP);
	}
	public Interval duration() {
		return new Interval(1000 * balls);
	}
	public void _destroy() {
		if(thread != null) {
			thread.interrupt();
		}
		((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.STOP);
		((ShootingApparatus) apparatus).upperSensor.removeListener(this);
	}
	public void lightSensor(BooleanSensorEvent ev) {
		if(!ev.currentValue) {
			ballsShot ++;
			((ShootingApparatus) apparatus).setUpperBelt(BeltDirection.STOP);
			((ShootingApparatus) apparatus).setLowerBelt(BeltDirection.STOP);
			thread = new DelayBelts(((ShootingApparatus) apparatus));
			thread.start();
		}
		
		if(ballsShot == balls)
		{
			stop();
		}
		
	}
	protected static class DelayBelts extends Thread {
		private final ShootingApparatus apparatus;
		public DelayBelts(ShootingApparatus apparatus) {
			this.apparatus = apparatus;
		}
		public void run() {
			try {
				Thread.sleep(DELAY);
			} catch(InterruptedException e) {
				return;
			}
						
			apparatus.setUpperBelt(BeltDirection.UP);
			apparatus.setLowerBelt(BeltDirection.UP);
		}
	}
}