/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensors;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author griffins
 */
public class Ultrasonic extends Sensor {

    public static final double INCHES_PER_VOLT = 512.0 / 5.07; // Vcc/512 volts per inch, so 512/Vcc inches per volt
    public static final double METERS_PER_INCH = 0.0254; // 0.0254 meters in an inch
    public static final double METERS_PER_VOLT = METERS_PER_INCH * INCHES_PER_VOLT;

    /**
     * An interface that should be implemented by all listeners.
     *
     * @author AceBoi
     */
    public static interface UltrasonicListener extends SensorListener {

        public void ultrasonic(UltrasonicEvent ev);
    }

    /**
     * A SensorEvent for Ultrasonics.
     *
     * @author AceBoi
     */
    public static class UltrasonicEvent extends SensorEvent {

        /**
         * The distance measurement of the ultrasonic.
         */
        public final double distance;
        /**
         * The sensor firing this event.
         */
        public final Ultrasonic source;

        /**
         * Constructs an UltrasonicEvent from the specified source, value, and
         * delta value.
         *
         * @param source The source of the event.
         * @param measurement The most recent measurement (in meters) taken by
         * the Ultrasonic
         */
        public UltrasonicEvent(Ultrasonic source, double measurement) {
            this.source = source;
            this.distance = measurement;
        }
    }
    private final AnalogChannel ultrasonic;
    private double rawOutput; // raw output in volts
    private double meterOutput;

    /**
     * Constructs an Ultrasonic with the specified ID and address.
     *
     * @param sensorId The ID of the sensor.
     * @param analogChannel The channel in the Analog module for this Ultrasonic
     */
    public Ultrasonic(long sensorId, int analogChannel) {
        super(sensorId);

        ultrasonic = new AnalogChannel(analogChannel);
        rawOutput = 0;
    }
    
    /**
     * Gets distance reading from ultrasonic.
     */
    public double getDistance()
    {
        checkForEvents();
        return meterOutput;
    }

    /**
     * Checks if the sensor should notify its listeners.
     */
    protected void checkForEvents() {
        double currentRawReading = ultrasonic.getVoltage();

        if (currentRawReading != rawOutput) {
            rawOutput = currentRawReading;
            meterOutput = rawOutput * METERS_PER_VOLT;

            fireEvent(new UltrasonicEvent(this, meterOutput));
        }
    }

    /**
     * Fires an UltrasonicEvent to all of the listeners.
     *
     * @param ev The UltrasonicEvent to be fired.
     */
    protected void fireEvent(UltrasonicEvent ev) {
        
        if(listeners == null)
            return;
        
        for (int i = 0; i < listeners.size(); i++) {
            ((UltrasonicListener) listeners.elementAt(i)).ultrasonic(ev);
        }
    }

    /**
     * Determines the type of the sensor. It should just return a value from
     * Sensor.Types, no funny business.
     *
     * @return The type of the sensor.
     */
    public short type() {
        return Sensor.Types.ULTRASONIC_RANGEFINDER;
    }

    /**
     * Checks if the SensorListener is a listener for the actual sensor.
     *
     * @param listener The SensorListener to check
     * @return True if it is valid, false if not.
     */
    protected boolean isValidListener(SensorListener listener) {
        return listener instanceof UltrasonicListener;
    }
}
