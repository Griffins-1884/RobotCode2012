package Y2012.shooting;

import sensors.LightSensor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import _static.Apparatus;

public class ShootingApparatus extends Apparatus {
	protected final Relay lowerBeltMotor, upperBeltMotor;
	public final Jaguar powerMotor;
	public final LightSensor lowerSensor, upperSensor;
	public double previousPower;
	
	/**
	 * A class (more closely resembles an enum) for belt directions
	 * 
	 */
	public static class BeltDirection {
		public final static int UP = 1,
				DOWN = 2,
				STOP = 3;
	}
	
	public ShootingApparatus(Relay lowerConveyorMotor, Relay upperConveyorMotor, Jaguar wheelsMotor, LightSensor lowerSensor, LightSensor upperSensor) {
		this.lowerBeltMotor = lowerConveyorMotor;
		this.upperBeltMotor = upperConveyorMotor;
		this.powerMotor = wheelsMotor;
		this.lowerSensor = lowerSensor;
		this.upperSensor = upperSensor;
		
		previousPower = 0;
	}
	public void setPower(double d) {
		powerMotor.set(d);
		previousPower = d;
	}
	public void setLowerBelt(int dir) {
		if(dir == BeltDirection.DOWN)
			lowerBeltMotor.set(Value.kReverse);
		else if(dir == BeltDirection.UP)
			lowerBeltMotor.set(Value.kForward);
		else if(dir == BeltDirection.STOP)
			lowerBeltMotor.set(Value.kOff);
	}
	public void setUpperBelt(int dir) {
		if(dir == BeltDirection.DOWN)
			upperBeltMotor.set(Value.kForward);
		else if(dir == BeltDirection.UP)
			upperBeltMotor.set(Value.kReverse);
		else if(dir == BeltDirection.STOP)
			upperBeltMotor.set(Value.kOff);
	}
}