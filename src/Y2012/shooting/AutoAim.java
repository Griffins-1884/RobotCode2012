package Y2012.shooting;

import _static.Location;
import actions.Action;
import actions.Interval;
import actions.MultiAction;

public class AutoAim extends Action {
	private Aim aimAction;
	public AutoAim(Location location, ShootingApparatus apparatus, MultiAction parent) {
		super(parent);
		
		double angle = 0.0, power = 0.0;
		// TODO figure out physics of where to aim
		
		aimAction = new Aim(angle, power, apparatus, parent);
	}
	public void act() {
		aimAction.start(); // TODO Listen to the aim action, then stop
	}
	public Interval duration() {
		return aimAction.duration();
	}
	public void destroy() {
		aimAction = null;
	}
}