package Y2012.shooting;

import actions.Action;
import actions.Interval;
import actions.MultiAction;

public class Shoot extends Action {
	private int balls;
	public Shoot(int balls, MultiAction parent) {
		super(parent);
		this.balls = balls;
	}
	public void act() {
		// TODO [getting your attention via an error] // Shoot the basketball
	}
	public Interval duration() {
		return null;
		// TODO [getting your attention via an error] // How long will shooting take?
	}
}