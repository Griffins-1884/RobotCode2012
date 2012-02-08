package Y2012.bridge;

import _static.Apparatus;
import sensors.LimitSwitch;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Monodent extends Apparatus {
	protected final Relay motor;
	protected final LimitSwitch lowerSwitch, upperSwitch;
	public Monodent(Relay motor, LimitSwitch lowerSwitch, LimitSwitch upperSwitch) {
		this.motor = motor;
		this.lowerSwitch = lowerSwitch;
		this.upperSwitch = upperSwitch;
	}
	protected void down() {
		if(!lowerSwitch.value()) {
			motor.set(Value.kForward);
		}
	}
	protected void up() {
		if(!upperSwitch.value()) {
			motor.set(Value.kReverse);
		}
	}
	protected void off() {
		motor.set(Value.kOff);
	}
}