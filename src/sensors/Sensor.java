package sensors;

/**
 * A sensor supporting listeners, encapsulating much of the functionality of the sensors in the package, and should be extended to provide more kinds of sensors.
 * 
 * @author Colin Poler
 */
// TODO add tutorial for making new sensor
public abstract class Sensor {
	/**
	 * An interface that should be extended by each sensor type with a method they call with their events.
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
								  OTHER_SENSOR = 24576;
	}
	
	/**
	 * A thread that polls the sensor, and checks for events.
	 * 
	 * @author Colin Poler
	 */
	// TODO should we have a centralized thread for all sensors? It would mean less ram and less CPU...
	protected abstract class SensorThread extends Thread {
		protected int pollFrequency = 20;
		public void run() {
			while(listeners.length > 0) {
				checkForEvents();
				try {
					Thread.sleep(pollFrequency);
				} catch(InterruptedException e) {}
			}
		}
		protected abstract void checkForEvents();
	}
	
	/**
	 * The ID of the sensor. It is used to identify sensors so that duplicates are not made.
	 */
	public final long sensorId;
	
	/**
	 * The objects that are listening to the sensor.
	 */
	protected SensorListener[] listeners;
	
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
			SensorListener[] newListeners = new SensorListener[listeners.length + 1];
			for(int i = 0; i < listeners.length; i++) {
				newListeners[i] = listeners[i];
			}
			newListeners[newListeners.length] = listener;
			listeners = newListeners;
			if(newListeners.length == 1) {
				thread().start();
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
			SensorListener[] newListeners = new SensorListener[listeners.length - 1];
			boolean hasBeenPassed = false;
			for(int i = 0; i < listeners.length - 1; i++) {
				// This is safe; we should not use .equals, because we want to get the actual reference
				if(listeners[i] == listener) {
					hasBeenPassed = true;
					continue;
				}
				if(!hasBeenPassed) {
					newListeners[i] = listeners[i];
				} else {
					newListeners[i] = listeners[i - 1];
				}
			}
			listeners = newListeners;
		}
	}
	
	/**
	 * Sets the frequency (actually the delay) that the sensor checks for changes. By default, it is every 20ms.
	 * 
	 * @param delay The delay between each poll of the sensor.
	 */
	public void setPollFrequency(int delay) {
		if(thread() != null) {
			thread().pollFrequency = delay;
		}
	}
	
	/**
	 * Gets the polling thread from the sensor.
	 * 
	 * @return The polling thread of the sensor.
	 */
	protected abstract SensorThread thread();
	
	/**
	 * Determines the type of the sensor. It should just return a value from Sensor.Types, no funny business.
	 * 
	 * @return The type of the sensor.
	 */
	public abstract short type();
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected abstract boolean isValidListener(SensorListener listener);
}