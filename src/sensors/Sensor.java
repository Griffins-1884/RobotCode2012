package sensors;

public abstract class Sensor {
	public static interface SensorListener {}
	public static class SensorEvent {}
	public static class Types {
		public final static int GYRO = 0;
	}
	
	public final long sensorId;
	protected SensorListener[] listeners;
	public Sensor(long sensorId) {
		this.sensorId = sensorId;
	}
	public void addListener(SensorListener listener) {
		if(listener != null && isValidListener(listener)) {
			SensorListener[] newListeners = new SensorListener[listeners.length + 1];
			for(int i = 0; i < listeners.length; i++) {
				newListeners[i] = listeners[i];
			}
			newListeners[newListeners.length] = listener;
			listeners = newListeners;
			if(newListeners.length == 1) {
				thread().run();
			}
		}
	}
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
	public void setPollFrequency(int delay) {
		if(thread() != null) {
			thread().pollFrequency = delay;
		}
	}
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
	protected abstract SensorThread thread();
	public abstract int type();
	protected abstract boolean isValidListener(SensorListener listener);
}