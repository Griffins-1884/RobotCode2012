package sensors;

import edu.wpi.first.wpilibj.AnalogChannel;

public class LimitSwitch extends BooleanSensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface LimitSwitchListener extends BooleanSensorListener {
		public void limitSwitch(BooleanSensorEvent ev);
	}
	
	/**
	 * The hardware sensor behind this object.
	 */
	public final AnalogChannel sensor;
	
	/**
	 * Constructs a Limit Switch with the specified ID and channel.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channel The channel of the sensor.
	 */
	public LimitSwitch(long sensorId, int channel) {
		super(sensorId);
		sensor = new AnalogChannel(channel);
	}
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.LIMIT_SWITCH.
	 */
	public short type() {
		return Sensor.Types.LIMIT_SWITCH;
	}
	
	/**
	 * Gets the value of the limit switch.
	 * 
	 * @return The current value of the limit switch.
	 */
	public boolean value() {
		return sensor.getVoltage() >= 4.0;
	}
	
	/**
	 * Fires a BooleanSensorEvent to all listeners.
	 */
	protected void fireEvent(BooleanSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((LimitSwitchListener) listeners.elementAt(i)).limitSwitch(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is a LimitSwitchListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof LimitSwitchListener);
	}
}