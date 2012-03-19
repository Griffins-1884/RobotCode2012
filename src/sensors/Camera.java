package sensors;

import spatial.RectangleMatch;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCamera.ResolutionT;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;


/**
 * A camera (no shape tracking).
 *
 * @author Colin Poler
 */
public class Camera extends Sensor {

    /**
     * An interface that should be implemented by all listeners.
     *
     * @author Colin Poler
     */
    public static interface CameraListener extends SensorListener {

        public void camera(CameraEvent ev);
    }

    /**
     * A SensorEvent for Cameras.
     *
     * @author Colin Poler
     */
    public static class CameraEvent extends SensorEvent {

        /**
         * The image of the camera.
         */
        public final ColorImage image;
        /**
         * The sensor firing this event.
         */
        public final Camera source;

        /**
         * Constructs an CameraEvent from the specified source, value, and delta
         * value.
         *
         * @param source The source of the event.
         * @param image The most recent image taken by the camera.
         */
        public CameraEvent(Camera source, ColorImage image) {
            this.source = source;
            this.image = image;
        }
    }
    private final AxisCamera camera;
    public final Servo tiltServo;
    private final Relay ledRing;
    private CriteriaCollection cc;      // The criteria for doing the particle filter operation

    /**
     * Constructs a Camera with the specified ID, address (can be null), tilt servo (can be null) and LED ring (can be null).
     *
     * @param sensorId The ID of the sensor.
     * @param address The address at which to find the camera
     * @param tiltServo The servo used for tilting the camera
     * @param ledRing The LED ring used by the camera
     */
    public Camera(long sensorId, String address, Servo tiltServo, Relay ledRing) {
        super(sensorId);
        if (address != null) {
            camera = AxisCamera.getInstance(address);
        } else {
            camera = AxisCamera.getInstance();
        }
        camera.writeResolution(ResolutionT.k320x240);
        camera.writeCompression(30);
        
        this.tiltServo = tiltServo;
        this.ledRing = ledRing;

        Timer.delay(2); // Sometimes, the cRIO starts before the camera so we have to put in a wait

        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 6, 320, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 8, 240, false);
    }

    /**
     * Checks if the sensor should notify its listeners.
     */
    protected void checkForEvents() {
        if (camera.freshImage()) {
            try {
                fireEvent(new CameraEvent(this, image()));
            } catch (AxisCameraException e) {
            } catch (NIVisionException e) {
            }
        }
    }

    /**
     * Determines the type of the sensor. It should just return a value from
     * Sensor.Types, no funny business.
     *
     * @return The type of the sensor.
     */
    public short type() {
        return Sensor.Types.CAMERA;
    }

    /**
     * Gets the value of the sensor.
     *
     * @return The value of the sensor.
     */
    public ColorImage image() throws AxisCameraException, NIVisionException {
        return camera.getImage();
    }

    /**
     * Tilts the camera to the specified angle, in degrees. This year, 60 is the center on our mount.
     *
     * @param angle The angle to tilt to
     */
    public void tilt(double angle) {
    	if(tiltServo != null) {
    		tiltServo.setAngle(angle);
    	}
    }

    /**
     * Gets the camera's current tilt value, in radians, with zero being the center
     *
     * @return The angle the camera is facing
     */
    public double angle() {
    	if(tiltServo != null) {
    		return tiltServo.get();
    	}
    	return 0.0;
    }

    /**
     * Turns the led ring on or off. IMPORTANT: LEDs must be mounted correctly. This will not work if the voltage must be reversed!
     *
     * @param on Whether the LED ring should be on or off.
     */
    public void setLEDRing(boolean on) {
    	if(ledRing != null) {
    		if(on) {
    			ledRing.set(Value.kReverse);
    		} else {
    			ledRing.set(Value.kOff);
    		}
    	}
    }

    /**
     * Gets the CriteriaCollection for vision tracking.
     *
     * @return The CriteriaCollection object.
     */
    public CriteriaCollection cc() {
        return cc;
    }

    public RectangleMatch[] trackRectangles() throws AxisCameraException, NIVisionException {
        ColorImage colorImage = image();
		
		// Hue from 104 to 187
		// Saturation from 112 to 255
		// Luminance from 53 to 208
		// Then convex hull
		// Then remove small objects
		
		// HSL thresholds as of March 8, 2012 (shots of red baskets at WPI) 
        BinaryImage thresholdImage = colorImage.thresholdHSL(104, 187, 94, 255, 53, 208);	// Get only blue areas
        BinaryImage convexHullImage = thresholdImage.convexHull(false);					// Fill in damaged rectangles
		BinaryImage bigObjectsImage = convexHullImage.removeSmallObjects(false, 2);			// Remove smaller objects
        BinaryImage filteredImage = bigObjectsImage.particleFilter(cc());					// Find rectangles

        RectangleMatch[] result = new RectangleMatch[NIVision.countParticles(filteredImage.image)];
        for (int i = 0; i < result.length; i++) {
            result[i] = new RectangleMatch(filteredImage, i);
        }

        colorImage.free();
        thresholdImage.free();
        bigObjectsImage.free();
        convexHullImage.free();
        filteredImage.free();

        return result;
    }

    /**
     * Fires a CameraEvent to all of the listeners.
     *
     * @param ev The CameraEvent to be fired.
     */
    protected void fireEvent(CameraEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            ((CameraListener) listeners.elementAt(i)).camera(ev);
        }
    }

    /**
     * Checks if the SensorListener is a listener for the actual sensor.
     *
     * @param listener The SensorListener to check
     * @return True if it is valid, false if not.
     */
    protected boolean isValidListener(SensorListener listener) {
        return listener instanceof CameraListener;
    }
}