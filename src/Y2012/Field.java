package Y2012;

import _static.Location;

public class Field {
	public static class Hoops {
		/**
		 * The top-back edge of the red hoops. Left and right are determined as if robot is in the middle of the field, looking at the hoops.
		 */
		public static final Location topRed = new Location(8.2169, 0, 2.4892),
									 middleLeftRed = new Location(8.2169, 0.695325, 1.5494),
									 middleRightRed = new Location(8.2169, -0.695325, 1.5494),
									 bottomRed = new Location(8.2169, 0, 0.7112);
		
		/**
		 * The top-back edge of the blue hoops. Left and right are determined as if robot is in the middle of the field, looking at the hoops.
		 */
		public static final Location topBlue = new Location(-8.2169, 0, 2.4892),
									 middleLeftBlue = new Location(-8.2169, -0.695325, 1.5494),
									 middleRightBlue = new Location(-8.2169, 0.695325, 1.5494),
									 bottomBlue = new Location(-8.2169, 0, 0.7112);
		
		/**
		 * The center of the bridges.
		 */
		public static final Location blueBridge = new Location(0, 3.5052, 0.3048),
									 coopertitionBridge = new Location(0, 0, 0.3048),
									 redBridge = new Location(0, -3.5052, 0.3048);
		
		/**
		 * The point to feed balls to the robot. It is 44 inches from the feeding slot.
		 */
		public static final Location blueFeedingPoint = new Location(7.366, 3.4925, 0),
									 redFeedingPoint = new Location(-7.366, -3.4925, 0);
	}
}