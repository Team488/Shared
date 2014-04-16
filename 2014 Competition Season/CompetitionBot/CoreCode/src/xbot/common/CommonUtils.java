/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.math.XYPair;
import java.util.Vector;

/**
 * These are a bunch of useful little utilities for things not available in Java ME.
 * @author John
 */
public class CommonUtils {
    
    private CommonUtils()
    {
    }
    
    /**
     * Coerces a double within a range. Useful for safety calculations.
     * For example, if you have the value 6, and max of 4 and min of -4, the function will return 4.
     * @param variable  variable to coerce
     * @param max   max allowed value
     * @param min   min allowed value
     * @return  coerced value.
     */
    public static double CoerceDouble(double variable, double max, double min)
    {        
        if (variable > max)
        {
            variable = max;
        }
        if (variable < min)
        {
            variable = min;
        }
        
        return variable;
    }
    
    public static boolean InRange(double variable, double max, double min)
    {
        if ((variable <= max) && (variable >= min))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Parses a String into a boolean
     * @param string of the form "true" or anything else. Anything that isn't "true" will read as false.
     * @return
     */
    public static boolean parseBoolean(String string)
    {
        if (string.equals("true")) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Parses a String into a Boolean.
     * @param string of the form "true" or "false".
     * @return
     */
    public static Boolean parseObjectBoolean(String string)
    {
        if (string.equals("true")) {
            return Boolean.valueOf(true);
        }
        else if (string.equals("false")) {
            return Boolean.valueOf(false);
        }
        else {
            return null;
        }
    }
    
    /**
     * Transforms a boolean into a String.
     * @param value value to transform
     * @return "true" or "false"
     */
    public static String booleanToString(boolean value){
        if (value){
            return "true";
        }
        else {
            return "false";
        }
    }
    
    /**
     * Gets the system's line separator. This could be different on different systems.
     * (For now, it's always "\n".
     * @return
     */
    public static String GetLineSeperator()
    {
        return "\n";
    }
    
    /**
     * Rotates a vector by a givenangle.
     * @param x X component of the vector
     * @param y Y component of the vector
     * @param angle angle (in degrees) to rotate the vector by
     * @return the rotated vector as a XYPair.
     */
    public static XYPair rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        XYPair out = new XYPair();
        out.X = x * cosA - y * sinA;
        out.Y = x * sinA + y * cosA;
        return out;
    }
    
    /**
     * Returns a generic property delimeter. For now it's just ",".
     * @return
     */
    public static String GetPropertyDelimiter()
    {
        return ",";
    }
    
    /**
     * Splits a String. Code copied from somewhere online. 
     * @param splitStr
     * @param delimiter
     * @return
     */
    public static String[] Split(String splitStr, String delimiter) {
        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        // split
        char[] chars = splitStr.toCharArray();
        for (int i=0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        String[] splitArray = new String[tokens.size()];
        for (int i=0; i < splitArray.length; i++) {
            splitArray[i] = (String)tokens.elementAt(i);
        }
        return splitArray;
    }
}
