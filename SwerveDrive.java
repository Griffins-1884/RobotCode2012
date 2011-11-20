/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author bhasinl
 */
public class SwerveDrive extends Drive {

    private static SpeedController frontLeftMotor;
    private static SpeedController frontRightMotor;
    private static SpeedController rearLeftMotor;
    private static SpeedController rearRightMotor;

    private static SpeedController anglingLeftSideMotor;
    private static SpeedController anglingRightSideMotor;


    // CHANGE THESE AFTER MEASURING
    public static final double ROBOT_WIDTH = 1.0; // in meters
    public static final double ROBOT_LENGTH = 1.5; // in meters

    public static void setMotor(int i, SpeedController speedController)
    {
        switch(i)
        {
            case 1:
                frontLeftMotor = speedController;
                break;

            case 2:
                frontRightMotor = speedController;
                break;

            case 3:
                rearLeftMotor = speedController;
                break;

            case 4:
                rearRightMotor = speedController;
                break;

            case 5:
                anglingLeftSideMotor = speedController;
                break;

            case 6:
                anglingRightSideMotor = speedController;
                break;

            default:
                System.out.println("Wrong input value");
                break;
        }
    }

    public static boolean hasBeenInitialized()
    {
        return frontLeftMotor != null && frontRightMotor != null &&
                rearLeftMotor != null && rearRightMotor != null &&
                anglingLeftSideMotor != null && anglingRightSideMotor != null;
    }

    public static void drive(double forward, double right, double rotateCCW)
    {
        LakshVector wheel1PosVector = new LakshVector(ROBOT_WIDTH/2., ROBOT_LENGTH/2.);
        LakshVector wheel2PosVector = new LakshVector(-ROBOT_WIDTH/2., ROBOT_LENGTH/2.);
        LakshVector wheel3PosVector = new LakshVector(-ROBOT_WIDTH/2., -ROBOT_LENGTH/2.);
        LakshVector wheel4PosVector = new LakshVector(ROBOT_WIDTH/2., -ROBOT_LENGTH/2.);

        // Rotate position vectors 90 degrees to get normalized rotation vectors
        LakshVector wheel1RotationVector = (wheel1PosVector.rotate(90.0)).normalize();
        LakshVector wheel2RotationVector = (wheel2PosVector.rotate(90.0)).normalize();
        LakshVector wheel3RotationVector = (wheel3PosVector.rotate(90.0)).normalize();
        LakshVector wheel4RotationVector = (wheel4PosVector.rotate(90.0)).normalize();
    }

}
