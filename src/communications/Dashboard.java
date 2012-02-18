package communications;

import spatial.Location;
import edu.wpi.first.wpilibj.SmartDashboard;

public class Dashboard {
	static {
		SmartDashboard.init();
	}
	public void displayLocation(Location location, double rotation) {
		String value = "{x:" + location.x + "," + 
						"y:" + location.y + "," + 
						"z:" + location.z + "," + 
						"r:" + rotation + "}";
		SmartDashboard.log(value, "__location__");
	}
	public void overlay(int x, int y, int height, int width, int color) {
		String value = "{x:" + x + "," + 
						"y:" + y + "," + 
						"h:" + height + "," + 
						"w:" + width + "," + 
						"c:" + color + "}";
		SmartDashboard.log(value, "__overlay__");
	}
}