/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Vector;

/**
 *
 * @author John
 */
public class CommonUtils {
    
    private CommonUtils()
    {
    }
    
    public static String GetLineSeperator()
    {
        return System.getProperty("line.separator");
    }
    
    public static String GetPropertyDelimiter()
    {
        return ",";
    }
    
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
