package communications;

import spatial.Location;
import edu.wpi.first.wpilibj.SmartDashboard;

public class Dashboard {
	static {
		SmartDashboard.init();
	}
	public static void displayLocation(Location location, double rotation) {
		String value = "{x:" + location.x + "," + 
						"y:" + location.y + "," + 
						"z:" + location.z + "," + 
						"r:" + rotation + "}";
		SmartDashboard.log(value, "__location__");
	}
	public static void overlay(int x, int y, int height, int width, int color) {
		String value = "{x:" + x + "," + 
						"y:" + y + "," + 
						"h:" + height + "," + 
						"w:" + width + "," + 
						"c:" + color + "}";
		SmartDashboard.log(value, "__overlay__");
	}
}