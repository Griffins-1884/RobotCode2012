package Y2012.bridge;

import _static.Apparatus;
import sensors.LimitSwitch;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Monodent extends Apparatus {
	protected final Relay motor;
	protected final LimitSwitch frontSwitch, backSwitch;
	public Monodent(Relay motor, LimitSwitch frontSwitch, LimitSwitch backSwitch) {
		this.motor = motor;
		this.frontSwitch = frontSwitch;
		this.backSwitch = backSwitch;
	}
	protected void down() {
		if(!frontSwitch.value()) {
			motor.set(Value.kForward);
		}
	}
	protected void up() {
		if(!backSwitch.value()) {
			motor.set(Value.kReverse);
		}
	}
	public void off() {
		motor.set(Value.kOff);
	}
}