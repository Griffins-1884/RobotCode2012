package sensors;

/**
 * A light sensor
 * 
 * @author Colin Poler
 */
public class LightSensor extends BooleanSensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface LightSensorListener extends BooleanSensorListener {
		public void lightSensor(BooleanSensorEvent ev);
	}
	
	/**
	 * The hardware sensor behind this object.
	 */
	public final edu.wpi.first.wpilibj.DigitalInput sensor;
	
	/**
	 * Constructs an Encoder with the specified ID and channel.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channel The channel of the sensor.
	 */
	public LightSensor(long sensorId, int channel) {
		super(sensorId);
		sensor = new edu.wpi.first.wpilibj.DigitalInput(channel);
	}
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.LIGHT_SENSOR.
	 */
	public short type() {
		return Sensor.Types.LIGHT_SENSOR;
	}
	
	/**
	 * Gets the value of the light sensor.
	 * 
	 * @return The current value of the light sensor.
	 */
	public boolean value() {
		return sensor.get();
	}
	
	/**
	 * Fires a BooleanSensorEvent to all listeners.
	 */
	protected void fireEvent(BooleanSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((LightSensorListener) listeners.elementAt(i)).lightSensor(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is a LightSensorListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof LightSensorListener);
	}
}
