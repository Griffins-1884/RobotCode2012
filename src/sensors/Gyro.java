package sensors;

public class Gyro extends AnalogSensor {
	public static interface GyroListener extends AnalogSensorListener {
		public void gyro(AnalogSensorEvent ev);
	}
	
	public final edu.wpi.first.wpilibj.Gyro sensor;
	public Gyro(long sensorId, int slot, int channel) {
		super(sensorId);
		sensor = new edu.wpi.first.wpilibj.Gyro(slot, channel);
	}
	public int type() {
		return Types.GYRO;
	}
	public double value() {
		return sensor.getAngle() * Math.PI / 180;
	}
	protected void fireEvent(AnalogSensorEvent ev) {
		for(int i = 0; i < listeners.length; i++) {
			((GyroListener) listeners[i]).gyro(ev);
		}
	}
	protected boolean isValidListener(SensorListener listener) {
		return (listener instanceof GyroListener);
	}
}