/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import java.util.Vector;

import edu.wpi.first.wpilibj.*;
import  edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class RobotTemplate extends IterativeRobot
{
    
    // Start camera code
    double kScoreThreshold = .01;
    boolean lastTrigger = false;

    //AxisCamera cam;
    // TrackerDashboard trackerDashboard = new TrackerDashboard();
    // End camera code
    

    Gyro gyro = new Gyro(1);
    int iterationCount = 0;
    
    
    public double originalTwist;

    
    public PlayList actions;
    AnalogChannel DIPSwitchForGoingStraight = new AnalogChannel(2); // if DIP Switch is connected, goingStraight is true
    // if DIP Switch is not connected, goingStraight is false


    // Pneumatics
    public Compressor comp = new Compressor(5, 5); // Pressure switch is DIO 5, Relay is Relay channel 5

    public Solenoid closingSolenoid;
    public Solenoid openingSolenoid;
    public boolean triggerHasBeenPressed;
    public static final int HAND_OPENING = 0;
    public static final int HAND_CLOSING = 1;
    int handState = 0;


    // Light detection
    DigitalInput centerlightSensor; // Channel one
    DigitalInput leftlightSensor; // Channel three
    DigitalInput rightlightSensor;// Channel two


    // Joysticks
    SmartJoyStick towerJoystick = new SmartJoyStick(2); // normal joystick for tower and arm
    SmartJoyStick driverJoystick = new SmartJoyStick(1); // twist joystick for movement
    //SmartJoyStick twistStick = new SmartJoyStick(3);

    //Constants for tower and arm to make things clearer
    public static final int UP = 1;
    public static final int NOTHING = 0;
    public static final int DOWN = -1;


    // Initialization code for tower stuff
    TowerActor towerControl;
    Jaguar towerJaguar = new Jaguar(6);
    Jaguar armJaguar = new Jaguar(5);
    
    AnalogChannel towerSwitchBottom = new AnalogChannel(3);
    AnalogChannel towerSwitchTop = new AnalogChannel(4);
    Encoder towerEncoder = new Encoder(11, 10);

    //Initialization code for arm stuff
    ArmActor armControl;
    boolean limitSwitchStateButtonHasBeenPressed = false; // for allowing the limit switches to make a difference to the arm movement
    AnalogChannel armSwitchBottom = new AnalogChannel(6);
    AnalogChannel armSwitchTop = new AnalogChannel(5);

    // Minibot deployment
    Jaguar minibotDeployment = new Jaguar(7);
    AnalogChannel minibotStop = new AnalogChannel(7); // this is true until the treads have moved off completely
    boolean minibotMovingOut = false;

    // Changing Gyro and Mecanum modes
    int driveState = 0;
    boolean driveStateHasBeenChanged = false;
    public static final int MECANUM_DRIVE = 0;
    public static final int GYRO_MECANUM_DRIVE = 1;

    // TowerHeightSensor setup
    TowerHeightSensor heightSensor;


    // Button box
    DriverStationEnhancedIO buttonBox = DriverStation.getInstance().getEnhancedIO();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initial code.
     */
    public void robotInit() {
        // i specifies the motor and the PWM slot on the Digital Sidecar
        // i = 1 is front left
        // i = 2 is front right
        // i = 3 is rear left
        // i = 4 is rear right

        MecanumDrive.setMotor(1, new Jaguar(1));
        MecanumDrive.setMotor(2, new Jaguar(2));
        MecanumDrive.setMotor(3, new Jaguar(3));
        MecanumDrive.setMotor(4, new Jaguar(4));

        towerEncoder.start();

      // dash = DriverStation.getInstance().getDashboardPackerHigh();
        
        gyro.setSensitivity(.007);
        System.out.println("RobotInit() completed.\n");

        closingSolenoid = new Solenoid(1);
        openingSolenoid = new Solenoid(2);

        //cam = AxisCamera.getInstance();
        //cam.writeResolution(AxisCamera.ResolutionT.k160x120);
        //cam.writeBrightness(70);

        centerlightSensor = new DigitalInput(1); // Channel one
        leftlightSensor = new DigitalInput(3); // Channel three
        rightlightSensor = new DigitalInput(2); // Channel two

        comp.start();
        minibotDeployment.set(0.0);

        closingSolenoid.set(true);
        openingSolenoid.set(false);
        triggerHasBeenPressed = false;

        heightSensor = new TowerHeightSensor(towerEncoder);
    }


    /**
     * This function is called once as soon as the robot becomes disabled.
     */
    public void disabledInit() {
        comp.start();

        if(comp.getPressureSwitchValue())
        {
            comp.setRelayValue(Relay.Value.kOff);
        }
        else
        {
            comp.setRelayValue(Relay.Value.kForward);
        }

        System.out.println("disabled init completed");
    }


    /**
     * This function is called while the robot is disabled.
     */
    public void disabledPeriodic() {
        if(comp.getPressureSwitchValue())
        {
            comp.setRelayValue(Relay.Value.kOff);
        }
        else
        {
            comp.setRelayValue(Relay.Value.kForward);
        }

        Watchdog.getInstance().feed();
        minibotDeployment.set(0.0);
    }


    /**
     * This function is called at the beginning of autonomous
     */
    public void autonomousInit()
    {
        // ADD THESE to PlayList WHEN DONE TESTING

        comp.start();
        gyro.reset();

        actions = new PlayList();

        actions.add(new RightFavoringLineTrackingAndTowerUntilLimitSwitch(leftlightSensor, centerlightSensor, rightlightSensor, towerJaguar, towerSwitchTop));

        
        if(DIPSwitchForGoingStraight.getVoltage() >= 4.5)
        { // If going straight
            actions.add(new TowerMovementForDistanceChange(towerJaguar, towerEncoder, -2.50)); // for STRAIGHT AHEAD
        }
        else
        { // If going on fork
            actions.add(new TowerMovementForDistanceChange(towerJaguar, towerEncoder, -6.00)); // for FORK
        }


        actions.add(new ArmAction(armJaguar, DOWN, 460));
        actions.add(new ForwardsMovementTimeWise(500));


        if(DIPSwitchForGoingStraight.getVoltage() >= 4.5)
        { // If going straight

        }
        else
        { // If going on fork
            actions.add(new TowerMovementForDistanceChange(towerJaguar, towerEncoder, -10.00)); // for FORK
        }


        actions.add(new OpenArm(closingSolenoid, openingSolenoid));
        actions.add(new TimerDelay(1400)); // 1400 milliseconds
        actions.add(new TowerMovementForDistanceChange(towerJaguar, towerEncoder, -18.00));
        actions.add(new BackwardsMovementTimeWise(3000));
        actions.add(new ZackNathan());

        // SimpleLineTrack cannot be used!!!!!!!

        System.out.println("autonomous init completed");
    }


   
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Watchdog.getInstance().feed();

        if(comp.getPressureSwitchValue())
        {
            comp.setRelayValue(Relay.Value.kOff);
        }
        else
        {
            comp.setRelayValue(Relay.Value.kForward);
        }

        actions.update();
    }


    /**
     * This function is called at the beginning of teleop
     */
    public void teleopInit() {
        comp.start();
        towerControl = new TowerActor(towerJaguar, towerSwitchBottom, towerSwitchTop, towerEncoder);
        armControl = new ArmActor(armJaguar, armSwitchBottom, armSwitchTop);
        originalTwist = driverJoystick.getThrottle();
        System.out.println("teleop init done");
    }


    /**
     * This function is called periodically during operator control
     */


    public void teleopPeriodic() {
        iterationCount ++;

        if(comp.getPressureSwitchValue())
        {
            comp.setRelayValue(Relay.Value.kOff);
        }
        else
        {
            comp.setRelayValue(Relay.Value.kForward);
        }

        if(iterationCount%50 == 0)
        {
            /*System.out.println("\n\nButton 1 pressed: " + driverJoystick.getRawButton(1));
            System.out.println("Button 2 pressed: " + driverJoystick.getRawButton(2));
            System.out.println("Button 3 pressed: " + driverJoystick.getRawButton(3));
            System.out.println("Button 4 pressed: " + driverJoystick.getRawButton(4));
            System.out.println("Button 5 pressed: " + driverJoystick.getRawButton(5));
            System.out.println("Button 6 pressed: " + driverJoystick.getRawButton(6));
            System.out.println("Button 7 pressed: " + driverJoystick.getRawButton(7));
            System.out.println("Button 8 pressed: " + driverJoystick.getRawButton(8));
            System.out.println("Button 9 pressed: " + driverJoystick.getRawButton(9));
            System.out.println("Button 10 pressed: " + driverJoystick.getRawButton(10));
            System.out.println("Button 11 pressed: " + driverJoystick.getRawButton(11));
            System.out.println("Button 12 pressed: " + driverJoystick.getRawButton(12));
            System.out.println("Button 13 pressed: " + driverJoystick.getRawButton(13));
            System.out.println("Button 14 pressed: " + driverJoystick.getRawButton(14));


            System.out.println("Arm height (90¼) off ground: " + heightSensor.getHeight());*/
        }
        
        Watchdog.getInstance().feed();


       // dash.commit();
       
        handleJoystickEvents();
        /* try {
            handleButtons();
        } catch (EnhancedIOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void handleButtons() throws EnhancedIOException
    {
        // Not means pressed
        
        if (!buttonBox.getDigital(2))
        { // Minibot in
            minibotDeployment.set(.75); // move in
        } 

        if (!buttonBox.getDigital(1))
        { // Minibot out
            minibotDeployment.set(-.75); // move out
        }

        if(buttonBox.getDigital(2) && buttonBox.getDigital(1))
        { // If neither minibot buttons are pressed
            minibotDeployment.set(0.0);
        }


        if (!buttonBox.getDigital(5))
        { // Arm up
            armControl.setDirectionTo(0.88); // move it up, fight gravity, maybe make this a function of height
        }

        if (!buttonBox.getDigital(7))
        { // Arm down
            armControl.setDirectionTo(-0.2);
        }

        if(buttonBox.getDigital(5) && buttonBox.getDigital(7))
        { // Neither arm buttons pressed
            armControl.setDirectionTo(0.0);
        }


        if (!buttonBox.getDigital(6))
        { // Tower up
            towerControl.setDirectionTo(UP);
        }

        if(!buttonBox.getDigital(3))
        { // Tower down
            towerControl.setDirectionTo(DOWN);
        }

        if(buttonBox.getDigital(6) && buttonBox.getDigital(3))
        { // Neither tower buttons pressed
            towerControl.setDirectionTo(NOTHING);
        }


        if(!buttonBox.getDigital(4))
        { // Fire
            if (!triggerHasBeenPressed && handState == HAND_OPENING)
            {
                closingSolenoid.set(false);
                openingSolenoid.set(true);
                triggerHasBeenPressed = true;
            }
            else if(!triggerHasBeenPressed && handState == HAND_CLOSING) {
                closingSolenoid.set(true);
                openingSolenoid.set(false);
                triggerHasBeenPressed = true;
            }
        }

        if(buttonBox.getDigital(4) && triggerHasBeenPressed)
        { // If the fire button has been released
            triggerHasBeenPressed = false;
            handState ++;
            handState %= 2;

        }


        if(driverJoystick.getRawButton(9) && armControl.usingLimitSwitches && !limitSwitchStateButtonHasBeenPressed)
        {
            armControl.usingLimitSwitches = false;
            towerControl.usingLimitSwitches = false;
            limitSwitchStateButtonHasBeenPressed = true;
        }
        else if(driverJoystick.getRawButton(9) && !armControl.usingLimitSwitches && !limitSwitchStateButtonHasBeenPressed)
        {
            armControl.usingLimitSwitches = true;
            towerControl.usingLimitSwitches = true;
            limitSwitchStateButtonHasBeenPressed = true;
        }
        else if(!driverJoystick.getRawButton(9) && limitSwitchStateButtonHasBeenPressed)
        {
            limitSwitchStateButtonHasBeenPressed = false;
        }

        towerControl.act();
        armControl.act();
    }


    // Handles all events that are determined by the joystick (drive system,
    // extend claw, deploy minibot, auto-lock, other motors, etc)
    public void handleJoystickEvents()
    {
        handleTowerMovement();
        handleArmMovement();
        handleHandMovement();
        handleMinibotDeployment();

        if(towerControl.hasHitBottom())
        {
            towerEncoder.reset(); // set it to zero at the bottom, just in case
        }

        handleDriverStateSelection();

        if(driveState == MECANUM_DRIVE)
        {
            handleMecanumMovement();
        }
        else if(driveState == GYRO_MECANUM_DRIVE)
        {
            handleMecanumMovementGyro();
        }
    }


    public void handleDriverStateSelection()
    {
        if(driverJoystick.getRawButton(2) && !driveStateHasBeenChanged)
        {
            driveStateHasBeenChanged = true;
            
            driveState ++;
            driveState %= 2;

            if(driveState == GYRO_MECANUM_DRIVE)
            {
                gyro.reset(); // reset gyro if changing to gyro drive
            }
        }
        else if (!driverJoystick.getRawButton(2))
        {
            driveStateHasBeenChanged = false;
        }
    }


    public void handleMinibotDeployment()
    {
        if(towerJoystick.getRawButton(11) && minibotStop.getVoltage() >= 4.5)
        {
            minibotDeployment.set(-.75); // move out
            minibotMovingOut = true;
        }
        else if(towerJoystick.getRawButton(10))
        {
            minibotDeployment.set(.75); // move in
            minibotMovingOut = false;
        }
        else
        {
            minibotDeployment.set(0.0);
            minibotMovingOut = false;
        }
    }


    public void handleTowerMovement()
    {
        if(towerControl.hasHitBottom())
        {
            towerEncoder.reset(); // set it to zero at the bottom, just in case
        }

        if(towerJoystick.getRawButton(3))
        {
            towerControl.setDirectionTo(UP);
        }
        else if(towerJoystick.getRawButton(2))
        {
            towerControl.setDirectionTo(DOWN);
        }
        else
        {
            towerControl.setDirectionTo(NOTHING);
        }

        towerControl.act(); // don't worry about the action being done or not
    }

    public void handleArmMovement ()
    {
        if(towerJoystick.getRawButton(5))
        {
            armControl.setDirectionTo(0.78); // move it up, fight gravity, maybe make this a function of height
        }
        else if(towerJoystick.getRawButton(4))
        {
            armControl.setDirectionTo(-0.2);
        }
        else
        {
            armControl.setDirectionTo(0.0);
        }
        armControl.act();

    }


    public void handleHandMovement () {

         if(towerJoystick.getTrigger())
         {
            if (!triggerHasBeenPressed && handState == HAND_OPENING)
            {
                closingSolenoid.set(false);
                openingSolenoid.set(true);
                triggerHasBeenPressed = true;
            }
            else if(!triggerHasBeenPressed && handState == HAND_CLOSING) {
                closingSolenoid.set(true);
                openingSolenoid.set(false);
                triggerHasBeenPressed = true;
            }

        }
        else if(!towerJoystick.getTrigger() && triggerHasBeenPressed)
        {
            triggerHasBeenPressed = false;
            handState ++;
            handState %= 2;

        }
    }


    public void handleMecanumMovement()
    {
        double forward = 0.0;
        double right = 0.0;
        double clockwise = 0.0;

        if (Math.abs(driverJoystick.getSmartX()) >= 0.05) {
            right = driverJoystick.getSmartX() * driverJoystick.getSmartX();

            if(driverJoystick.getSmartX() < 0.0)
            {
                right *= -1.0;
            }
        }

        if (Math.abs(driverJoystick.getSmartY()) >= 0.05) {
            forward = driverJoystick.getSmartY() * driverJoystick.getSmartY();

            if(driverJoystick.getSmartY() < 0.0)
            {
                forward *= -1.0;
            }
        }

        double throttle = driverJoystick.getThrottle() - originalTwist;
        clockwise = throttle * .40;

        if(minibotMovingOut) // so it's easier to control the turning
        {
            clockwise *= 0.6;
        }

        // Code for Logitech Joysticks, not Saitek one
        /*if (driverJoystick.getRawButton(4))
        {
            clockwise = 0.6;
        }
        if (driverJoystick.getRawButton(5))
        {
            clockwise = -0.6;
        }*/


        MecanumDrive.drive(-forward, right, clockwise);

    }

    public void handleMecanumMovementGyro()
    {
        double forward = 0.0;
        double right = 0.0;
        double clockwise = 0.0;


        if (driverJoystick.getRawButton(5)){ // button 5 on SAITEK
            gyro.reset();
        }

        if (Math.abs(driverJoystick.getSmartX()) >= 0.05) {
            right = driverJoystick.getSmartX() * driverJoystick.getSmartX();

            if(driverJoystick.getSmartX() < 0.0)
            {
                right *= -1.0;
            }
        }

        if (Math.abs(driverJoystick.getSmartY()) >= 0.05) {
            forward = driverJoystick.getSmartY() * driverJoystick.getSmartY();

            if(driverJoystick.getSmartY() < 0.0)
            {
                forward *= -1.0;
            }
        }

        // System.out.println((int)((driverJoystick.getThrottle()-originalTwist)*1000) +";" +originalTwist);

        double throttle = driverJoystick.getThrottle() - originalTwist;
        clockwise = throttle * .40;

        if(minibotMovingOut) // so it's easier to control the turning
        {
            clockwise *= 0.6;
        }

        // Code for Logitech Joysticks, not Saitek one
        /*if (driverJoystick.getRawButton(4))
        {
            clockwise = 0.6;
        }
        if (driverJoystick.getRawButton(5))
        {
            clockwise = -0.6;
        }*/


        double angle = -Math.PI/180 * (gyro.getAngle()); // negative since it's upside down

        
        MecanumDrive.drive(-(forward*Math.cos(angle)-
        right*Math.sin(angle)),
                forward*Math.sin(angle)+
        right*Math.cos(angle)
        , clockwise);
       

    }

}