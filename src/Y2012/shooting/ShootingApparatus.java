package Y2012.shooting;

import sensors.LightSensor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import _static.Apparatus;

public class ShootingApparatus extends Apparatus {
	protected final Relay lowerBeltMotor, upperBeltMotor;
	protected final Jaguar powerMotor;
	protected final LightSensor lowerSensor, upperSensor;
	protected double previousPower;
	public ShootingApparatus(Relay lowerConveyorMotor, Relay upperConveyorMotor, Jaguar wheelsMotor, LightSensor lowerSensor, LightSensor upperSensor) {
		this.lowerBeltMotor = lowerConveyorMotor;
		this.upperBeltMotor = upperConveyorMotor;
		this.powerMotor = wheelsMotor;
		this.lowerSensor = lowerSensor;
		this.upperSensor = upperSensor;
		
		previousPower = 0;
	}
	protected void setPower(double d) {
		powerMotor.set(d);
		previousPower = d;
	}
	protected void setLowerBelt(boolean b) {
		if(b) lowerBeltMotor.set(Value.kOn);
		else lowerBeltMotor.set(Value.kOff);
	}
	protected void setUpperBelt(boolean b) {
		if(b) upperBeltMotor.set(Value.kOn);
		else upperBeltMotor.set(Value.kOff);
	}
}