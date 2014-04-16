/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.math;

/**
 * A class for very simple math that isn't in Java ME.
 * @author John
 */
public class SimpleMath {
    
    /**
     * Simple exponentiation (since Java ME doesn't have it)
     * @param base number to be exponentiated
     * @param exponent how many times to multiply the number by itself.
     * @return 
     */
    public static double XtotheY(double x, double y)
    {
        double result = x;
        
        for (int i = 1; i < y; i++)
        {
            result *= result;
        }
        
        return result;
    }
}
