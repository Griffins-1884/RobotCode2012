package sensors;

/**
 * An analog sensor (values are doubles), and should be extended to provide usable sensors.
 * 
 * @author Colin Poler
 */
public abstract class AnalogSensor extends Sensor {
	/**
	 * A SensorListener for AnalogSensors.
	 * 
	 * @author Colin Poler
	 */
	public static interface AnalogSensorListener extends SensorListener {}
	
	/**
	 * A SensorEvent for AnalogSensors.
	 * 
	 * @author Colin Poler
	 */
	public static class AnalogSensorEvent extends SensorEvent {
		public final double currentValue, deltaValue;
		private final AnalogSensor source;
		public AnalogSensorEvent(AnalogSensor source, double currentValue, double deltaValue) {
			this.source = source;
			this.currentValue = currentValue;
			this.deltaValue = deltaValue;
		}
		public AnalogSensor source() {
			return source;
		}
	}
	
	/**
	 * A polling thread for an AnalogSensor.
	 * 
	 * @author Colin Poler
	 */
	protected abstract class AnalogSensorThread extends SensorThread {
		protected final AnalogSensor owner;
		protected double oldValue, threshold;
		protected AnalogSensorThread(AnalogSensor owner) {
			this.owner = owner;
			oldValue = value();
			threshold = 0.01;
		}
		protected void checkForEvents() {
			double currentValue = value(), deltaValue = currentValue - oldValue;
			if(Math.abs(deltaValue) >= threshold) {
				fireEvent(new AnalogSensorEvent(owner, currentValue, deltaValue));
			}
		}
	}
	
	/**
	 * The polling thread of the AnalogSensor.
	 */
	protected AnalogSensorThread pollThread;
	
	/**
	 * Constructs an AnalogSensor with the specified sensor ID.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public AnalogSensor(long sensorId) {
		super(sensorId);
	}
	
	/**
	 * Returns the polling thread, for use by the superclass.
	 * 
	 * @return The polling thread of the sensor.
	 */
	protected AnalogSensorThread thread() {
		return pollThread;
	}
	
	/**
	 * Sets the threshold for when an event for the sensor should be fired.
	 * 
	 * @param threshold The threshold of the sensor
	 */
	public void setThreshold(double threshold) {
		pollThread.threshold = threshold;
	}
	
	/**
	 * Determines the type of the sensor. It should just return a value from Sensor.Types, no funny business.
	 * 
	 * @return The type of the sensor.
	 */
	public abstract short type();
	
	/**
	 * Gets the value of the sensor.
	 * 
	 * @return The value of the sensor.
	 */
	public abstract double value();
	
	/**
	 * Fires an event to all of the listeners.
	 * 
	 * @param ev The event to be fired.
	 */
	protected abstract void fireEvent(AnalogSensorEvent ev);
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected abstract boolean isValidListener(SensorListener listener);
}