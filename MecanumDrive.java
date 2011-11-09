/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SpeedController;

public class MecanumDrive
{
    private static SpeedController m_frontLeftMotor;
    private static SpeedController m_frontRightMotor;
    private static SpeedController m_rearLeftMotor;
    private static SpeedController m_rearRightMotor;

    public static int iterationCount = 0;

    // i specifies the motor
    // i = 1 is front left
    // i = 2 is front right
    // i = 3 is rear left
    // i = 4 is rear right
    public static void setMotor(int i, SpeedController speedController)
    {
        switch(i)
        {
            case 1:
                m_frontLeftMotor = speedController;
                break;

            case 2:
                m_frontRightMotor = speedController;
                break;

            case 3:
                m_rearLeftMotor = speedController;
                break;

            case 4:
                m_rearRightMotor = speedController;
                break;

            default:
                System.out.println("Wrong input value");
                break;
        }
    }


    public static boolean hasBeenInitialized()
    {
        return m_frontLeftMotor != null && m_frontRightMotor != null && m_rearLeftMotor != null && m_rearRightMotor != null;
    }

    
    public static void drive(double forward, double right, double clockwise)
    {
        if(!hasBeenInitialized())
        {
            System.out.println("Motors not initialized, breaking");
            return;
        }

        double threshold = 1.0;
        double wheelScalar = 0.85;

        double F = (-1)*forward;
        double R = (-1)*right;
        double CL = (-1)*clockwise;

        double FL = F + CL + R;
        double FR = F - CL - R;
        double BL = F + CL - R;
        double BR = F - CL + R;

        FL = (-1.0) * FL * wheelScalar;
        FR = FR * wheelScalar;
        BL = (-1.0) * BL * wheelScalar;
        BR = BR * wheelScalar;

        double wheelMax = Math.max(Math.max(Math.abs(FL), Math.abs(FR)), Math.max(Math.abs(BL), Math.abs(BR)));
        if (wheelMax >= threshold) {
            FL = FL * threshold / wheelMax;
            FR = FR * threshold / wheelMax;
            BL = BL * threshold / wheelMax;
            BR = BR * threshold / wheelMax;
        }

        // System.out.println("FL: " + FL + "\nFR: " + FR + "\nBL: " + BL + "\nBR: " + BR);


        m_frontLeftMotor.set(FL);
        m_frontRightMotor.set(FR);
        m_rearLeftMotor.set(BL);
        m_rearRightMotor.set(BR);

    }
}