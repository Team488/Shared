/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import xbot.common.CommonTools;
import xbot.common.logging.*;

/**
 *
 * @author Alex
 */
public class RobotLogWriter extends LogWriter {
    private final static String PATHPREFIX = "file:///";
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
                
        super.writeDebugLine("Writing to Robot Storage");
        
        try 
        {
            CheckForLogFile(fileName);
            
            FileConnection c = (FileConnection) Connector.open(fileName, Connector.READ_WRITE);            
            OutputStream os = c.openOutputStream();
            super.writeDebugLine("Output stream created.");
            
            os.write(entry.getBytes());
            super.writeDebugLine("Entry written to disk.");
            os.flush();
            os.close();
            c.close();
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

    private void CheckForLogFile(String fileName) throws IOException {
                
        FileConnection c = (FileConnection) Connector.open(fileName, Connector.READ_WRITE);
        
        if (!c.exists())
        {
            super.writeDebugLine("No " + fileName + " file detected. Creating one now...");
            //we need to create the file if it doesn't exist already.
            c.create();
            super.writeDebugLine("Log file created.");
            c.close();
        }
    }
}
