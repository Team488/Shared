/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.logging.LoggingQueue;
import xbot.common.logging.LogWriter;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;

/**
 *
 * @author Alex
 */
public class CommonTools {
    public Time time;
    public LogConsumer logConsumer;
    public PropertyManager propertyManager;
    private static CommonTools instance;
    public static LogProducer.Level loggingLevel;
    
    private CommonTools(Time time, LogWriter writer, 
            PermanentStorageProxy permStore, ITableProxy randomStore) {
        this.propertyManager = new PropertyManager(permStore, randomStore);
        this.logConsumer = new LogConsumer(writer);
        this.time = new Time();
        
        InitializeCommonTools();
    }
    
    private void InitializeCommonTools()
    {
        lowTasks = new LowPriorityTasks();
        logQueue = new LoggingQueue();
        loggingLevel = LogProducer.INFO;
    }
    
    public static void CreateCommonTools(Time time, LogWriter writer, 
            PermanentStorageProxy permStore, ITableProxy randomStore)
    {
        instance = new CommonTools(time, writer, permStore, randomStore);
        
        // This needs to happen early, and this is probably the best place for it.
        CommonTools.Get().propertyManager.LoadPropertiesFromStorage();
    }
    
    public static CommonTools Get()
    {
        if (instance == null)
        {
            // freak out
            throw new IllegalStateException("Need to initialize CommonTools before calling on it");
        }
        else
        {
            return instance;
        }
    }
    
    private LowPriorityTasks lowTasks;
    public LowPriorityTasks GetLowPriorityTasks()
    {
        return lowTasks;
    }
    
    private LoggingQueue logQueue;
    public LoggingQueue GetLoggingQueue()
    {
        return logQueue;
    }
}
