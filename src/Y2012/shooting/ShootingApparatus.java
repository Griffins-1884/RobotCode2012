package Y2012.shooting;

import actions.Action;
import actions.MultiAction;
import sensors.LightSensor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import _static.Apparatus;

public class ShootingApparatus extends Apparatus {
	private final Relay lowerBeltMotor, upperBeltMotor;
	private final Jaguar powerMotor, angleMotor;
	protected final LightSensor lowerSensor, upperSensor;
	private boolean lock = false;
	public ShootingApparatus(Relay lowerConveyorMotor, Relay upperConveyorMotor, Jaguar wheelsMotor, Jaguar angleAdjustmentMotor, LightSensor lowerSensor, LightSensor upperSensor) {
		this.lowerBeltMotor = lowerConveyorMotor;
		this.upperBeltMotor = upperConveyorMotor;
		this.powerMotor = wheelsMotor;
		this.angleMotor = angleAdjustmentMotor;
		this.lowerSensor = lowerSensor;
		this.upperSensor = upperSensor;
	}
	protected void setPower(double d) {
		powerMotor.set(d);
	}
	protected void setAngleMotor(double d) {
		angleMotor.set(d);
	}
	protected void setLowerBelt(boolean b) {
		if(b) lowerBeltMotor.set(Value.kOn);
		else lowerBeltMotor.set(Value.kOff);
	}
	protected void setUpperBelt(boolean b) {
		if(b) upperBeltMotor.set(Value.kOn);
		else upperBeltMotor.set(Value.kOff);
	}
	public static abstract class ShootingApparatusAction extends Action {
		public final ShootingApparatus apparatus;
		public ShootingApparatusAction(ShootingApparatus apparatus, MultiAction parent) {
			super(parent);
			this.apparatus = apparatus;
		}
		public void start() {
			while(apparatus.lock) {} // Idle until the shooter is not busy
			apparatus.lock = true;
			super.start();
			apparatus.lock = false;
		}
		public void destroy() {
			apparatus.lock = false;
			_destroy();
		}
		public abstract void _destroy();
	}
}