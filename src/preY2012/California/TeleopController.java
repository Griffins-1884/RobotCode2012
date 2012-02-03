package preY2012.California;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.ModdedSmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

import driveSystems.*;

import _static.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
				
				RectangleMatch[] bestReports = getBestReports(reports);
				sortReportsByPosition(bestReports); // sorts reports so the highest report is first
				
				// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
				if(bestReports.length > 0) {
					int bestReport = 0;
					for(int i = 1; i < bestReports.length; i++) {
						if(bestReports[bestReport].particleQuality < bestReports[i].particleQuality) {
							bestReport = i;
						}
					}
					double multiplier = Math.abs(bestReports[bestReport].center_mass_x_normalized);
					multiplier = Math.max(multiplier, 0.1);
					
					
					if(bestReports[bestReport].center_mass_x_normalized > tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * 0.45));
						movementMade = true;
					} else if(bestReports[bestReport].center_mass_x_normalized < -tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * -0.45));
						movementMade = true;
					}
					
					ModdedSmartDashboard.overlayStart();
					ModdedSmartDashboard.overlay(bestReports[bestReport].center_mass_x, bestReports[bestReport].center_mass_y, bestReports[bestReport].boundingRectWidth, bestReports[bestReport].boundingRectHeight);
					ModdedSmartDashboard.overlayEnd();
										
					System.out.println("\n\nBest Particle: "
							+ "\nCenter of mass x normalized: " + bestReports[bestReport].center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + bestReports[bestReport].center_mass_y_normalized
							+ "\nWidth: " + bestReports[bestReport].boundingRectWidth
							+ "\nHeight: " + bestReports[bestReport].boundingRectHeight);
					
					// We are assuming a constant elevation difference between the camera and the target's CENTER!
					// This is defined in the Location class
					System.out.println(Tracking.findRectangle(bestReports[bestReport]));
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
			System.out.println("Gyro: " + rotation*180./Math.PI + " degrees");
			robot.driveSystem.move(new Movement(new Vector(rotation/(Math.PI/6), 0, 0), 0));
			Watchdog.getInstance().feed();
			return;
		} else if(rotation != 0) {
			rotation = 0;
			((Robot) robot).gyro.reset();
		}
		
		// Ultrasonic output
		
		if(leftJoystick.button(6))
		{
			System.out.println("Ultrasonic output: " + ((Robot) robot).ultrasonic.value() + " meters");
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
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), rotation));
		} else {
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.forward() + leftJoystick.forward()) / 2.0, 0, 0), (rightJoystick.forward() - leftJoystick.forward()) / 2.0));
		}
		
		
		
		Watchdog.getInstance().feed();
	}
	
	
	// Get the best reports, up to a maximum of four
	public RectangleMatch[] getBestReports(RectangleMatch[] reports)
	{
		int bestIndex = 0;
		RectangleMatch[] result = new RectangleMatch[4];
		
		// FINISH THIS
		
	}
	
	
	public void sortReportsByPosition(RectangleMatch[] reports)
	{
		RectangleMatch temp;
		int maxIndex;
		
		for(int startingIndex = 0; startingIndex < reports.length; startingIndex ++)
		{
			maxIndex = startingIndex;
			
			for(int i = startingIndex; i < reports.length; i ++) // find max value and its index
			{
				if(reports[i].center_mass_y_normalized > reports[maxIndex].center_mass_y_normalized) {
					maxIndex = i;
				}
			}
			
			temp = reports[maxIndex]; // store temp as the max value
			reports[maxIndex] = reports[startingIndex];
			reports[startingIndex] = temp;
		}	
	}
	
	public void continuous() {}
}