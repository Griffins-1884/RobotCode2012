package Y2012;

import Y2012.shooting.AutoAim;
import spatial.Location;
import spatial.RectangleMatch;
import spatial.Tracking;
import spatial.Vector;
import Y2012.shooting.ShootingApparatus.BeltDirection;
import driveSystems.Movement;
import input.Joystick;
import edu.wpi.first.wpilibj.Watchdog;

import _static.*;
import driveSystems.CaliforniaDrive;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import edu.wpi.first.wpilibj.ModdedSmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import input.AdjustableJoystick;
import sensors.BooleanSensor.BooleanSensorEvent;
import sensors.LightSensor.LightSensorListener;

public class TeleopController extends Controller implements LightSensorListener {
	public final Joystick leftJoystick, rightJoystick;


	public TeleopController(Robot robot) {
		super(robot);
		leftJoystick = new Joystick(1);
		rightJoystick = new Joystick(2);
	}

	public void initialize() {
		if(AutonomousController.actions != null)
			AutonomousController.actions.stop();
		
		//ModdedSmartDashboard.log(-1, "Distance to basket");

		
		robot.driveSystem.move(new Movement(new Vector(0, 0, 0), 0));
		robot.camera.setLEDRing(true);
		robot.camera.tilt(77);
		robot.shootingApparatus.upperSensor.addListener(this);
	}
	
	private double powerToShootAt = 1.0;
	private final double HIGH_POWER = 1.0;
	private final double LOW_POWER = 0.9;
	private boolean previousLowSpeedButtonState = false;
	private boolean previousHighSpeedButtonState = false;
	
	public void periodic() {
		//System.out.println("Encoder angle: " + robot.encoder.value()*180./Math.PI + " degrees");
		//System.out.println("Distance travelled: " + robot.encoder.distance() + " meters");
		//System.out.println(robot.accelerometer.xValue() +" " + robot.accelerometer.yValue());
		
		cameraServo();
		
		boolean cameraMadeMovement;
		
		try {			
			cameraMadeMovement = cameraTrack();


			if(!cameraMadeMovement) {
				drive();
			}

			monodent();


			// Choose high or low speed
			if(!previousHighSpeedButtonState && !robot.buttonBox.getDigital(ButtonBoxTypes.HIGH_SPEED)) 
			{
				powerToShootAt = HIGH_POWER;
				previousHighSpeedButtonState = true;
			}
			else if(robot.buttonBox.getDigital(ButtonBoxTypes.HIGH_SPEED))
			{
				previousHighSpeedButtonState = false;
			}
			
			if(!previousLowSpeedButtonState && !robot.buttonBox.getDigital(ButtonBoxTypes.LOW_SPEED)) 
			{
				powerToShootAt = LOW_POWER;
				previousLowSpeedButtonState = true;
			}
			else if(robot.buttonBox.getDigital(ButtonBoxTypes.LOW_SPEED))
			{
				previousLowSpeedButtonState = false;
			}

			boolean doingIntake = intake();
			
			if(!robot.buttonBox.getDigital(ButtonBoxTypes.FIRE)) {
				dumbShoot(-powerToShootAt); // TODO: Add various constant speeds
			} else if(!doingIntake && robot.buttonBox.getDigital(ButtonBoxTypes.FIRE)) {
				//System.out.println("Turn off belts");
				turnOffBeltsAndShooter();
			}

		} catch(EnhancedIOException ex) {
			ex.printStackTrace();
		}

		// smartShoot(); // uncomment later

		Watchdog.getInstance().feed();
	}
	
