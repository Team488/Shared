/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.math;

/**
 * A small class to do some basic angular math.
 * @author John
 */
public class Angles {
    
    /**
     * This method assumes degrees are positive, but I think it works with negative as well!
     * @param actual The actual measurement
     * @param desired Where you want to be
     * @return The difference between desired and actual. In the basic case, if desired is greater
     * than actual, the output will be positive.
     */
    public static double DifferenceBetweenDegrees(double actual, double desired)
    {
        // 0 - first, reduce to below 360
        double firstMod = actual % 360;
        double secondMod = desired % 360;
        
        // Take the first minus the second.
        double attemptOne = actual - desired;
        
        //Then, find the smaller number, add 360 to it, and repeat the operation.
        if (actual < desired)
        {
            actual += 360;
        }
        else
        {
            desired += 360;
        }
        
        double attemptTwo = actual - desired;
        
        //Find the smallest magnitude change
        if (Math.abs(attemptOne) < Math.abs(attemptTwo))
        {
            return -attemptOne;
        }
        else
        {
            return -attemptTwo;
        }
    }
}
