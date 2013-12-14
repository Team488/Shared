/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;
import xbot.common.logging.LogProducer;

/**
 *
 * @author John
 */
public class Loggable {
    
    private LogProducer logProducer;
    
    public Loggable(String name)
    {
        logProducer = new LogProducer(name);
    }
    
    protected void Log(LogProducer.Level level, String message)
    {
        logProducer.Log(level, message);
    }
    
    protected void Verbose(String message)
    {
        Log(LogProducer.VERBOSE, message);
    }
    
    protected void Info(String message)
    {
         Log(LogProducer.INFO, message);
    }
    
    protected void Important(String message)
    {
         Log(LogProducer.IMPORTANT, message);
    }
    
    protected void Warning(String message)
    {
        Log(LogProducer.WARNING, message);
    }
    
    protected void Error(String message)
    {
         Log(LogProducer.ERROR, message);
    }
}