	public static class ButtonBoxTypes
	{
		public static final int HIGH_SPEED = 11,
				LOWER_BELT_OVERRIDE = 8,
				LOW_SPEED = 7,
				FIRE = 9,
				INTAKE = 1,
				OUTTAKE = 10,
				AIM_TOP = 6,
				AIM_MIDDLE_LEFT = 2,
				AIM_MIDDLE_RIGHT = 12,
				AIM_BOTTOM = 4,
				MONODENT_DEPLOY = 3,
				MONODENT_RETRACT = 5;
	}
	
	
	public void continuous() {}

	
	public static final double RAMP_DECREMENT = 0.01;
	/* Turns off belts and ramps down shooter.
	 * 
	 */
	public void turnOffBeltsAndShooter()
	{
		shooting = false;
		
		// Turn off all belts
		robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
		robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
		targetWasReached = false;

		// Set jaguar to zero by ramping
		double targetPower = 0.0;
		double currentPower = robot.shootingApparatus.previousPower;

		int sign = 1;

		if(targetPower < currentPower) {
			sign *= -1;
		}

		if(Math.abs(targetPower - currentPower) < RAMP_DECREMENT) {
			robot.shootingApparatus.setPower(targetPower);
		} else {
			robot.shootingApparatus.setPower(currentPower + sign * RAMP_DECREMENT);
		}

		//System.out.println("Current power: " + currentPower);
	}
	
	
	/* Shoots using the vector found through camera target tracking
	 * 
	 */
	public void smartShoot() throws EnhancedIOException
	{
		if(vectorToShootAt == null)
			return; // do nothing
		
		double distanceAlongFloor = vectorToShootAt.horizontalProjection().magnitude();
		
		double muzzleVelocity = distanceAlongFloor/Math.cos(AutoAim.ANGLE) * 
				Math.sqrt(AutoAim.GRAV_CONSTANT / 
				( 2*(distanceAlongFloor*Math.tan(AutoAim.ANGLE) - (vectorToShootAt.z)) ) );
		
		double power = AutoAim.findJagInput(muzzleVelocity);
		
		if(Math.abs(power) > 1)
		{
			//System.out.println("Can't make shot, need too high of a jaguar input");
			power = 1;
		}
		
		dumbShoot(-power);
	}
	

	public static final double angleStep = 35./50.; // turn 35 degrees each second
	public static final double maxAngle = 146;
	public static final double minAngle = 26;
	
	public void cameraServo()
	{
		double currentAngle = robot.camera.tiltServo.getAngle();
						
		if(rightJoystick.button(2))
		{
			if(currentAngle-angleStep >= minAngle)
			{
				robot.camera.tilt(currentAngle-angleStep*1.5);
			}
			else
			{
				robot.camera.tilt(minAngle);
			}
		}
		
		if(rightJoystick.button(1))
		{
			if(currentAngle+angleStep <= maxAngle)
			{
				robot.camera.tilt(currentAngle + angleStep*3); // have to fight gravity
			}
			else
			{
				robot.camera.tilt(maxAngle);
			}
		}
		
	}


	boolean targetWasReached = false;
	boolean shooting = false;
	boolean waitBeforeShooting = false;
	long timeOfShot; // the time when the shot was made (i.e. when the ball left while shooting)
	long targetReachedTime;
	public static final int TIME_BETWEEN_SHOTS = 1100; // time in milliseconds
	public static final int TIME_BEFORE_INTAKE = 3500;

