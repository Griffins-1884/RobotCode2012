package sensors;

/**
 * An gyro sensor.
 * 
 * @author Colin Poler
 */
public class Gyro extends AnalogSensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface GyroListener extends AnalogSensorListener {
		public void gyro(AnalogSensorEvent ev);
	}
	
	/**
	 * The hardware sensor behind this object.
	 */
	public final edu.wpi.first.wpilibj.Gyro sensor;
	
	/**
	 * Constructs an Gyro with the specified ID, and channel.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channel The channel of the sensor.
	 */
	public Gyro(long sensorId, int channel) {
		super(sensorId);
		sensor = new edu.wpi.first.wpilibj.Gyro(channel);
		setThreshold(Math.PI / 100);
	}
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.GYRO.
	 */
	public short type() {
		return Types.GYRO;
	}

	/**
	 * Gets the value of the gyro in radians
	 * 
	 * @return The current radians value of the gyro.
	 */
	public double value() {
		// TODO check this
		return sensor.getAngle() * Math.PI / 180;
	}
	
	/**
	 * Fires an AnalogSensorEvent to all listeners.
	 */
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((GyroListener) listeners.get(i)).gyro(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is a GyroListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof GyroListener);
	}
}