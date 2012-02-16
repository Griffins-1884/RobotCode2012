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
		public void accelerometer(AccelerometerEvent ev);
	}
	
	
	/**
	 * The hardware sensor behind this object.
	 * NOTE: we are using the ADXL335 3-axis +/- 3g accelerometer here.
	 */
	public final edu.wpi.first.wpilibj.AnalogChannel xChannel;
	public final edu.wpi.first.wpilibj.AnalogChannel yChannel;
	public final edu.wpi.first.wpilibj.AnalogChannel zChannel;
	
	
	/**
	 * Constructs a 2-axis Accelerometer with the specified ID, and three channels (any of which can be -1 depending on usage).
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param channelX The x channel of the sensor.
	 * @param channelY The y channel of the sensor.
	 * @param channelZ The z channel of the sensor.
	 */
	public Accelerometer(long sensorId, int channelX, int channelY, int channelZ) {
		super(sensorId);
		if(channelX > 0) {
			xChannel = new edu.wpi.first.wpilibj.AnalogChannel(channelX);
		} else {xChannel = null;}
		if(channelY > 0) {
			yChannel = new edu.wpi.first.wpilibj.AnalogChannel(channelY);
		} else {yChannel = null;}
		if(channelZ > 0) {
			zChannel = new edu.wpi.first.wpilibj.AnalogChannel(channelZ);
		} else {zChannel = null;}
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
	 * A SensorEvent for Accelerometers. Created because accelerometer uses three axes.
	 * 
	 * @author Colin Poler
	 */
	public static class AccelerometerEvent extends AnalogSensorEvent {
		/**
		 * The current values of the sensor.
		 */
		public final double currentXValue;
		public final double currentYValue;
		public final double currentZValue;
		
		/**
		 * The change from the previous values of the sensor.
		 */
		public final double deltaXValue;
		public final double deltaYValue;
		public final double deltaZValue;
		
		/**
		 * Constructs an AnalogSensorEvent from the specified source, value, and delta value.
		 * 
		 * @param source The source of the event.
		 * @param currentValue The current value of the sensor.
		 * @param deltaValue The delta value of the sensor.
		 */
		public AccelerometerEvent(AnalogSensor source, double currentValue, double deltaValue, double currentXValue, double deltaXValue, double currentYValue, double deltaYValue, double currentZValue, double deltaZValue) {
			super(source, currentValue, deltaValue);
			this.currentXValue = currentXValue;
			this.deltaXValue = deltaXValue;
			this.currentYValue = currentYValue;
			this.deltaYValue = deltaYValue;
			this.currentZValue = currentZValue;
			this.deltaZValue = deltaZValue;
		}
	}
	
	private double oldXValue, oldYValue, oldZValue;
	
	/**
	 * Because we want to listen to all axes, we override this to check every axis.
	 */
	public void checkForEvents() {
		double currentValue = value(), deltaValue = currentValue - oldValue;
		double currentXValue = value(), deltaXValue = currentXValue - oldXValue;
		double currentYValue = value(), deltaYValue = currentYValue - oldYValue;
		double currentZValue = value(), deltaZValue = currentZValue - oldZValue;
		if(Math.abs(deltaValue) >= threshold || Math.abs(deltaXValue) >= threshold || Math.abs(deltaYValue) >= threshold || Math.abs(deltaZValue) >= threshold) {
			oldValue = currentValue;
			oldXValue = currentXValue;
			oldYValue = currentYValue;
			oldZValue = currentZValue;
			fireEvent(new AccelerometerEvent(this, currentValue, deltaValue, currentXValue, deltaXValue, currentYValue, deltaYValue, currentZValue, deltaZValue));
		}
	}
	
	/**
	 * Gets the net XY acceleration of the robot in m/s^2
	 * This is the magnitude of the net acceleration vector, which has
	 * x and y components.
	 * 
	 * @return The current net acceleration value of the accelerometer.
	 */
	public double value() {
		double accX = xValue();
		double accY = yValue();
		double accZ = zValue();
		
		return Math.sqrt(accX*accX + accY*accY + accZ*accZ);
	}
	
	/**
	 * Gets the X acceleration of the robot in m/s^2
	 * 
	 * @return The current x acceleration value of the accelerometer.
	 */
	public double xValue() {
		return sensorAcceleration(xChannel);
	}
	
	/**
	 * Gets the Y acceleration of the robot in m/s^2
	 * 
	 * @return The current y acceleration value of the accelerometer.
	 */
	public double yValue() {
		return sensorAcceleration(yChannel);
	}
	
	/**
	 * Gets the Z acceleration of the robot in m/s^2
	 * 
	 * @return The current z acceleration value of the accelerometer.
	 */
	public double zValue() {
		return sensorAcceleration(zChannel);
	}

	/**
	 * Gets the acceleration of the robot in m/s^2 for this specific axis
	 * Nominal output is 1.5 V at 0 g, plus 300 mV/g
	 * Can measure +/- 3g for each axis
	 * 
	 * @param sensor The sensor we're using. One sensor per axis.
	 * 
	 * @return The acceleration of the robot along the specified axis
	 */
	public static double sensorAcceleration(edu.wpi.first.wpilibj.AnalogChannel sensor) {
		if(sensor == null) { // Because we don't require all three axes
			return 0.0;
		}
		
		double rawVoltage = sensor.getVoltage();
		rawVoltage -= 1.5;
		
		double value = rawVoltage/0.3; // 0.3 V per g
		value *= 9.80;
		
		return value;
	}

	/**
	 * Fires an AnalogSensorEvent to all listeners for this accelerometer.
	 */
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((AccelerometerListener) listeners.elementAt(i)).accelerometer((AccelerometerEvent) ev);
		}
	}
	
	/**
	 * Checks if the specified listener is an AccelerometerListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof AccelerometerListener);
	}
	
}
