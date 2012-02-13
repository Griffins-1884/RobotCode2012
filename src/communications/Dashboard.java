package communications;

import edu.wpi.first.wpilibj.SmartDashboard;

public class Dashboard {
	static {
		SmartDashboard.init();
	}
	public void overlay(int x, int y, int h, int w, int c) {
		String value = "{x:" + x + "," + 
						"y:" + y + "," + 
						"h:" + h + "," + 
						"w:" + w + "," + 
						"c:" + c + "}";
		SmartDashboard.log(value, "__overlay__");
	}
}