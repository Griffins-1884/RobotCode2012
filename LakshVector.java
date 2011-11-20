/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;

/**
 *
 * @author bhasinl
 */
public class LakshVector {

    private double x;
    private double y;

    public LakshVector(double xComponent, double yComponent)
    {
        x = xComponent;
        y = yComponent;
    }


    // Adds this vector to the new vector v and returns the sum
    public LakshVector add(LakshVector v)
    {
        return new LakshVector(x() + v.x(), y() + v.y());
    }


    // Make the vector a unit vector
    public LakshVector normalize()
    {
        double mag = Math.sqrt(x()*x() + y()*y());

        return new LakshVector(x()/mag, y()/mag);
    }


    // Rotate vector by angle theta in degrees COUNTERCLOCKWISE
    public LakshVector rotate(double degreesTheta)
    {
        double radiansTheta = degreesTheta*Math.PI/180.;

        // Use a rotation matrix
        double xComponent = x()*Math.cos(radiansTheta) - y()*Math.sin(radiansTheta);
        double yComponent = x()*Math.sin(radiansTheta) + y()*Math.cos(radiansTheta);

        return new LakshVector(xComponent, yComponent);
    }


    // Returns a direction angle
    public double getDirectionAngle()
    {
        return MathUtils.atan2(y(), x());
    }

    public double x()
    {
        return x;
    }

    public void setX(double newX)
    {
        x = newX;
    }

    public double y()
    {
        return y;
    }

    public void setY(double newY)
    {
        y = newY;
    }

}
