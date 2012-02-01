package preY2012.California;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.ModdedSmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

import driveSystems.*;

import _static.*;
import input.*;
import image.*;

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
	private double rotation = 0;
	
	public void periodic() {
		
		if(rightJoystick.trigger()) {
			try {
				RectangleMatch[] reports = ((Robot) robot).camera.trackRectangles();
				/*for (int i = 0; i < reports.length; i++) {                                // print results
					ParticleAnalysisReport r = reports[i];
					System.out.println("\n\nParticle: " + i
							+ "\nCenter of mass x normalized: " + r.center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + r.center_mass_y_normalized
							+ "\nWidth: " + r.boundingRectWidth
							+ "\nHeight: " + r.boundingRectHeight);
				}*/
				
				// Turn towards the first rectangle
				double tolerance = 0.025;
				boolean movementMade = false;
				
				// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
				if(reports.length > 0) {
					int bestReport = 0;
					for(int i = 1; i < reports.length; i++) {
						if(reports[bestReport].particleQuality < reports[i].particleQuality) {
							bestReport = i;
						}
					}
					double multiplier = Math.abs(reports[bestReport].center_mass_x_normalized);
					multiplier = Math.max(multiplier, 0.1);
					
					
					if(reports[bestReport].center_mass_x_normalized > tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * 0.45));
						movementMade = true;
					} else if(reports[bestReport].center_mass_x_normalized < -tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * -0.45));
						movementMade = true;
					}
					
					ModdedSmartDashboard.overlayStart();
					ModdedSmartDashboard.overlay(reports[bestReport].center_mass_x, reports[bestReport].center_mass_y, reports[bestReport].boundingRectWidth, reports[bestReport].boundingRectHeight);
					//ModdedSmartDashboard.overlayEnd();
					
					
					System.out.println("\n\nBest Particle: "
							+ "\nCenter of mass x normalized: " + reports[bestReport].center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + reports[bestReport].center_mass_y_normalized
							+ "\nWidth: " + reports[bestReport].boundingRectWidth
							+ "\nHeight: " + reports[bestReport].boundingRectHeight);
					
					// We are assuming a constant elevation difference between the camera and the target's CENTER!
					// This is defined in the Location class
					System.out.println(Tracking.findRectangle(reports[bestReport]));
				}
				
				Watchdog.getInstance().feed();
				return;
				
			} catch (AxisCameraException ex) {
				ex.printStackTrace();
			} catch (NIVisionException ex) {
				ex.printStackTrace();
			}
		}
		if(leftJoystick.button(7)) {
			rotation = ((Robot) robot).gyro.value();
			System.out.println(rotation);
			robot.driveSystem.move(new Movement(new Vector(rotation/30, 0, 0), 0));
		} else if(rotation != 0) {
			rotation = 0;
			((Robot) robot).gyro.reset();
		}
		
		// Ultrasonic output
		
		System.out.println("Ultrasonic output: " + ((Robot) robot).ultrasonic.getDistance() + " meters");
		
		
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
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), rotation));
		} else {
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.forward() + leftJoystick.forward()) / 2.0, 0, 0), (rightJoystick.forward() - leftJoystick.forward()) / 2.0));
		}
		
		
		
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
}