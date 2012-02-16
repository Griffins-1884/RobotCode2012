package Y2012;

import spatial.Location;
import spatial.RectangleMatch;
import spatial.Tracking;
import spatial.Vector;
import Y2012.shooting.ShootingApparatus.BeltDirection;
import driveSystems.Movement;
import input.Joystick;
import edu.wpi.first.wpilibj.Watchdog;

import _static.*;
import edu.wpi.first.wpilibj.ModdedSmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

public class TeleopController extends Controller {
	public final Joystick leftJoystick, rightJoystick;
	
	
	public TeleopController(Robot robot) {
		super(robot);
		leftJoystick = new Joystick(1);
		rightJoystick = new Joystick(2);
	}
	
	public void initialize() {
		robot.camera.setLEDRing(true);
	}
	
	public void periodic() {
		System.out.println("Back limit switch: " + robot.monodent.backSwitch.value());
		System.out.println("Front limit switch: " + robot.monodent.frontSwitch.value());
		
		boolean cameraMadeMovement = cameraTrack();
		
		if(!cameraMadeMovement)
			drive();
		
		monodent();
		
		if(!intake())
			shoot();
		
		Watchdog.getInstance().feed();
	}
	public void continuous() {}
	
	boolean targetWasReached = false;
	long targetReachedTime;
	
	public void shoot()
	{
		if(rightJoystick.trigger())
		{	
			// Get motor up to speed
			
			double targetPower = -1.0;
			double currentPower = robot.shootingApparatus.previousPower;
			
			if(!targetWasReached && targetPower == currentPower)
			{
				targetReachedTime = System.currentTimeMillis();
				targetWasReached = true;
			}
			
			if(!targetWasReached)
			{
				int sign = 1;
				double rampIncrement = 0.03;

				if(targetPower < currentPower) {
					sign *= -1;
				}

				if(Math.abs(targetPower - currentPower) < rampIncrement) {
					robot.shootingApparatus.setPower(targetPower);
				} else {
					robot.shootingApparatus.setPower(currentPower + sign * rampIncrement);
				}
				
				robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
				robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			}
			else
			{
				long timePassedSinceTargetReached = System.currentTimeMillis()-targetReachedTime;
				
				if(timePassedSinceTargetReached > 1000)
				{
					robot.shootingApparatus.setLowerBelt(BeltDirection.UP);
					robot.shootingApparatus.setUpperBelt(BeltDirection.UP);
				}
				else
				{
					robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
					robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
				}
			}
			
		}
		else
		{
			// Turn off all belts
			robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
			targetWasReached = false;
			
			// Set jaguar to zero
			double targetPower = 0.0;
			double currentPower = robot.shootingApparatus.previousPower;
		
			int sign = 1;
			double rampIncrement = 0.03;

			if(targetPower < currentPower) {
				sign *= -1;
			}

			if(Math.abs(targetPower - currentPower) < rampIncrement) {
				robot.shootingApparatus.setPower(targetPower);
			} else {
				robot.shootingApparatus.setPower(currentPower + sign * rampIncrement);
			}

			robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
			robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
		}
	}
	
	public boolean intake()
	{
		if(leftJoystick.button(6))
		{
			/*if(!robot.shootingApparatus.upperSensor.value()) // false means no ball
				robot.shootingApparatus.setUpperBelt(BeltDirection.UP);
			else
				robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);*/
			
			robot.shootingApparatus.setUpperBelt(BeltDirection.UP);
			robot.shootingApparatus.setLowerBelt(BeltDirection.UP);
			
			return true;
		}
		else if(leftJoystick.button(7))
		{
			robot.shootingApparatus.setUpperBelt(BeltDirection.DOWN);
			robot.shootingApparatus.setLowerBelt(BeltDirection.DOWN);
			
			return true;
		}
		else
		{
			robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
			
			return false;
		}
		
		
	}
	
