package sensors;

/**
 * A boolean sensor (only provides boolean values) which is easily extensible by more specific sensors.
 * 
 * @author Colin Poler
 */
public abstract class BooleanSensor extends Sensor {
	/**
	 * An interface that should be extended by each BooleanSensor type to include a method they call with their events.
	 * 
	 * @author Colin Poler
	 */
	public static interface BooleanSensorListener extends SensorListener {}
	
	/**
	 * A BooleanSensor event, which is sent to listeners.
	 * 
	 * @author Colin Poler
	 */
	public static class BooleanSensorEvent extends SensorEvent {
		/**
		 * The current value of the sensor.
		 */
		public final boolean currentValue;
		
		/**
		 * The sensor firing this event.
		 */
		public final BooleanSensor source;
		
		/**
		 * Constructs a BooleanSensorEvent from the specified source and value..
		 * 
		 * @param source The source of the event.
		 * @param currentValue The current value of the sensor.
		 */
		public BooleanSensorEvent(BooleanSensor source, boolean currentValue) {
			this.source = source;
			this.currentValue = currentValue;
		}
	}
	
	/**
	 * The previous value of the sensor.
	 */
	protected boolean oldValue;
	
	/**
	 * Constructs an BooleanSensor with the specified ID.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public BooleanSensor(long sensorId) {
		super(sensorId);
		oldValue = value();
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		boolean currentValue = value();
		if(currentValue != oldValue) {
			oldValue = currentValue;
			fireEvent(new BooleanSensorEvent(this, currentValue));
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
	public abstract boolean value();
	
	/**
	 * Fires an BooleanSensorEvent to all listeners.
	 * 
	 * @param The BooleanSensorEvent to be fired.
	 */
	protected abstract void fireEvent(BooleanSensorEvent ev);
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected abstract boolean isValidListener(SensorListener listener);
}