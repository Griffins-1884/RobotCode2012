package Y2012.bridge;

import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LimitSwitch;
import sensors.LimitSwitch.LimitSwitchListener;
import actions.Interval;
import actions.MultiAction;
import _static.Apparatus;

public class RaiseMonodent extends Apparatus.ApparatusAction implements LimitSwitchListener {
	private final LimitSwitch limitSwitch;
	public RaiseMonodent(Apparatus apparatus, MultiAction parent) {
		super(apparatus, parent);
		limitSwitch = ((Monodent) apparatus).upperSwitch;
		limitSwitch.addListener(this);
	}
	protected void act() {
		if(limitSwitch.value()) {
			stop();
			return;
		}
		((Monodent) apparatus).up();
	}
	public void _destroy() {
		limitSwitch.removeListener(this);
		((Monodent) apparatus).off();
	}
	public Interval duration() {
		return new Interval(500); // TODO check duration
	}
	public void limitSwitch(BooleanSensorEvent ev) {
		((Monodent) apparatus).off();
		stop();
	}
}