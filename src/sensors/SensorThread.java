package sensors;

import java.util.Vector;

/**
 * A thread that checks all sensors for whether they should fire an event.
 * 
 * @author Colin Poler
 */
public class SensorThread extends Thread {
	/**
	 * The singleton thread.
	 */
	public static final SensorThread thread = new SensorThread();
	
	private int pollDelay;
	private Vector<Sensor> sensors;
	private SensorThread() {
		pollDelay = 20;
		sensors = new Vector<Sensor>();
	}
	
	/**
	 * Tells every sensor to check for events every duration of the poll delay.
	 */
	public void run() {
		while(!sensors.isEmpty()) {
			for(int i = 0; i < sensors.size(); i++) {
				sensors.get(i).checkForEvents();
			}
			try {
				Thread.sleep(pollDelay);
			} catch(InterruptedException e) {}
		}
	}
	
	/**
	 * Adds a sensor to the list of those that should be checked.
	 * 
	 * @param sensor The sensor to add.
	 */
	public void addSensor(Sensor sensor) {
		boolean threadNotRunning = sensors.isEmpty();
		if(!sensors.contains(sensor)) {
			sensors.add(sensor);
		}
		if(threadNotRunning) {
			start();
		}
	}
	
	/**
	 * Removes a sensor from the list of those that should be checked.
	 * 
	 * @param sensor The sensor to remove.
	 */
	public void removeSensor(Sensor sensor) {
		sensors.remove(sensor);
	}
	
	/**
	 * Sets the delay after which each sensor checks for changes. By default, it is every 20ms.
	 * 
	 * @param delay The delay between each poll of the sensor.
	 */
	public void setPollDelay(int delay) {
		this.pollDelay = delay;
	}
}