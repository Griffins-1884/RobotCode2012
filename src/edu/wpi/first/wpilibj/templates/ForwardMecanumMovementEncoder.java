/**
 *
 * Deals with forward/backward movement for mecanum drive
 *
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class ForwardMecanumMovementEncoder extends MecanumMovement {

    // Construct a forward movement action with encoder and 
    // distance to travel (in inches)
    // pre: enc is not null and dist is nonzero
    public ForwardMecanumMovementEncoder(double dist, Encoder enc)
    {
        super(dist, enc);
    }

    // Moves forward for a certain distance
    // This method is called until isDone returns true
    public void act()
    {
        // This assumes mecanum drive is used
        if(MecanumDrive.hasBeenInitialized())
        {
            if(getEncoderTarget() > getInitialEncoder())
            {
                // Move forwards
                MecanumDrive.drive(1, 0, 0);
            }
            else if(getEncoderTarget() < getInitialEncoder())
            {
                // Move backwards
                MecanumDrive.drive(-1, 0, 0);
            }

        }
    }


    // Returns encoder value change for a given distance in inches
    // Positive encoder change means forward
    public double convertDistanceToEncoder(double inches)
    {
        // For experimental data:
        double encoderChange = 1.0; // change these variables
        double inchesTravelled = 1.0;

        // In the above case, we state that +1.0 inch travelled corresponds
        // To an encoder change of +1.0. Change this with real data

        double inchesToEncoder = encoderChange/inchesTravelled;

        return inches * inchesToEncoder;
    }

}