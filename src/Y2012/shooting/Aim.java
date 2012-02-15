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
		((ShootingApparatus) apparatus).setPower(power);
	}
	public Interval duration() {
		return new Interval(1000); // TODO time these
	}
	public void _destroy() {}
}