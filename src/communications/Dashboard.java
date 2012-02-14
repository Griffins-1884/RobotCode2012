package communications;

import edu.wpi.first.wpilibj.SmartDashboard;

public class Dashboard {
	static {
		SmartDashboard.init();
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