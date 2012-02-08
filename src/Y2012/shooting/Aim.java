package Y2012.shooting;

import _static.Apparatus;
import actions.Interval;
import actions.MultiAction;

public class Aim extends Apparatus.ApparatusAction {
	public final double angle, power;
	public Aim(double angle, double power, ShootingApparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		this.angle = angle;
		this.power = power;
	}
	public void act() {
		((ShootingApparatus) apparatus).setPower(power);
		// TODO how do we set angle?
	}
	public Interval duration() {
		return new Interval(1000); // TODO time these
	}
	public void _destroy() {}
}