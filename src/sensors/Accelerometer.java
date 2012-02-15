package sensors;

/*
 * NB: we are using the ADXL335 3-axis +/- 3g accelerometer here.
 * This class only uses two of the axes (x and y).
 */ 

public class Accelerometer extends AnalogSensor {

	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 */
	public static interface AccelerometerListener extends AnalogSensorListener {
		public void accelerometer(AnalogSensorEvent ev);
	}
	
	
	/**
	 * The hardware sensor behind this object.
	 * NOTE: we are using the ADXL335 3-axis +/- 3g accelerometer here.
	 * This class only uses two of the axes (x and y).
	 */
	public final edu.wpi.first.wpilibj.AnalogChannel xSensor;
	public final edu.wpi.first.wpilibj.AnalogChannel ySensor;
	
	
	/**
	 * Constructs a 2-axis Accelerometer with the specified ID, and two channels.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channel The channel of the sensor.
	 */
	public Accelerometer(long sensorId, int channelX, int channelY) {
		super(sensorId);
		xSensor = new edu.wpi.first.wpilibj.AnalogChannel(channelX);
		ySensor = new edu.wpi.first.wpilibj.AnalogChannel(channelY);
	}
	
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.ACCELEROMETER.
	 */
	public short type() {
		return Types.ACCELEROMETER;
	}
	
	/**
	 * Gets the net XY acceleration of the robot in m/s^2
	 * This is the magnitude of the net acceleration vector, which has
	 * x and y components.
	 * 
	 * @return The current net acceleration value of the accelerometer.
	 */
	public double value() {
		double accX = sensorAcc(xSensor);
		double accY = sensorAcc(ySensor);
		
		return Math.sqrt(accX*accX + accY*accY);
	}

	/**
	 * Gets the acceleration of the robot in m/s^2 for this specific axis
	 * Nominal output is 1.5 V at 0 g, plus 300 mV/g
	 * Can measure +/- 3g for each axis
	 * 
	 * @param sensor The sensor we're using. One sensor per axis.
	 * 
	 * @return The x acceleration of the robot
	 */
	public double sensorAcc(edu.wpi.first.wpilibj.AnalogChannel sensor) {
		double rawVoltage = sensor.getVoltage();
		rawVoltage -= 1.5;
		
		double acc = rawVoltage/0.3; // 0.3 V per g
		acc *= 9.80;
		
		return acc;
	}

	/**
	 * Fires an AnalogSensorEvent to all listeners for this accelerometer.
	 */
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((AccelerometerListener) listeners.elementAt(i)).accelerometer(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is an AccelerometerListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof AccelerometerListener);
	}
	
}
