package image;

import _static.Vector;

import com.sun.squawk.util.MathUtils;


public class Tracking {
	public static final double realTargetHeight = 0.4572; // target height in meters
	public static final double realTargetWidth = 0.6096; // target width in meters
	public static final double focalLength = 0.0004; // Axis 206 camera focal length in meters
	public static final double axis206FOV = 48.0*Math.PI/180.0; // 54 degrees in radians. A doc suggested 48 degrees is better
	public static final double FOVHeightPixels = 240.0;
	public static final double FOVWidthPixels = 320.0;
	
	// See the whitepaper for the process
	
	/**
	 * Gets the location of the target, setting the robot as the origin.
	 * See the whitepaper for more on the process.
	 * 
	 * @param rect	The rectangle we're looking at
	 * @param elevationDifference	The difference in elevation between the camera and COM of the rect
	 * 
	 * @return The Location of the rectangle in x and y space, with x defined as the axis in
	 * the direction of the robot and y rotated 90 degrees CW from that. The robot is at the origin.
	 */
	public static Vector findRectangle(RectangleMatch rect, double elevationDifference) {
		double hImage; // height of rectangle in meters on the camera's "plate"
		double wImage;
		
		// tan FOV/2 = (wPlate/2)/f (see paper page 2)
		// wPlate is the width of the camera's plate, which we need to find wImage
		
		// MAYBE LOOK THESE UP INSTEAD?
		double wPlate = Math.tan(axis206FOV/2.0)*focalLength*2.0;
		double hPlate = wPlate*3.0/4.0; // Is this true? Because it's 320 x 240
		
		hImage = rect.boundingRectHeight/FOVHeightPixels*hPlate; // convert pixel height to meter height
		wImage = rect.boundingRectWidth/FOVWidthPixels*wPlate;
		
		double alpha = MathUtils.asin(2.0*elevationDifference*hImage/(focalLength*realTargetHeight))/2.0;
		
		double distance = elevationDifference/Math.sin(alpha);
		
		double beta;
		
		
		beta = MathUtils.acos((distance*wImage)/(focalLength*realTargetWidth));
		
		if(rect.center_mass_x_normalized < 0) {
			beta *= -1; // resolve sign ambiguity due to arccos
		}
		
		double y = distance*Math.sin(beta); // to return
		double distanceAlongFloor = distance*Math.cos(alpha); // horizontal distance along floor
		
		double horizontalBeta = MathUtils.asin(y/distanceAlongFloor);
		
		double x = distanceAlongFloor*Math.cos(horizontalBeta);
		
		System.out.println("Horizontal Beta: " + horizontalBeta*180./Math.PI);
		System.out.println("Beta: " + beta*180./Math.PI);
		System.out.println("Alpha: " + alpha*180./Math.PI);
		System.out.println("Distance: " + distance);
		System.out.println("Distance along floor: " + distanceAlongFloor);
		
		return new Vector(y, x, 0); // TODO height of rectangle?
	}
}