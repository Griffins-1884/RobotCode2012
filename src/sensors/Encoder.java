package sensors;

// TODO should Encoder be analog or integral?
public class Encoder extends AnalogSensor {
	public static interface EncoderListener extends AnalogSensorListener {
		public void encoder(AnalogSensorEvent ev);
	}
	
	public final edu.wpi.first.wpilibj.Encoder sensor;
	public Encoder(long sensorId, int aChannel, int bChannel) {
		super(sensorId);
		sensor = new edu.wpi.first.wpilibj.Encoder(aChannel, bChannel);
		setThreshold(Math.PI / 100);
	}
	public short type() {
		return Types.ENCODER;
	}
	public double value() {
		// TODO convert to radians
		return sensor.get();
	}
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.length; i++) {
			((EncoderListener) listeners[i]).encoder(ev);
		}
	}
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof EncoderListener);
	}
}