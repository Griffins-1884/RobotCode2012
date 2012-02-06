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
	
	// Heights of centers of rectangle (in meters) relative to the camera 
	// Black rectangle has 22 inch height
	public static final double kInch = 0.0254; // an inch in meters
	public static final double blackRectangleHeight = 22*kInch;
	
	public static final double CAMERA_HEIGHT = 0.492;
	public static final double BOTTOM_ELEVATION = 28*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;
	public static final double MIDDLE_ELEVATION = 61*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;
	public static final double TOP_ELEVATION = 98*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;

	public TeleopController(Robot robot) {
		super(robot);
		leftJoystick = new AdjustableJoystick(1);
		rightJoystick = new AdjustableJoystick(2);
	}

	public void initialize() {
	}
	private boolean previousButton2State = false;
	private double rotation = 0;

	public void periodic() {

		if(rightJoystick.trigger()) {
			try {
				RectangleMatch[] reports = robot.camera.trackRectangles();
				/*
				 * for (int i = 0; i < reports.length; i++) { // print results
				 * ParticleAnalysisReport r = reports[i];
				 * System.out.println("\n\nParticle: " + i + "\nCenter of mass x
				 * normalized: " + r.center_mass_x_normalized + "\nCenter of
				 * mass y normalized: " + r.center_mass_y_normalized + "\nWidth:
				 * " + r.boundingRectWidth + "\nHeight: " +
				 * r.boundingRectHeight);
				}
				 */

				// Turn towards the first rectangle
				double tolerance = 0.025;
				boolean movementMade = false;

				RectangleMatch[] bestReports = getBestReports(reports);
				sortReportsByPosition(bestReports); // sorts reports so the highest report is first

				RectangleMatch topReport = null;
				double topCenterY = -1.0;
				RectangleMatch bottomReport = null;
				double bottomCenterY = 1.0;
				RectangleMatch middleLeftReport = null;
				double middleCenterLeftX = 1.0;
				RectangleMatch middleRightReport = null;
				double middleCenterRightX = -1.0;

				for(int i = 0; i < bestReports.length; i++) {
					// bestReports can contain up to four entries. The end ones will be null 
					if(bestReports[i] == null) {
						break;
					}

					// Check if top
					if(bestReports[i].center_mass_y_normalized >= topCenterY) {
						topReport = bestReports[i];
						topCenterY = bestReports[i].center_mass_y_normalized;
					}
					
					// Check if bottom
					if(bestReports[i].center_mass_y_normalized <= bottomCenterY) {
						bottomReport = bestReports[i];
						bottomCenterY = bestReports[i].center_mass_y_normalized;
					}

					// Check if this report is in the middle
					if(bestReports[i].center_mass_y_normalized >= bottomCenterY && bestReports[i].center_mass_y_normalized <= topCenterY) {
						
						// Check if left
						if(bestReports[i].center_mass_x_normalized <= middleCenterLeftX)
						{
							middleLeftReport = bestReports[i];
							middleCenterLeftX = bestReports[i].center_mass_x_normalized;
						}
						
						// Check if right
						if(bestReports[i].center_mass_x_normalized >= middleCenterRightX)
						{
							middleRightReport = bestReports[i];
							middleCenterRightX = bestReports[i].center_mass_x_normalized;
						}
					}

					
				}

				
				
				RectangleMatch rectangleChosen = middleLeftReport; // the middle rectangles will be the first two we see
				double elevation = MIDDLE_ELEVATION;
				boolean showAllRectangles = false;
				
				// Button choices are arranged in same formation as rectangles
				if(rightJoystick.button(3))
				{
					rectangleChosen = topReport;
					elevation = TOP_ELEVATION;
				}
				else if(rightJoystick.button(4))
				{
					rectangleChosen = middleLeftReport;
					elevation = MIDDLE_ELEVATION;
				}
				else if(rightJoystick.button(5))
				{
					rectangleChosen = middleRightReport;
					elevation = MIDDLE_ELEVATION;
				}
				else if(rightJoystick.button(2))
				{
					rectangleChosen = bottomReport;
					elevation = BOTTOM_ELEVATION;
				}
				else // if no buttons pressed, display all of them
				{
					showAllRectangles = true;
				}
				
				ModdedSmartDashboard.overlayStart();
				
				// Display rectangle
				if(showAllRectangles) // display all four if one isn't locked on
				{
					for(int i = 0; i < bestReports.length; i++)
					{
						if(bestReports[i] == null)
							break;
						
						ModdedSmartDashboard.overlay(bestReports[i].center_mass_x, bestReports[i].center_mass_y, bestReports[i].boundingRectWidth, bestReports[i].boundingRectHeight);
					}
				}
				else
				{
					if(rectangleChosen != null)
					{
						ModdedSmartDashboard.overlay(rectangleChosen.center_mass_x, rectangleChosen.center_mass_y, rectangleChosen.boundingRectWidth, rectangleChosen.boundingRectHeight);
					}
				}
				
				ModdedSmartDashboard.overlayEnd();
				
				
				// Turn towards rectangle chosen
				// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
				if(rectangleChosen != null)
				{
					double multiplier = Math.abs(rectangleChosen.center_mass_x_normalized);
					multiplier = Math.max(multiplier, 0.1);


					if(rectangleChosen.center_mass_x_normalized > tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * 0.45));
						movementMade = true;
					} else if(rectangleChosen.center_mass_x_normalized < -tolerance) {
						robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), multiplier * -0.45));
						movementMade = true;
					}
					
					System.out.println("\n\nChosen rectangle: "
							+ "\nCenter of mass x normalized: " + rectangleChosen.center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + rectangleChosen.center_mass_y_normalized
							+ "\nWidth: " + rectangleChosen.boundingRectWidth
							+ "\nHeight: " + rectangleChosen.boundingRectHeight);

					// We are assuming a constant elevation difference between the camera and the target's CENTER!
					// This is defined in the Location class
					System.out.println(Tracking.findRectangle(rectangleChosen, elevation));
				}

				Watchdog.getInstance().feed();
				return;

			} catch(AxisCameraException ex) {
				ex.printStackTrace();
			} catch(NIVisionException ex) {
				ex.printStackTrace();
			}
		}
		if(leftJoystick.button(7)) {
			rotation = robot.gyro.value();
			System.out.println("Gyro: " + rotation * 180. / Math.PI + " degrees");
			robot.driveSystem.move(new Movement(new Vector(rotation / (Math.PI / 6), 0, 0), 0));
			Watchdog.getInstance().feed();
			return;
		} else if(rotation != 0) {
			rotation = 0;
			robot.gyro.reset();
		}

		// Ultrasonic output

		if(leftJoystick.button(6)) {
			System.out.println("Ultrasonic output: " + robot.ultrasonic.value() + " meters");
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
	public RectangleMatch[] getBestReports(RectangleMatch[] reports) {
		RectangleMatch temp;
		int bestIndex;
		RectangleMatch[] result = new RectangleMatch[4];

		for(int startingIndex = 0; startingIndex < reports.length; startingIndex++) {
			bestIndex = startingIndex;

			for(int i = startingIndex; i < reports.length; i++) // find max value and its index
			{
				if(reports[i].particleQuality > reports[bestIndex].particleQuality) {
					bestIndex = i;
				}
			}

			temp = reports[bestIndex]; // store temp as the max value
			reports[bestIndex] = reports[startingIndex];
			reports[startingIndex] = temp;
		}

		// Get best four

		for(int i = 0; i < reports.length && i < 4; i++) {
			result[i] = reports[i];
		}

		return result;

	}

	public void sortReportsByPosition(RectangleMatch[] reports) {
		RectangleMatch temp;
		int maxIndex;

		for(int startingIndex = 0; startingIndex < reports.length; startingIndex++) {
			maxIndex = startingIndex;

			for(int i = startingIndex; i < reports.length; i++) // find max value and its index
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

	public void continuous() {
	}
}