package spatial;

import sensors.*;
import sensors.Accelerometer.AccelerometerEvent;
import sensors.Accelerometer.AccelerometerListener;
import sensors.AnalogSensor.AnalogSensorEvent;
import sensors.Gyro.GyroListener;

public class LocationTracker implements AccelerometerListener, GyroListener {
	private final Accelerometer accelerometer;
	private final Gyro gyro;
	public LocationTracker(Accelerometer accelerometer, Gyro gyro) {
		this.accelerometer = accelerometer;
		this.gyro = gyro;
		accelerometer.addListener(this);
		gyro.addListener(this);
	}
	
	private Vector velocity;
	private Location location;
	private long previousUpdate;
	public void update() {
		long millisecondsSincePreviousUpdate = System.currentTimeMillis();
		// TODO mathy stuff here
		previousUpdate = System.currentTimeMillis();
	}
	public void gyro(AnalogSensorEvent ev) {
		update();
	}
	public void accelerometer(AccelerometerEvent ev) {
		update();
	}
	
	public void correctLocation(Location correction) {
		this.location = correction;
	}
	public void correctVelocity(Vector correction) {
		this.velocity = correction;
	}
}