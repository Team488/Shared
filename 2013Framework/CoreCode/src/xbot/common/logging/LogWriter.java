/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author Alex
 */
public class LogWriter {
    public void writeDebugLine(String entry) {
        System.out.println(entry);
    }
    
    public void writeToStorage(String multiLineEntry)
    {
        //Only things that write to permanent storage overload this method.
    }
}
