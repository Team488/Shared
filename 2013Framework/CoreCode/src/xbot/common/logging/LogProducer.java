/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.common.CommonTools;
import xbot.common.FauxEnum;

/**
 *
 * @author John
 */
public class LogProducer {
    
    public static class Level extends FauxEnum {
        public Level(int id)
        {
            super(id);
        }
    }
    public final static Level 
            VERBOSE = new Level(0),
            INFO = new Level(1),
            IMPORTANT = new Level(2),
            WARNING = new Level(3),
            ERROR = new Level(4);
    
    private String nameOfCaller;
    
    public LogProducer(String nameOfCaller)
    {
        this.nameOfCaller = nameOfCaller;
    }
    
    public void Log(Level level, String message)
    {
        if (level.Value() < CommonTools.loggingLevel.Value())
        {
            return;
        }
        
        // here is where we format the message
        StringBuffer sbuffer = new StringBuffer();
        // Get the current time
        double totalSeconds = 
                CommonTools.Get().time.GetElapsedMSecFromMarker(0) / 1000.0;
        
        sbuffer.append("[").append(totalSeconds).append("]");
        // Add the correct logging level
        if(level == VERBOSE) sbuffer.append("[Verbose] ");
        if(level == INFO) sbuffer.append("[Info] ");
        if(level == IMPORTANT) sbuffer.append("[Important] ");
        if(level == WARNING) sbuffer.append("[Warning] ");
        if(level == ERROR) sbuffer.append("[ERROR] ");
        // Add the name of the caller
        sbuffer.append(nameOfCaller).append(": ");
        
        // Add the actual message
        sbuffer.append(message);
        
        // Put the message onto the "queue"
        CommonTools.Get().GetLoggingQueue().Enqueue(sbuffer.toString());
    }
}
