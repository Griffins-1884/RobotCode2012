package Y2012.shooting; // TODO rewrite all of my actions

import Y2012.Wiring;
import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Aim extends Apparatus.ApparatusAction {
	public final double targetPower;
	public Aim(double targetPower, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.targetPower = targetPower*Wiring.shooterMotorCoefficient;
		
	}
	public void act() {
		// Note: setPower() changes previousPower
		double currentPower = ((ShootingApparatus) apparatus).previousPower;
		final int millisecondsToWait = 100;
		
		while(currentPower != targetPower) {
			currentPower = ((ShootingApparatus) apparatus).previousPower;
			
			int sign = 1;
			
			if(targetPower < currentPower)
				sign *= -1;
			
			if(Math.abs(targetPower - currentPower) < 0.05) {
				((ShootingApparatus) apparatus).setPower(targetPower);
			} else {
				((ShootingApparatus) apparatus).setPower(currentPower + sign * 0.05);
			}

			try {
				Thread.sleep(millisecondsToWait);
			} catch(InterruptedException ex) {
				return; // Just exit, we were interrupted by stopping the action.
			}
		}
		
		stop();
	}
	public Interval duration() {
		return new Interval(1000); // TODO time these
	}
	public void _destroy() {}
}