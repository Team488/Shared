/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

/**
 * Knows how to format strings and such.
 * This class exists because Java ME does not have printf or sprintf,
 * or the Formatter class! Argh.
 * 
 * @author sterlingdorminey
 */
public class Formatter {
    /**
     * Formats a double as a string, and cuts off the fractional part of
     * the number to be at most precision digits.
     * @param value
     * @param precision
     * @return 
     */
    public static String format(double value, int precision) throws IllegalArgumentException {
        // Validation
        if (precision < 1) {
            throw new IllegalArgumentException("precision must be at least 1");
        }
        
        String unformatted = Double.toString(value);
        
        // There are three parts to an unformatted string:
        // Example:
        // 
        // 3.1415926535E-7
        // ^- integer part.
        //   ^- fractional part.
        //             ^- exponent part (optional.).
        
        int integerEnd = unformatted.indexOf('.');
        
        if (integerEnd < 0) {
            // There is nothing to trim.
            return unformatted;
        }
        
        int fractionalEnd = unformatted.indexOf('E');
        
        boolean hasExponent = true;
        
        if (fractionalEnd < 0) {
            hasExponent = false;
            fractionalEnd = unformatted.length();
        }
        
        String integerSegment = unformatted.substring(0, integerEnd);
        
        String fractionalSegment = unformatted.substring(integerEnd + 1, fractionalEnd);
        
        // Trim
        if (precision > fractionalSegment.length()) {
            precision = fractionalSegment.length();
        }
        fractionalSegment = fractionalSegment.substring(0, precision);
        
        String exponentSegment = "";
        
        if (hasExponent) {
            exponentSegment = unformatted.substring(fractionalEnd + 1, unformatted.length());
        }
        
        String formatted = integerSegment + "." + fractionalSegment;

        if (hasExponent) {
            formatted = formatted + "E" + exponentSegment;
        }
        
        return formatted;
    }
}
