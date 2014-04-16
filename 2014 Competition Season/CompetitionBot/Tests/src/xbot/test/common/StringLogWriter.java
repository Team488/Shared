/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.logging.LogWriter;

/**
 *
 * @author Alex
 */
public class StringLogWriter extends LogWriter {
    public String log = "";
    public String storageLog = "";
    
    public StringLogWriter(String name)
    {
        super(name);
    }
    
    @Override
    public void writeDebugLine(String entry) {
        super.writeDebugLine(entry);
        log+= entry + "\n";
    }
    
    @Override
    public void writeToStorage(String multiLineEntry)
    {
        storageLog += multiLineEntry;
    }
}
