/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;
import xbot.common.logging.LogProducer;
import xbot.common.properties.BooleanProperty;

/**
 * Most objects in CoreCode inherit from this object. It allows for a number of convenience methods for logging,
 * such as Info(String Message).
 * @author John
 */
public class Loggable {
    private String name;
    
    private LogProducer logProducer;
    private BooleanProperty doVerboseLogging;
    
    private double loggingPain;
    private double lastTimeCalled;
    private final double painTimeFactor = 5; //Every second, decrease pain by 5
    private final double painThreshold = 25;
    private boolean inBadState;
    
    /**
     * Each object that extends Loggable must provide a name. That way, when logging, the message will
     * automatically be formatted with something like "[SystemName]: blah blah blah"
     * @param name
     */
    public Loggable(String name)
    {
        InitLoggable(name);
    }
    
    private void InitLoggable(String name)
    {
        inBadState = false;
        lastTimeCalled = -1000;
        loggingPain = 0;
        this.name = name;
        logProducer = new LogProducer(name, LogProducer.LOGGING);
    }
    
    /**
     * Systems that want to have verbose logging need to explicitly hand off a BooleanProperty to toggle
     * it on and off. Verbose logging is so detrimental to the performance of the robot that we want to make it
     * fairly hard to run by accident.
     * @param name
     * @param verboseLoggingProperty
     */
    public Loggable(String name, BooleanProperty verboseLoggingProperty)
    {
        InitLoggable(name);
        doVerboseLogging = verboseLoggingProperty;
    }
    
    /**
     * Gives back the name of the system.
     * @return
     */
    public String getName()
    {
        return this.name;
    }
    
    private boolean ShouldSendMessage()
    {
        loggingPain++; //each message adds one unit of pain
        
        double deltaTime = CommonTools.Get().time.GetElapsedMSecFromMarker(lastTimeCalled);
        double painDecayFromTime = (deltaTime / 1000) * painTimeFactor;
        
        loggingPain -= painDecayFromTime;
        
        if (loggingPain < 0)
        {
            loggingPain = 0;
        }
        
        lastTimeCalled = CommonTools.Get().time.GetMarker();
        
        if (IsExceedingMessagingThreshold())
        {
            return false;
        }
        else
        {
            inBadState = false; //we're back to normal operation
            return true;    
        }
    }
    
    /**
     * All other convenience methods use this in some way.
     * @param level
     * @param message
     */
    protected void Log(LogProducer.Level level, String message)
    {
        logProducer.Log(level, message);
    }
    
    /**
     * Logs a verbose message. Used for really noisy stuff, such as motor updates every tick.
     * @param message
     */
    protected void Verbose(String message)
    {
        if (doVerboseLogging != null)
        {
            if (doVerboseLogging.get() == true)
            {
                Log(LogProducer.VERBOSE, message);
            }
        }        
    }
    
    /**
     * Logs an info message. Used for things human readers would want to know about, like state changes,
     * long-running sensors toggling, etc.
     * @param message
     */
    protected void Info(String message)
    {
        if (ShouldSendMessage())
        {
            Log(LogProducer.INFO, message);
        }
    }
    
    /**
     * Logs an important message. This is for stuff you think a human reader should really notice.
     * @param message
     */
    protected void Important(String message)
    {
        if (ShouldSendMessage())
        {
            Log(LogProducer.IMPORTANT, message);
        }
    }
    
    /**
     * Logs a warning message. This is for when something has gone wrong, but you can recover or workaround
     * the problem and keep operating (maybe in a limited mode). However, you want to warn so that the problem can be fixed.
     * @param message
     */
    protected void Warning(String message)
    {
        if (ShouldSendMessage())
        {
            Log(LogProducer.WARNING, message);
        }
    }
    
    /**
     * Logs an error message. This is for when something has gone wrong, and there is no way to workaround.
     * There aren't many things that should be considered errors.
     * @param message
     */
    protected void Error(String message)
    {
        if (ShouldSendMessage())
        {
            Log(LogProducer.ERROR, message);
        }
    }

    private boolean IsExceedingMessagingThreshold() {
        if (loggingPain > painThreshold) {
            if (inBadState == false)
            {
                inBadState = true;
                Warning("This object is sending too many messages!");
            }
            return true;
        }
        return false;
    }
}
