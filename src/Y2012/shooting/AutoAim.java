package Y2012.shooting;

import actions.Action;
import actions.Interval;

public class AutoAim extends Action {
	private Aim aimAction;
	public AutoAim() {
            super(null);
            //[getting your attention via an error] // Create an Aim action after processing where to shoot
	}
	public void act() {
		aimAction.act();
	}
	public Interval duration() {
		return aimAction.duration();
	}
}