	public void dumbShoot(double targetPower) throws EnhancedIOException
	{
		// Get motor up to speed
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

			shooting = false;
			robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
			robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
		}
		else
		{
			long timePassedSinceTargetReached = System.currentTimeMillis()-targetReachedTime;

			
			if(timePassedSinceTargetReached > TIME_BEFORE_INTAKE)
			{
				shooting = true;
				long currentTime = System.currentTimeMillis();
				
				if(waitBeforeShooting && currentTime - timeOfShot > TIME_BETWEEN_SHOTS)
				{
					waitBeforeShooting = false;
				}
				
				//System.out.println("Intake button: " + !robot.buttonBox.getDigital(ButtonBoxTypes.INTAKE));
				
				
				if(!waitBeforeShooting && !robot.buttonBox.getDigital(ButtonBoxTypes.INTAKE))
				{
					//System.out.println("IN HERE");
					robot.shootingApparatus.setLowerBelt(BeltDirection.UP);
					robot.shootingApparatus.setUpperBelt(BeltDirection.UP);
				}
				else
				{
					//System.out.println("DELAYING " + System.currentTimeMillis());
					robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
					robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
				}
			}
			else
			{
				shooting = false;
				robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);
				robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			}
		}

	}

	public boolean intake() throws EnhancedIOException
	{
		boolean lowerBeltOverride = false;
		
		if(!robot.buttonBox.getDigital(ButtonBoxTypes.LOWER_BELT_OVERRIDE))
		{
			robot.shootingApparatus.setLowerBelt(BeltDirection.DOWN);
			lowerBeltOverride = true;
		}
		
		if(!robot.buttonBox.getDigital(ButtonBoxTypes.INTAKE))
		{
			if(!robot.shootingApparatus.upperSensor.value()) // false means no ball
				robot.shootingApparatus.setUpperBelt(BeltDirection.UP);
			else
				robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			
			if(!lowerBeltOverride)
				robot.shootingApparatus.setLowerBelt(BeltDirection.UP);

			return true;
		}
		else if(!robot.buttonBox.getDigital(ButtonBoxTypes.OUTTAKE))
		{
			robot.shootingApparatus.setUpperBelt(BeltDirection.DOWN);
			robot.shootingApparatus.setLowerBelt(BeltDirection.DOWN);

			return true;
		}
		else if(lowerBeltOverride)
		{
			return true;
		}
		else
		{
			robot.shootingApparatus.setUpperBelt(BeltDirection.STOP);
			robot.shootingApparatus.setLowerBelt(BeltDirection.STOP);

			return false;
		}


	}


	private Vector vectorToShootAt; // this is the vector we pass into the autoAim method

	/* Tracks multiple rectangles
	 *
	 * @return true if the robot did move for vision tracking, or false if
	 * the robot did not move
	 */
	public boolean cameraTrack() throws EnhancedIOException {

		// If no buttons pressed, return and do nothing
		if(robot.buttonBox.getDigital(ButtonBoxTypes.AIM_TOP)
				&& robot.buttonBox.getDigital(ButtonBoxTypes.AIM_MIDDLE_LEFT)
				&& robot.buttonBox.getDigital(ButtonBoxTypes.AIM_MIDDLE_RIGHT)
				&& robot.buttonBox.getDigital(ButtonBoxTypes.AIM_BOTTOM))
		{
			return false;
		}
		
		try {
			//long processingStartTime = System.currentTimeMillis();

			RectangleMatch[] reports = robot.camera.trackRectangles();

			/*
			 * for (int i = 0; i < reports.length; i++) { // print results
			 * RectangleMatch r = reports[i]; System.out.println("\n\nParticle:
			 * " + i + "\nCenter of mass x" + r.center_mass_x_normalized +
			 * "\nCenter of mass y:" + r.center_mass_y_normalized + "\nWidth:" +
			 * r.boundingRectWidth + "\nHeight: " + r.boundingRectHeight); 
				 }
			 */


			// Turn towards the first rectangle
			double tolerance = 0.06;
			boolean movementMade = false;

			//System.out.println(reports.length);

			RectangleMatch[] bestReports = getBestReports(reports);

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

			RectangleMatch rectangleChosen = topReport; // the middle rectangles will be the first two we see
			double elevation = Tracking.TOP_ELEVATION;
			boolean showAllRectangles = false;

			// Button choices are arranged in same formation as rectangles
			if(!robot.buttonBox.getDigital(ButtonBoxTypes.AIM_TOP)) {
				rectangleChosen = topReport;
				elevation = Tracking.TOP_ELEVATION;
			} else if(!robot.buttonBox.getDigital(ButtonBoxTypes.AIM_MIDDLE_LEFT)) {
				rectangleChosen = middleLeftReport;
				elevation = Tracking.MIDDLE_ELEVATION;
			} else if(!robot.buttonBox.getDigital(ButtonBoxTypes.AIM_MIDDLE_RIGHT)) {
				rectangleChosen = middleRightReport;
				elevation = Tracking.MIDDLE_ELEVATION;
			} else if(!robot.buttonBox.getDigital(ButtonBoxTypes.AIM_BOTTOM)) {
				rectangleChosen = bottomReport;
				elevation = Tracking.BOTTOM_ELEVATION;
			} else // if no buttons pressed, display all of them
			{
				showAllRectangles = true;
			}

//			ModdedSmartDashboard.overlayStart();
//
//			// Display rectangle
//			if(showAllRectangles) // display all four if one isn't locked on
//			{
//				for(int i = 0; i < bestReports.length; i++) {
//					if(bestReports[i] == null) {
//						break;
//					}
//
//					ModdedSmartDashboard.overlay(bestReports[i].center_mass_x, bestReports[i].center_mass_y, bestReports[i].boundingRectWidth, bestReports[i].boundingRectHeight);
//				}
//			} else {
//				if(rectangleChosen != null) {
//					ModdedSmartDashboard.overlay(rectangleChosen.center_mass_x, rectangleChosen.center_mass_y, rectangleChosen.boundingRectWidth, rectangleChosen.boundingRectHeight);
//				}
//			}
//
//			//ModdedSmartDashboard.overlayEnd();


			// Turn towards rectangle chosen
			// TODO: Maybe use PID to set angle instead of rotating at a constant angular velocity
			if(rectangleChosen != null) {
				double multiplier = Math.abs(rectangleChosen.center_mass_x_normalized);
				multiplier = Math.max(multiplier, 0.5);

				//System.out.println("Multiplier: " + multiplier);

				double forwardMovement = (rightJoystick.forward()+leftJoystick.forward())/2.0;
				
				if(rectangleChosen.center_mass_x_normalized > tolerance) {
					robot.driveSystem.move(new Movement(new Vector(forwardMovement, 0, 0), multiplier * 0.38));
					movementMade = true;
				} else if(rectangleChosen.center_mass_x_normalized < -tolerance) {
					robot.driveSystem.move(new Movement(new Vector(forwardMovement, 0, 0), multiplier * -0.38));
					movementMade = true;
				}

				/*
				 * System.out.println("\n\nChosen rectangle: " + "\nVertical
				 * angle: " +
				 * Tracking.getVerticalAngleToRectangle(rectangleChosen, 0) +
				 * "\nCenter of mass x normalized: " +
				 * rectangleChosen.center_mass_x_normalized + "\nCenter of mass
				 * y normalized: " + rectangleChosen.center_mass_y_normalized +
				 * "\nWidth: " + rectangleChosen.boundingRectWidth + "\nHeight:
				 * " + rectangleChosen.boundingRectHeight);
				 */

				Location rectangleLocation = new Location(0, 0, elevation);

				vectorToShootAt = Tracking.getVectorToTarget(rectangleChosen, rectangleLocation);
				
				ModdedSmartDashboard.log(vectorToShootAt.horizontalProjection().magnitude(), "Distance to basket");
				
				// We are assuming a constant elevation difference between the camera and the target's CENTER!
				// This is defined in the Location class
				//System.out.println(vectorToShootAt);
				//System.out.println("Time taken: " + (int) (System.currentTimeMillis() - processingStartTime));
			}

			if(movementMade) {
				return true;
			} else {
				return false;
			}

		} catch(AxisCameraException ex) {
			ex.printStackTrace();
		} catch(NIVisionException ex) {
			ex.printStackTrace();
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

	private boolean singleJoystick = false; // SET TO FALSE!!!!!
	private boolean previousButton9State = false;
	public final double rampStep = 0.04;

	public void drive() {

		/*if(rightJoystick.button(9) && !previousButton9State)
		{	
			singleJoystick = !singleJoystick;
			previousButton9State = true;
		}
		else if(!rightJoystick.button(9))
		{
			previousButton9State = false;
		}*/
		
		// oldVector updates every time we call CaliforniaDrive's move method
		Vector oldVector = ((CaliforniaDrive) robot.driveSystem).oldVector;
		double currentX = oldVector.x;
		double targetX; // for ramping Jaguars so Suryansh doesn't tip over the robot again
		double rotation;

		if(singleJoystick) {
			targetX = rightJoystick.forward();
			rotation = (rightJoystick.right() + rightJoystick.clockwise()) / 2;

		} else { // Double joystick
			// Divide both by 2 so that sensitivity doesn't max out when both joysticks are at halfway
			targetX = (rightJoystick.forward() + leftJoystick.forward()) / 2.0;
			rotation = (rightJoystick.forward() - leftJoystick.forward()) / 2.0;
		}

		if(Math.abs(currentX - targetX) < rampStep) {
			robot.driveSystem.move(new Movement(new Vector(targetX, 0, 0), rotation)); // go to the target value if the step is small
		} else if(currentX < targetX) {
			robot.driveSystem.move(new Movement(new Vector(currentX + rampStep, 0, 0), rotation));
		} else if(currentX > targetX) {
			robot.driveSystem.move(new Movement(new Vector(currentX - rampStep, 0, 0), rotation));
		}
	}

	private boolean previousMonodentUpButtonState = false, previousMonodentDownButtonState = false;

	public void monodent() throws EnhancedIOException {

		// Button 3 goes up, button 2 goes down
		boolean currentMonodentUpButtonState = !robot.buttonBox.getDigital(ButtonBoxTypes.MONODENT_RETRACT),
				currentMonodentDownButtonState = !robot.buttonBox.getDigital(ButtonBoxTypes.MONODENT_DEPLOY);

		if(currentMonodentUpButtonState && currentMonodentDownButtonState) {
			//System.out.println("Off");
			robot.monodent.off();
		} else if(currentMonodentDownButtonState || leftJoystick.button(1)) { //for control by both driver and controller
			//System.out.println("Down");
			robot.monodent.down();
		} else if(currentMonodentUpButtonState || leftJoystick.button(2)) {//for control by both driver and controller
			//System.out.println("Up");
			robot.monodent.up();
		} else if(!currentMonodentUpButtonState || !currentMonodentDownButtonState) {
			//System.out.println("Off");
			robot.monodent.off();
		}

		previousMonodentUpButtonState = currentMonodentUpButtonState;
		previousMonodentDownButtonState = currentMonodentDownButtonState;
	}

	// LightSensorListener method
	public void lightSensor(BooleanSensorEvent ev) {
		
		// The shooting variable is set to true every time the motor is powered
		// and the belt is turning
		if(shooting)
		{
			if(!ev.source.value()) // A ball has left while shooting
			{
				waitBeforeShooting = true;
				timeOfShot = System.currentTimeMillis();
			}
		}
	}
}