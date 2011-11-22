package sensors;

public abstract class AnalogSensor extends Sensor {
	public static interface AnalogSensorListener extends SensorListener {}
	public static class AnalogSensorEvent extends SensorEvent {
		public final double currentValue, deltaValue;
		private final AnalogSensor source;
		public AnalogSensorEvent(AnalogSensor source, double currentValue, double deltaValue) {
			this.source = source;
			this.currentValue = currentValue;
			this.deltaValue = deltaValue;
		}
		public AnalogSensor source() {
			return source;
		}
	}

	protected AnalogSensorThread pollThread;
	public AnalogSensor(long sensorId) {
		super(sensorId);
	}
	protected AnalogSensorThread thread() {
		return pollThread;
	}
	public void setThreshold(double threshold) {
		pollThread.threshold = threshold;
	}
	protected abstract class AnalogSensorThread extends SensorThread {
		protected final AnalogSensor owner;
		protected double oldValue, threshold;
		protected AnalogSensorThread(AnalogSensor owner) {
			this.owner = owner;
			oldValue = value();
			threshold = Math.PI / 100;
		}
		protected void checkForEvents() {
			double currentValue = value(), deltaValue = currentValue - oldValue;
			if(Math.abs(deltaValue) >= threshold) {
				fireEvent(new AnalogSensorEvent(owner, currentValue, deltaValue));
			}
		}
	}
	public abstract int type();
	public abstract double value();
	protected abstract void fireEvent(AnalogSensorEvent ev);
	protected abstract boolean isValidListener(SensorListener listener);
}