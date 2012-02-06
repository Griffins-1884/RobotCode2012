package sensors;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * An ultrasonic rangefinder sensor.
 * 
 * @author Colin Poler
 */
public class Ultrasonic extends AnalogSensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface UltrasonicListener extends AnalogSensorListener {
		public void ultrasonic(AnalogSensorEvent ev);
	}
	
	/**
	 * The number of inches indicated by each volt returned.
	 */
	public static final double INCHES_PER_VOLT = 512.0 / 5.0;
	
	/**
	 * Conversion factors for distances.
	 */
	public static final double INCHES = 1.0,
							   MILLIMETERS = INCHES * 25.4, // There are 25.4 millimeters in an inch
							   METERS = MILLIMETERS / 1000.0;
	
	/**
	 * The hardware sensor behind this object.
	 */
	public final AnalogChannel sensor;
	
	/**
	 * The conversion factor to use
	 */
	public double conversionFactor;
	
	/**
	 * Constructs an Ultrasonic with the specified ID, and channel.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channel The channel of the sensor.
	 */
	public Ultrasonic(long sensorId, int channel) {
		super(sensorId);
		sensor = new AnalogChannel(channel);
		conversionFactor = METERS;
	}
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.ULTRASONIC.
	 */
	public short type() {
		return Sensor.Types.ULTRASONIC;
	}

	/**
	 * Determines the distance in front of the ultrasonic rangefinder, in the current units.
	 * 
	 * @return The distance in front of the ultrasonic rangefinder, in the current units.
	 */
	public double value() {
		if(sensor == null)
			return 0.0;
		
		return sensor.getAverageVoltage() * INCHES_PER_VOLT * conversionFactor;
	}
	
	/**
	 * Set the units of this sensor to the given unit. Use one of the constants.
	 * 
	 * @param unit The unit to return distance in.
	 */
	public void setUnit(double unit) {
		conversionFactor = unit;
	}
	
	/**
	 * Fires an AnalogSensorEvent to all listeners.
	 */
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((UltrasonicListener) listeners.elementAt(i)).ultrasonic(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is an UltrasonicListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return listener instanceof UltrasonicListener;
	}
}