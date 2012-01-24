package preY2011.California;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

import driveSystems.*;

import _static.*;
import input.*;

public class TeleopController extends Controller {
	
	private final Joystick leftJoystick, rightJoystick;
	private boolean oneJoystick = false;
	
	public TeleopController(AbstractRobot robot) {
		super(robot);
		leftJoystick = new AdjustableJoystick(1);
		rightJoystick = new AdjustableJoystick(2);
	}
	
	public void initialize() {}
	
	private boolean previousButton2State = false;
	
	public void periodic() {
		
		if(rightJoystick.trigger()) {
			try {
				ParticleAnalysisReport[] reports = ((Robot) robot).camera.trackRectangles();
				for (int i = 0; i < reports.length; i++) {                                // print results
					ParticleAnalysisReport r = reports[i];
					System.out.println("\n\nParticle: " + i
							+ "\nCenter of mass x normalized: " + r.center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + r.center_mass_y_normalized
							+ "\nWidth: " + r.boundingRectWidth
							+ "\nHeight: " + r.boundingRectHeight);
				}
				
				// Turn towards the first rectangle
				double tolerance = 0.05;
				boolean movementMade = false;
				
				// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
				if(reports.length > 0)
				{
					if(reports[0].center_mass_x_normalized > tolerance) {
						robot.driveSystem.move(new Movement(new Vector(0, 0), -0.5));
						movementMade = true;
					} else if(reports[0].center_mass_x_normalized < -tolerance) {
						robot.driveSystem.move(new Movement(new Vector(0, 0), 0.5));
						movementMade = true;
					}	
				}
				
				if(movementMade) { // If the robot did not move by itself, allow the driver to continue
					Watchdog.getInstance().feed();
					return;
				}
				
			} catch (AxisCameraException ex) {
				ex.printStackTrace();
			} catch (NIVisionException ex) {
				ex.printStackTrace();
			}
		}
		
		if((leftJoystick.button(2) || rightJoystick.button(2)) && !previousButton2State) {
			oneJoystick = !oneJoystick;
			previousButton2State = true;
		} else if(!leftJoystick.button(2) && !rightJoystick.button(2) && previousButton2State) {
			previousButton2State = false;
		}
		
		if(oneJoystick) {
			double rotation = rightJoystick.right() / 2;
			if(rightJoystick.button(4)) {
				rotation *= 2;
			}
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0), rotation));
		} else {
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.forward() + leftJoystick.forward()) / 2.0, 0), (rightJoystick.forward() - leftJoystick.forward()) / 2.0));
		}
		
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}