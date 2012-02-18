package Y2012.shooting;

import spatial.Location;
import spatial.RectangleMatch;
import spatial.Tracking;
import spatial.Vector;
import Y2012.Robot;
import actions.Action;
import actions.ActionListener;
import actions.Interval;
import actions.MultiAction;
import driveSystems.Movement;
import edu.wpi.first.wpilibj.ModdedSmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

public class LineUpAndAim extends Action implements ActionListener {
	
	public static class RectangleChoice {
		public static final int TOP = 0,
				MIDDLE_LEFT = 1,
				MIDDLE_RIGHT = 2,
				BOTTOM = 3;
	}
	
	public int choice; // is a RectangleChoice
	public Vector vectorToRectangle;
	// public AutoAim aimAction;
	public Aim aimAction;
	public static final int waitTimeInMilliseconds = 100;

	public LineUpAndAim(int choice, MultiAction parent) {
		super(parent);
		this.choice = choice;
	}

	protected void act() {
		
		boolean doneTurning = false;
		
		while(!doneTurning)
		{
			try {
				RectangleMatch[] reports = Robot.robot.camera.trackRectangles(); // TODO add to parameters, not direct access

				// Turn towards the first rectangle
				double tolerance = 0.025;

				RectangleMatch[] bestReports = getBestReports(reports);

				System.out.println(reports.length);

				
				// Center_of_mass_y_normalized is negative when above the origin
				
				RectangleMatch topReport = null;
				double topCenterY = 1.0;
				RectangleMatch bottomReport = null;
				double bottomCenterY = -1.0;
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
					if(bestReports[i].center_mass_y_normalized <= topCenterY) {
						topReport = bestReports[i];
						topCenterY = bestReports[i].center_mass_y_normalized;
					}

					// Check if bottom
					if(bestReports[i].center_mass_y_normalized >= bottomCenterY) {
						bottomReport = bestReports[i];
						bottomCenterY = bestReports[i].center_mass_y_normalized;
					}

					// Check if this report is in the middle
					if(bestReports[i].center_mass_y_normalized <= bottomCenterY && bestReports[i].center_mass_y_normalized >= topCenterY) {

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

				if(choice == RectangleChoice.TOP) {
					rectangleChosen = topReport;
					elevation = Tracking.TOP_ELEVATION;
				} else if(choice == RectangleChoice.MIDDLE_LEFT) {
					rectangleChosen = middleLeftReport;
					elevation = Tracking.MIDDLE_ELEVATION;
				} else if(choice == RectangleChoice.MIDDLE_RIGHT) {
					rectangleChosen = middleRightReport;
					elevation = Tracking.MIDDLE_ELEVATION;
				} else if(choice == RectangleChoice.BOTTOM) {
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
					multiplier = Math.max(multiplier, 0.15);


					if(rectangleChosen.center_mass_x_normalized > tolerance) {
						Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), multiplier * 0.45));
					} else if(rectangleChosen.center_mass_x_normalized < -tolerance) {
						Robot.robot.driveSystem.move(new Movement(new Vector(0, 0, 0), multiplier * -0.45));
					}
					else
					{
						doneTurning = true;
					}

					System.out.println("\n\nChosen rectangle: "
							+ "\nCenter of mass x normalized: " + rectangleChosen.center_mass_x_normalized
							+ "\nCenter of mass y normalized: " + rectangleChosen.center_mass_y_normalized
							+ "\nWidth: " + rectangleChosen.boundingRectWidth
							+ "\nHeight: " + rectangleChosen.boundingRectHeight);

					Location rectangleLocation = new Location(0, 0, elevation);

					// We are assuming a constant elevation difference between the camera and the target's CENTER!
					// This is defined in the Location class
					vectorToRectangle = Tracking.getVectorToTarget(rectangleChosen, rectangleLocation);
					
					System.out.println(vectorToRectangle);
				}
			} catch(AxisCameraException ex) {
				ex.printStackTrace();
			} catch(NIVisionException ex) {
				ex.printStackTrace();
			}
			
			
			// Wait
			try {
				Thread.sleep(waitTimeInMilliseconds);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		// At this point, we've found the vector to the rectangle. Just shoot
		//aimAction = new AutoAim(vectorToRectangle, Robot.robot.shootingApparatus, parent);
		aimAction = new Aim(-1.0, Robot.robot.shootingApparatus, parent);
		aimAction.addListener(this);
		aimAction.start();
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

	protected void destroy() {
		aimAction.stop();
	}

	public Interval duration() {
		return new Interval(1000); // dunno
	}
	
	public void actionCompleted(Action source) {
		stop();
	}
}
