/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.math;

import xbot.common.CommonTools;
import xbot.common.CommonUtils;

/**
 * The XYPair is a class to hold two values representing an X and Y point. It is 
 * used to either store absolute positions (such as location on the field) or vectors
 * where X and Y represent vector components.
 * @author John
 */
public class XYPair {
    /**
     * X component of the pair.
     */
    public double X;
    /**
     * Y component of the pair.
     */
    public double Y;
    
    /**
     *
     */
    public XYPair()
    {
        X = 0;
        Y = 0;
    }
    
    /**
     * 
     * @param x
     * @param y
     */
    public XYPair(double x, double y)
    {
        this.X = x;
        this.Y = y;
    }
    
    /**
     * Constructs an XYPair given a magnitude and degrees.
     * @param magnitude
     * @param degrees
     * @return 
     */
    public static XYPair fromPolarCoordinates(double magnitude, double degrees) {
        XYPair pair = new XYPair(magnitude, 0);
        return pair.rotate(degrees);
    }
    /**
     * Add one XYPair to another
     * @param other
     * @return The sum of the two
     */
    public XYPair add(XYPair other) {
        XYPair result = new XYPair();
        result.X = this.X + other.X;
        result.Y = this.Y + other.Y;
        return result;
    }
    
    /**
     * Add the X & Y components together.
     * @return the sum of X and Y
     */
    public double internalSum()
    {
        return X + Y;
    }
    
    public double internalAbsoluteSum()
    {
        return Math.abs(X) + Math.abs(Y);
    }
    
    /**
     * Multiply X and Y by a scalar (number) value
     * @param scalar number to multiply by
     * @return the scaled pair
     */
    public XYPair multiplyScalar(double scalar) {
        XYPair result = new XYPair();
        result.X = this.X * scalar;
        result.Y = this.Y * scalar;
        return result;
    }
    
    /**
     * Assuming the XYPair represents a vector, rotate it 
     * @param degrees how many degrees to rotate the vector by.
     * @return the rotated vector.
     */
    public XYPair rotate(double degrees)
    {
        return CommonUtils.rotateVector(X, Y, degrees);
    }
    
    public double toRadians()
    {
        return CommonTools.Get().trigUtil.atan2(Y, X);
    }
    
    public double toDegrees()
    {
        double potentialDegrees =  toRadians() * 180 / Math.PI;
        
        if (potentialDegrees < 0)
        {
            potentialDegrees += 360;
        }
        
        return potentialDegrees;
    }
    
    /**
     * Returns projection OF the other vector ONTO this vector..
     * @param other
     * @return 
     */
    public double project(XYPair other) {
        double dotProduct = (this.X * other.X) + (this.Y * other.Y);
        return dotProduct / this.magnitude();
    }
    
    /**
     * Pop pop!
     * @return 
     */
    public double magnitude() {
        return Math.sqrt((this.X * this.X) + (this.Y * this.Y));
    }
    
    /**
     * Transform this into an array of form double[2], where [0] is X and [1] is Y
     * @return
     */
    public double[] toArray() {
        return new double[] {this.X, this.Y};
    }
    
    public boolean equals(XYPair other, double tolerance) {
        return Math.abs(this.X - other.X) < tolerance && Math.abs(this.Y - other.Y) < tolerance;
    }
    
    public String toString() {
        return "(" + this.X + ", " + this.Y + ")";
    }
}
