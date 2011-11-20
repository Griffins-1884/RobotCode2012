/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    Gyro gyro = new Gyro(1);
    int iterationCount = 0;
    
    public PlayList actions;


    // Pneumatics
    public Compressor comp = new Compressor(5, 5); // Pressure switch is DIO 5, Relay is Relay channel 5

    public Solenoid closingSolenoid;
    public Solenoid openingSolenoid;


    // Joysticks
    SmartJoyStick driverJoystick = new SmartJoyStick(1); // twist joystick for movement


    // Button box
    DriverStationEnhancedIO buttonBox = DriverStation.getInstance().getEnhancedIO();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    }
    
}
