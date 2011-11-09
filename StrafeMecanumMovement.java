package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class StrafeMecanumMovement extends MecanumMovement {

    // Construct a strafe movement action with encoder and
    // distance to travel (in inches)
    // pre: enc is not null and dist is nonzero
    public StrafeMecanumMovement(double dist, Encoder enc)
    {
        super(dist, enc);
    }

    // Strafes for a certain distance
    // This method is called until isDone returns true
    public void act()
    {
        // This assumes mecanum drive is used
        if(MecanumDrive.hasBeenInitialized())
        {
            // Move right
            if(getEncoderTarget() > getInitialEncoder())
            {
                MecanumDrive.drive(0, 1, 0);
            }
            else if(getEncoderTarget() < getInitialEncoder())
            {
                // Move left
                MecanumDrive.drive(0, -1, 0);
            }

        }
    }

    // Returns encoder value change for a given distance in inches
    // Positive encoder change means right
    // The calibration values will be different compared to those for moving forward in mecanum!!!!
    public double convertDistanceToEncoder(double inches)
    {
        // For experimental data:
        double encoderChange = 1.0; // change these variables
        double inchesTravelled = 1.0; // these will be different from the forward
        // movement values since they deal with encoder changes when strafing

        // In the above case, we state that +1.0 inch travelled corresponds
        // To an encoder change of +1.0. Change this with real data

        double inchesToEncoder = encoderChange/inchesTravelled;

        return inches * inchesToEncoder;
    }

}