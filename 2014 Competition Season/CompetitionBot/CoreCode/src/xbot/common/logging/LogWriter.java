/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

/**
 * Anything that creates CommonTools will need to provide a LogWriter that extends this class.
 * This allows us to not worry about implementation in the core code, while still exposing useful methods.
 * @author Alex
 */
public class LogWriter {
    
    /**
     *
     */
    protected String logName;
    
    /**
     * 
     * @param name
     */
    public LogWriter(String name)
    {
        this.logName = name;
    }
    
    /**
     * Write a line to some form of debug console. Typically small messages.
     * @param entry
     */
    public void writeDebugLine(String entry) {
        System.out.println(entry);
    }
    
    /**
     * Write a string to permanent storage. Typically a large string with line separators.
     * @param multiLineEntry
     */
    public void writeToStorage(String multiLineEntry)
    {
        //Only things that write to permanent storage overload this method.
    }
}
