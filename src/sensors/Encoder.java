package sensors;

/**
 * An encoder sensor.
 * 
 * @author Colin Poler
 */
public class Encoder extends AnalogSensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface EncoderListener extends AnalogSensorListener {
		public void encoder(AnalogSensorEvent ev);
	}
	
	/**
	 * The hardware sensor behind this object.
	 */
	public final edu.wpi.first.wpilibj.Encoder sensor;
	public final int clicksPerRevolution;
	public final double wheelRadius; // in METERS!!!!!!!!
	/**
	 * Constructs an Encoder with the specified ID, A-channel and B-channel.
	 * 
	 * @param sensorId The ID of the sensor.
	 * @param aChannel The A-channel of the sensor.
	 * @param bChannel The B-channel of the sensor.
	 */
	public Encoder(long sensorId, int aChannel, int bChannel, int clicksPerRevolution, double wheelRadius) {
		super(sensorId);
		sensor = new edu.wpi.first.wpilibj.Encoder(aChannel, bChannel);
		setThreshold(Math.PI / 100);
		this.clicksPerRevolution = clicksPerRevolution;
		this.wheelRadius = wheelRadius;
		
		this.start(); // NEVER FORGET FEB 17 2012
	}
	
	
	/* Starts the encoder.
	 * 
	 */
	public final void start()
	{
		sensor.start();
	}
	
	/* Reset the encoder.
	 * 
	 */
	public final void reset()
	{
		sensor.reset();
	}
	
	/**
	 * Determines the type of the sensor.
	 * 
	 * @return Sensors.Types.ENCODER.
	 */
	public short type() {
		return Types.ENCODER;
	}
	
	/**
	 * Gets the value of the encoder in radians
	 * 
	 * @return The current radians value of the encoder.
	 */
	public double value() {
		// NEGATE TO MAKE VALUE POSITIVE WHEN GOING FORWARD!
		return -sensor.getRaw() * 2 * Math.PI / clicksPerRevolution;
	}
	
	/**
	 * Gets the distance traveled in meters since the encoder began
	 * 
	 * @return The current number of meters the encoder has traveled
	 */
	public double distance()
	{
		return value()*wheelRadius;
	}
	
	/**
	 * Fires an AnalogSensorEvent to all listeners.
	 */
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((EncoderListener) listeners.elementAt(i)).encoder(ev);
		}
	}
	
	/**
	 * Checks if the specified listener is an EncoderListener.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof EncoderListener);
	}
}