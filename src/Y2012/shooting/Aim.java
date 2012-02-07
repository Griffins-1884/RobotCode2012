package Y2012.shooting;

import actions.Interval;
import actions.MultiAction;

public class Aim extends ShootingApparatus.ShootingApparatusAction {
	public final double angle, power;
	public Aim(double angle, double power, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.angle = angle;
		this.power = power;
	}
	public void act() {
		apparatus.setPower(power);
		// TODO how do we set angle?
	}
	public Interval duration() {
		return new Interval(1000); // TODO time these
	}
	public void _destroy() {}
}