/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.common.CommonTools;
import xbot.common.FauxEnum;

/**
 * This logProducer class formats messages based on typical message types (Info, Error, Warning, etc...)
 * and puts them into the robot's log queues. These messages are later read by a LogConsumer and persisted.
 * @author John
 */
public class LogProducer {
    
    /**
     *
     */
    public static class Level extends FauxEnum {
        /**
         *
         * @param id
         */
        public Level(int id)
        {
            super(id);
        }
    }
    
    /**
     *
     */
    public static class LogMode extends FauxEnum {
        /**
         *
         * @param id
         */
        public LogMode(int id)
        {
            super(id);
        }
    }
    
    /**
     *
     */
    public final static Level
            VERBOSE = new Level(0),
            /**
             *
             */
            INFO = new Level(1),
            /**
             *
             */
            IMPORTANT = new Level(2),
            /**
             *
             */
            WARNING = new Level(3),
            /**
             *
             */
            ERROR = new Level(4),
            /**
             *
             */
            DATA = new Level(5);
    
    /**
     *
     */
    public final static LogMode
            LOGGING = new LogMode(0),
            /**
             *
             */
            TELEMETRY = new LogMode(1);
    
    private String nameOfCaller;
    private LogMode mode;
    
    /**
     *
     * @param nameOfCaller
     * @param mode
     */
    public LogProducer(String nameOfCaller, LogMode mode)
    {
        this.nameOfCaller = nameOfCaller;
        this.mode = mode;
    }
    
    /**
     *  Log a message
     * @param level What level the message is
     * @param message   The message to log.
     */
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
        EnqueueData(sbuffer.toString());
    }
    
    private void EnqueueData(String data)
    {
        if (mode == LOGGING)
        {
            CommonTools.Get().GetLoggingQueue().Enqueue(data);
        }
        if (mode == TELEMETRY)
        {
            CommonTools.Get().GetTelemetryQueue().Enqueue(data);
        }
    }
}