	/* Tracks multiple rectangles
	 * 
	 * @return true if the robot did move for vision tracking, or false if
	 * the robot did not move
	 */
	public boolean cameraTrack() {
		Joystick joystickToUse = leftJoystick;
		
		if(joystickToUse.trigger()) {
			try {
				RectangleMatch[] reports = robot.camera.trackRectangles();
				/*
				 * for (int i = 0; i < reports.length; i++) { // print results
				 * ParticleAnalysisReport r = reports[i];
				 * System.out.println("\n\nParticle: " + i + "\nCenter of mass x
				 * normalized: " + r.center_mass_x_normalized + "\nCenter of
				 * mass y normalized: " + r.center_mass_y_normalized + "\nWidth:
				 * " + r.boundingRectWidth + "\nHeight: " +
				 * r.boundingRectHeight); }
				 */

				// Turn towards the first rectangle
				double tolerance = 0.025;
				boolean movementMade = false;

				RectangleMatch[] bestReports = getBestReports(reports);

				System.out.println(reports.length);

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
						if(bestReports[i].center_mass_x_normalized <= middleCenterLeftX) {
							middleLeftReport = bestReports[i];
							middleCenterLeftX = bestReports[i].center_mass_x_normalized;
						}

					// Check if right
						if(bestReports[i].center_mass_x_normalized >= middleCenterRightX) {
							middleRightReport = bestReports[i];
							middleCenterRightX = bestReports[i].center_mass_x_normalized;
						}
					}


				}



				RectangleMatch rectangleChosen = middleLeftReport; // the middle rectangles will be the first two we see
				double elevation = Tracking.MIDDLE_ELEVATION;
				boolean showAllRectangles = false;

				// Button choices are arranged in same formation as rectangles
				if(joystickToUse.button(3)) {
					rectangleChosen = topReport;
					elevation = Tracking.TOP_ELEVATION;
				} else if(joystickToUse.button(4)) {
					rectangleChosen = middleLeftReport;
					elevation = Tracking.MIDDLE_ELEVATION;
				} else if(joystickToUse.button(5)) {
					rectangleChosen = middleRightReport;
					elevation = Tracking.MIDDLE_ELEVATION;
				} else if(joystickToUse.button(2)) {
					rectangleChosen = bottomReport;
					elevation = Tracking.BOTTOM_ELEVATION;
				} else // if no buttons pressed, display all of them
				{
					showAllRectangles = true;
				}

				ModdedSmartDashboard.overlayStart();

				// Display rectangle
				if(showAllRectangles) // display all four if one isn't locked on
				{
					for(int i = 0; i < bestReports.length; i++) {
						if(bestReports[i] == null) {
							break;
						}

						ModdedSmartDashboard.overlay(bestReports[i].center_mass_x, bestReports[i].center_mass_y, bestReports[i].boundingRectWidth, bestReports[i].boundingRectHeight);
					}
				} else {
					if(rectangleChosen != null) {
						ModdedSmartDashboard.overlay(rectangleChosen.center_mass_x, rectangleChosen.center_mass_y, rectangleChosen.boundingRectWidth, rectangleChosen.boundingRectHeight);
					}
				}

				//ModdedSmartDashboard.overlayEnd();


				// Turn towards rectangle chosen
				// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
				if(rectangleChosen != null) {
					double multiplier = Math.abs(rectangleChosen.center_mass_x_normalized);
					multiplier = Math.max(multiplier, 0.1);


					if(rectangleChosen.center_mass_x_normalized > tolerance) {
						robot.driveSystem.move(new Movement(new Vector(joystickToUse.forward(), 0, 0), multiplier * 0.45));
						movementMade = true;
					} else if(rectangleChosen.center_mass_x_normalized < -tolerance) {
						robot.driveSystem.move(new Movement(new Vector(joystickToUse.forward(), 0, 0), multiplier * -0.45));
						movementMade = true;
					}

					System.out.println("\n\nChosen rectangle: "
							+ "\nCenter of mass x normalized: " + rectangleChosen.center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + rectangleChosen.center_mass_y_normalized
							+ "\nWidth: " + rectangleChosen.boundingRectWidth
							+ "\nHeight: " + rectangleChosen.boundingRectHeight);

					Location rectangleLocation = new Location(0, 0, elevation);
					
					// We are assuming a constant elevation difference between the camera and the target's CENTER!
					// This is defined in the Location class
					System.out.println(Tracking.getVectorToTarget(rectangleChosen, rectangleLocation));
				}
				
				if(movementMade)
					return true;
				else
					return false;

			} catch(AxisCameraException ex) {
				ex.printStackTrace();
			} catch(NIVisionException ex) {
				ex.printStackTrace();
			}
		}
		
		return false;
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
	
	private boolean singleJoystick = false;
	public void drive() {
		if(singleJoystick) {
			double rotation = (rightJoystick.right() + rightJoystick.clockwise()) / 2;
			robot.driveSystem.move(new Movement(new Vector(rightJoystick.forward(), 0, 0), rotation));
		} else { // Double joystick
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			robot.driveSystem.move(new Movement(new Vector((rightJoystick.forward() + leftJoystick.forward()) / 2.0, 0, 0), (rightJoystick.forward() - leftJoystick.forward()) / 2.0));
		}
	}

	private boolean previousMonodentUpButtonState = false, previousMonodentDownButtonState = false;

	public void monodent() {
		
		// Button 3 goes up, button 2 goes down
		boolean currentMonodentUpButtonState = rightJoystick.button(3),
				currentMonodentDownButtonState = rightJoystick.button(2);
		if(currentMonodentUpButtonState == previousMonodentUpButtonState && currentMonodentDownButtonState == previousMonodentDownButtonState) {
			return;
		}
		
		if(currentMonodentUpButtonState && currentMonodentDownButtonState) {
			robot.monodent.off();
		} else if(currentMonodentDownButtonState) {
			robot.monodent.down();
		} else if(currentMonodentUpButtonState) {
			robot.monodent.up();
		}
		
		if(!currentMonodentUpButtonState || !currentMonodentDownButtonState)
		{
			robot.monodent.off();
		}
		
		previousMonodentUpButtonState = currentMonodentUpButtonState;
		previousMonodentDownButtonState = currentMonodentDownButtonState;
	}
}