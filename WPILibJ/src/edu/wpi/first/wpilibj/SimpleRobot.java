/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

/**
 * A simple robot base class that knows the standard FRC competition states (disabled, autonomous, or operator controlled).
 *
 * You can build a simple robot program off of this by overriding the
 * robotinit(), disabled(), autonomous() and operatorControl() methods. The startCompetition() method will calls these methods
 * (sometimes repeatedly).
 * depending on the state of the competition.
 * 
 * Alternatively you can override the robotMain() method and manage all aspects of the robot yourself.
 */
public class SimpleRobot extends RobotBase {

    private boolean m_robotMainOverridden;

    /**
     * Create a new SimpleRobot
     */
    public SimpleRobot() {
        super();
        m_robotMainOverridden = true;
    }

    /**
     * Robot-wide initialization code should go here.
     *
     * Users should override this method for default Robot-wide initialization which will
     * be called when the robot is first powered on.
     *
     * Called exactly 1 time when the competition starts.
     */
    protected void robotInit() {
        System.out.println("Default robotInit() method running, consider providing your own");
    }

    /**
     * Disabled should go here.
     * Users should overload this method to run code that should run while the field is
     * disabled.
     *
     * Called repeatedly while the robot is in the disabled state.
     */
    protected void disabled() {
        System.out.println("Default disabled() method running, consider providing your own");
    }

    /**
     * Autonomous should go here.
     * Users should add autonomous code to this method that should run while the field is
     * in the autonomous period.
     *
     * Called repeatedly while the robot is in the autonomous state.
     */
    public void autonomous() {
        System.out.println("Default autonomous() method running, consider providing your own");
    }

    /**
     * Operator control (tele-operated) code should go here.
     * Users should add Operator Control code to this method that should run while the field is
     * in the Operator Control (tele-operated) period.
     * 
     * Called repeatedly while the robot is in the operator-controlled state.
     */
    public void operatorControl() {
        System.out.println("Default operatorControl() method running, consider providing your own");
    }

    /**
     * Robot main program for free-form programs.
     *
     * This should be overridden by user subclasses if the intent is to not use the autonomous() and
     * operatorControl() methods. In that case, the program is responsible for sensing when to run
     * the autonomous and operator control functions in their program.
     *
     * This method will be called immediately after the constructor is called. If it has not been
     * overridden by a user subclass (i.e. the default version runs), then the robotInit(), disabled(), autonomous() and
     * operatorControl() methods will be called.
     */
    public void robotMain() {
        m_robotMainOverridden = false;
    }

    /**
     * Start a competition.
     * This code tracks the order of the field starting to ensure that everything happens
     * in the right order. Repeatedly run the correct method, either Autonomous or OperatorControl
     * when the robot is enabled. After running the correct method, wait for some state to change,
     * either the other mode starts or the robot is disabled. Then go back and wait for the robot
     * to be enabled again.
     */
    public void startCompetition() {
        robotMain();
        if (!m_robotMainOverridden) {
            // first and one-time initialization
            robotInit();

            while (true) {
                if (isDisabled()) {
                    disabled();
                    while (isDisabled()) {
                        Timer.delay(0.01);
                    }
                } else if (isAutonomous()) {
                    autonomous();
                    while (isAutonomous() && !isDisabled()) {
                        Timer.delay(0.01);
                    }
                } else {
                    operatorControl();
                    while (isOperatorControl() && !isDisabled()) {
                        Timer.delay(0.01);
                    }
                }
            } /* while loop */
        }
    }
}
