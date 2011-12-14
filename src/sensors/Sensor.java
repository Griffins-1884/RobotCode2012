package sensors;

import java.util.Vector;

/**
 * A sensor supporting listeners, encapsulating much of the functionality of the sensors in the package, and should be extended to provide more kinds of sensors.
 * 
 * @author Colin Poler
 */
// TODO add tutorial for making new sensor
public abstract class Sensor {
	/**
	 * An interface that should be extended by each sensor type to include a method they call with their events.
	 * 
	 * @author Colin Poler
	 */
	public static interface SensorListener {}
	
	/**
	 * A class that should be extended by each sensor type with an event for that sensor.
	 * 
	 * @author Colin Poler
	 */
	public static class SensorEvent {}
	
	/**
	 * A class (more closely resembles an enum) that describes the various types of sensors implemented so far. Shorts were used so that sensors can be classified by (Sensor.type() / 8192).
	 * 
	 * @author Colin Poler
	 */
	public static class Types {
		public final static short ABSTRACT_ANALOG_SENSOR = 0,
									  ACCELEROMETER = 1,
									  COMPASS = 2,
									  GYRO = 3,
									  ENCODER = 4,
									  ULTRASONIC_RANGEFINDER = 5,
								  ABSTRACT_BOOLEAN_SENSOR = 8192,
									  PNEUMATIC_PRESSURE_SENSOR = 8193,
								  ABSTRACT_DIGITAL_SENSOR = 16384,
								  	  LIGHT_SENSOR = 16385,
								  OTHER_SENSOR = 24576;
	}
	
	/**
	 * The ID of the sensor. It is used to identify sensors so that duplicates are not made.
	 */
	public final long sensorId;
	
	/**
	 * The objects that are listening to the sensor.
	 */
	protected Vector listeners;
	
	/**
	 * Constructs a new Sensor with the specified sensor ID.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public Sensor(long sensorId) {
		this.sensorId = sensorId;
	}
	
	/**
	 * Adds a listener to the sensor. The listener is checked to make sure it is valid (e.g. AnalogSensor ensures it is an AnalogSensorListener)
	 * 
	 * @param listener The SensorListener to add.
	 */
	public void addListener(SensorListener listener) {
		if(listener != null && isValidListener(listener)) {
			listeners.addElement(listener);
			if(listeners.size() >= 1) {
				SensorThread.thread.addSensor(this);
			}
		}
	}
	
	/**
	 * Removes the listener from the sensor.
	 * 
	 * @param listener The SensorListener to remove.
	 */
	public void removeListener(SensorListener listener) {
		if(listener != null) {
			listeners.removeElement(listener);
			if(listeners.size() == 0) {
				SensorThread.thread.removeSensor(this);
			}
		}
	}
	
	/**
	 * Determines the type of the sensor. It should just return a value from Sensor.Types, no funny business.
	 * 
	 * @return The type of the sensor.
	 */
	public abstract short type();
	
	/**
	 * Checks if there are events from the sensor.
	 */
	protected abstract void checkForEvents();
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected abstract boolean isValidListener(SensorListener listener);
}