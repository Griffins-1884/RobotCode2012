package _static;

import com.sun.squawk.util.MathUtils;

public class Location {
	/**
	 * The x and y components of the location
	 */
	public final double x, y;
	
	
	/**
	 * Constructs a location from the given x and y components
	 * 
	 * @param x The x-component of the location.
	 * @param y The y-component of the location.
	 */
	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}
        
        public static final double realTargetHeight = 0.4572; // target height in meters
        public static final double realTargetWidth = 0.6096; // target width in meters
        public static final double focalLength = 0.0004; // Axis 206 camera focal length in meters
        public static final double elevationDifference = 1.0; // TODO: CHANGE; is elevation difference between camera's lens and 
        // center of rectangle
        public static final double axis206FOV = 0.9424777961; // 54 degrees in radians. A doc suggested 48 degrees is better
        public static final double FOVHeightPixels = 240.0;
        public static final double FOVWidthPixels = 320.0;
        
        // See the whitepaper for the process
        
        /**
	 * Gets the location of the target, setting the robot as the origin.
         * See the whitepaper for more on the process.
	 * 
         * @param COM_normalized_x The center of mass x-coordinate (normalized between -1 and 1) of the rectangle
         * @param COM_normalized_y The center of mass y-coordinate (normalized between -1 and 1) of the rectangle
         * @param rectangleHeight The height of the rectangle in pixels
         * @param rectangleWidth the width of the rectangle in pixels
         * 
	 * @return The Location of the rectangle in x and y space, with x defined as the axis in
         * the direction of the robot and y rotated 90 degrees CW from that. The robot is at the origin.
	 */
        public static Location getLocationOfRectangle(double COM_normalized_x, double COM_normalized_y, double rectangleHeight, double rectangleWidth)
        {
            double hImage; // height of rectangle in meters on the camera's "plate"
            double wImage;
            
            // tan FOV/2 = (wPlate/2)/f (see paper page 2)
            // wPlate is the width of the camera's plate, which we need to find wImage
            
            // MAYBE LOOK THESE UP INSTEAD?
            double wPlate = Math.tan(axis206FOV/2.0)*focalLength*2.0;
            double hPlate = wPlate*3.0/4.0; // Is this true? Because it's 320 x 240
            
            hImage = rectangleHeight/FOVHeightPixels*hPlate; // convert pixel height to meter height
            wImage = rectangleWidth/FOVWidthPixels*wPlate;
            
            double alpha = MathUtils.asin(2.0*elevationDifference*hImage/(focalLength*realTargetHeight))/2.0;
            
            double distance = elevationDifference/Math.sin(alpha);
            
            double beta = MathUtils.acos((distance*wImage)/(focalLength*realTargetWidth));
            
            if(COM_normalized_x < 0)
            {
                beta *= -1; // resolve sign ambiguity due to arccos
            }
            
            double y = distance*Math.sin(beta); // to return
            double distanceAlongFloor = distance*Math.cos(alpha); // horizontal distance along floor
            
            double horizontalBeta = MathUtils.asin(y/distanceAlongFloor);
            
            double x = distanceAlongFloor*Math.cos(horizontalBeta);
            
            return new Location(x, y);
        }
        
        
        /**
	 * Converts the location to the String
	 * 
	 * @return Return a string describing this Location
	 */
        public String toString()
        {
            return "X: " + x + "\nY:" + y;
        }
	
	/**
	 * Gets the distance to another location.
	 * 
	 * @return The distance to the specified location.
	 */
	public double distanceTo(Location l) {
		return Math.sqrt((l.x - x) * (l.x - x) + (l.y - y) * (l.y - y));
	}
	
	/**
	 * The direction of the vector.
	 * 
	 * @return The direction of the vector.
	 */
	public double directionTo(Location l) {
		return MathUtils.atan2(l.y - y, l.x - x);
	}
}