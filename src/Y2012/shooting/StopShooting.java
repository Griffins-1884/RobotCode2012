/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Y2012.shooting;

import Y2012.TeleopController;
import _static.Apparatus.ApparatusAction;
import actions.Interval;

/**
 *
 * @author griffins
 */
public class StopShooting extends ApparatusAction {
		
	public StopShooting(ShootingApparatus apparatus)
	{
		super(apparatus);
	}
	
	protected void act() {
		// Note: setPower() changes previousPower
		double currentPower = ((ShootingApparatus) apparatus).previousPower;
		final int millisecondsToWait = 35;
		
		while(currentPower != 0) {
			currentPower = ((ShootingApparatus) apparatus).previousPower;
			
			int sign = 1;
			
			if(currentPower > 0)
				sign *= -1;
			
			if(Math.abs(0 - currentPower) < TeleopController.RAMP_DECREMENT) {
				((ShootingApparatus) apparatus).setPower(0);
			} else {
				((ShootingApparatus) apparatus).setPower(currentPower + sign * TeleopController.RAMP_DECREMENT);
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
