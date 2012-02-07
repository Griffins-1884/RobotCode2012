package Y2012.shooting;

import actions.Interval;
import actions.MultiAction;

public class Shoot extends ShootingApparatus.ShootingApparatusAction {
	private int balls;
	public Shoot(int balls, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.balls = balls;
	}
	public void act() {
		apparatus.setUpperBelt(true);
		apparatus.setLowerBelt(true);
	}
	public Interval duration() {
		return new Interval(1000); // TODO How long will shooting take?
	}
	public void _destroy() {
		apparatus.setUpperBelt(false);
		apparatus.setLowerBelt(false);
	}
}