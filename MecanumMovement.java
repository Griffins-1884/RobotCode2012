package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
    
public abstract class MecanumMovement implements Action {

    private double encoderTarget;
    private double initialEncoder;
    private Encoder encoder; // encoder to measure distances

    
    public MecanumMovement(double dist, Encoder enc)
    {
        setEncoder(enc);
        setInitialEncoder(getEncoder().get());
        setEncoderTarget(getEncoder().get() + convertDistanceToEncoder(dist));
    }


    public void setEncoder(Encoder newEncoder)
    {
        encoder = newEncoder;
    }

    public Encoder getEncoder()
    {
        return encoder;
    }

    public void setInitialEncoder(double initialEncoderValue)
    {
        initialEncoder = initialEncoderValue;
    }

    public double getInitialEncoder()
    {
        return initialEncoder;
    }

    public void setEncoderTarget(double newEncoderTarget)
    {
        encoderTarget = newEncoderTarget;
    }

    public double getEncoderTarget()
    {
        return encoderTarget;
    }

    // This method is called until isDone returns true
    // Varies based on the mecanum movement chosen
    public abstract void act();

    // Returns whether we've reached the end of our movement
    public boolean isDone()
    {
        if(getEncoderTarget() - getInitialEncoder() > 0)
        {
            return getEncoder().get() >= getEncoderTarget();
        }
        else // dist is nonzero so change in encoder is negative
        {
            return getEncoder().get() <= getEncoderTarget();
        }
    }

    // Returns encoder value change for a given distance in inches
    // Varies based on strafe, forward movement, etc
    public abstract double convertDistanceToEncoder(double inches);

}
