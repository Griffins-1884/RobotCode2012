/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Y2012.shooting;

import _static.Apparatus.ApparatusAction;
import actions.Interval;
import actions.MultiAction;

/**
 *
 * @author griffins
 */
public class StopShooting extends ApparatusAction {
		
	public StopShooting(ShootingApparatus apparatus, MultiAction parent)
	{
		super(apparatus, parent);
	}
	
	protected void act() {
		// Note: setPower() changes previousPower
		double currentPower = ((ShootingApparatus) apparatus).previousPower;
		final int millisecondsToWait = 100;
		
		while(currentPower != 0) {
			currentPower = ((ShootingApparatus) apparatus).previousPower;
			
			int sign = 1;
			
			if(currentPower > 0)
				sign *= -1;
			
			if(Math.abs(0 - currentPower) < 0.05) {
				((ShootingApparatus) apparatus).setPower(0);
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
		return new Interval(3000); // Dunno
	}
	
	public void _destroy() {}
}
