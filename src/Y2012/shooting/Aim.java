package Y2012.shooting;

import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Aim extends Apparatus.ApparatusAction {
	public final double power;
	public Aim(double power, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.power = power;
	}
	public void act() {
		
		// Note: setPower() changes oldPower
		double currentPower = ((ShootingApparatus) apparatus).previousPower;
		final int millisecondsToWait = 100;
		
		while(currentPower < power)
		{
			currentPower = ((ShootingApparatus) apparatus).previousPower;
			
			if(power - currentPower < 0.05)
			{
				((ShootingApparatus) apparatus).setPower(power);
			}
			else
			{
				((ShootingApparatus) apparatus).setPower(currentPower + 0.05);
			}
			
			try {
				Thread.sleep(millisecondsToWait);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		while(power < currentPower)
		{
			currentPower = ((ShootingApparatus) apparatus).previousPower;
			
			if(currentPower - power < 0.05)
			{
				((ShootingApparatus) apparatus).setPower(power);
			}
			else
			{
				((ShootingApparatus) apparatus).setPower(currentPower - 0.05);
			}
			
			try {
				Thread.sleep(millisecondsToWait);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	public Interval duration() {
		return new Interval(1000); // TODO time these
	}
	public void _destroy() {}
}