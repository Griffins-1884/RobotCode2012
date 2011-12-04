package sensors;

/**
 * An analog sensor (provides real number values) which is easily extensible by more specific sensors.
 * 
 * @author Colin Poler
 */
public abstract class AnalogSensor extends Sensor {
	/**
	 * An interface that should be extended by each AnalogSensor type to include a method they call with their events.
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
		/**
		 * The current value of the sensor.
		 */
		public final double currentValue;
		
		/**
		 * The change from the previous value of the sensor.
		 */
		public final double deltaValue;
		private final AnalogSensor source;
		
		/**
		 * Constructs an AnalogSensorEvent from the specified source, value, and delta value.
		 * 
		 * @param source The source of the event.
		 * @param currentValue The current value of the sensor.
		 * @param deltaValue The delta value of the sensor.
		 */
		public AnalogSensorEvent(AnalogSensor source, double currentValue, double deltaValue) {
			this.source = source;
			this.currentValue = currentValue;
			this.deltaValue = deltaValue;
		}
		
		/**
		 * Returns the source of the event.
		 * 
		 * @return The source of the event.
		 */
		public AnalogSensor source() {
			return source;
		}
	}
	
	/**
	 * The previous value of the sensor.
	 */
	protected double oldValue;
	
	/**
	 * The threshold at which to notify listeners.
	 */
	protected double threshold;
	
	/**
	 * Constructs an AnalogSensor with the specified ID.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public AnalogSensor(long sensorId) {
		super(sensorId);
		oldValue = value();
		threshold = 0.01;
	}
	
	/**
	 * Sets the threshold for when an event for the sensor should be fired.
	 * 
	 * @param threshold The threshold of the sensor
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		double currentValue = value(), deltaValue = currentValue - oldValue;
		if(Math.abs(deltaValue) >= threshold) {
			fireEvent(new AnalogSensorEvent(this, currentValue, deltaValue));
		}
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
	 * Fires an AnalogSensorEvent to all of the listeners.
	 * 
	 * @param ev The AnalogSensorEvent to be fired.
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