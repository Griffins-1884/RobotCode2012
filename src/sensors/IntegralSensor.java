package sensors;

public abstract class IntegralSensor extends Sensor {
	public static interface IntegralSensorListener extends SensorListener {}
	public static class IntegralSensorEvent extends SensorEvent {
		public final int currentValue, deltaValue;
		private final IntegralSensor source;
		public IntegralSensorEvent(IntegralSensor source, int currentValue, int deltaValue) {
			this.source = source;
			this.currentValue = currentValue;
			this.deltaValue = deltaValue;
		}
		public IntegralSensor source() {
			return source;
		}
	}

	protected IntegralSensorThread pollThread;
	public IntegralSensor(long sensorId) {
		super(sensorId);
	}
	protected IntegralSensorThread thread() {
		return pollThread;
	}
	protected abstract class IntegralSensorThread extends SensorThread {
		protected final IntegralSensor owner;
		protected int oldValue;
		protected IntegralSensorThread(IntegralSensor owner) {
			this.owner = owner;
			oldValue = value();
		}
		protected void checkForEvents() {
			int currentValue = value(), deltaValue = currentValue - oldValue;
			if(deltaValue != 0) {
				fireEvent(new IntegralSensorEvent(owner, currentValue, deltaValue));
			}
		}
	}
	public abstract short type();
	public abstract int value();
	protected abstract void fireEvent(IntegralSensorEvent ev);
	protected abstract boolean isValidListener(SensorListener listener);
}