/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import java.io.*;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import xbot.common.CommonTools;
import xbot.common.logging.*;

/**
 *
 * @author Alex
 */
public class RobotLogWriter extends LogWriter {
    private final static String PATHPREFIX = "/XbotData/Logs/";
    private final static String PATHSUFFIX = ".txt";
    
    public RobotLogWriter(String name)
    {
        super(name);
    }
    
    private String getNewLogPath()
    {
        String timeStamp = CommonTools.Get().time.getRealTime();
        timeStamp = timeStamp.trim();
        
        String fullFileName = PATHPREFIX + this.logName + " " + timeStamp + PATHSUFFIX;
        return fullFileName;
    }
    
    public void writeToStorage(String entry)
    {
        String fileName = getNewLogPath();
        
        // Create the folder for the log path, in case it doesn't exist :).
        File file = new File(fileName);
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
        }
        
        super.writeDebugLine("Writing to Robot Storage");
        
        try 
        {
            OutputStream os = new FileOutputStream(fileName);
            super.writeDebugLine("Output stream created.");
            
            os.write(entry.getBytes());
            super.writeDebugLine("Entry written to disk.");
            os.flush();
            os.close();
            super.writeDebugLine("Output stream closed.");
        }
        catch (EOFException e) {
          super.writeDebugLine("End of file reached: " + e.getMessage());
        } 
        catch (IOException e) { // catch all IOExceptions not handled by previous catch blocks
          super.writeDebugLine("General I/O exception: " + e.getMessage());
          e.printStackTrace();
        }
    }
}
