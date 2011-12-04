package sensors;

/**
 * An integral sensor (only provides integral values) which is easily extensible by more specific sensors.
 * 
 * @author Colin Poler
 */
public abstract class IntegralSensor extends Sensor {
	/**
	 * An interface that should be extended by each IntegralSensor type to include a method they call with their events.
	 * 
	 * @author Colin Poler
	 */
	public static interface IntegralSensorListener extends SensorListener {}
	
	/**
	 * An IntegralSensor event, which is sent to listeners.
	 * 
	 * @author Colin Poler
	 */
	public static class IntegralSensorEvent extends SensorEvent {
		/**
		 * The current value of the sensor.
		 */
		public final int currentValue;
		
		/**
		 * The change from the previous value of the sensor.
		 */
		public final int deltaValue;
		private final IntegralSensor source;
		
		/**
		 * Constructs an IntegralSensorEvent from the specified source, value, and delta value.
		 * 
		 * @param source The source of the event.
		 * @param currentValue The current value of the sensor.
		 * @param deltaValue The delta value of the sensor.
		 */
		public IntegralSensorEvent(IntegralSensor source, int currentValue, int deltaValue) {
			this.source = source;
			this.currentValue = currentValue;
			this.deltaValue = deltaValue;
		}
		
		/**
		 * Returns the source of the event.
		 * 
		 * @return The source of the event.
		 */
		public IntegralSensor source() {
			return source;
		}
	}
	
	/**
	 * The previous value of the sensor.
	 */
	protected int oldValue;
	
	/**
	 * Constructs an IntegralSensor with the specified ID.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public IntegralSensor(long sensorId) {
		super(sensorId);
		oldValue = value();
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		int currentValue = value(), deltaValue = currentValue - oldValue;
		if(deltaValue != 0) {
			fireEvent(new IntegralSensorEvent(this, currentValue, deltaValue));
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
	public abstract int value();
	
	/**
	 * Fires an IntegralSensorEvent to all listeners.
	 * 
	 * @param The IntegralSensorEvent to be fired.
	 */
	protected abstract void fireEvent(IntegralSensorEvent ev);
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected abstract boolean isValidListener(SensorListener listener);
}