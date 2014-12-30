/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.properties.PropertyManager;
import xbot.common.properties.ITableProxy;
import xbot.common.properties.PermanentStorageProxy;
import xbot.common.logging.LoggingQueue;
import xbot.common.logging.LogWriter;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;

/**
 * This represents a group of tools that are used by Systems and Workers. This class
 * must be instantiated before anything else is going to work.
 * @author Alex
 */
public class CommonTools {
    /**
     * The universal time system.
     */
    public Time time;
    /**
     * The object responsible for caching and flushing logs to storage.
     */
    public LogConsumer logConsumer;
    /**
     * The object responsible for caching and flushing telemetry to storage.
     */
    public LogConsumer telemetryConsumer;
    /**
     * The PropertyManager keeps track of all the "configuration" on the robot by reading/writing to 
     * temporary and permanent storages.
     */
    public PropertyManager propertyManager;
    private static CommonTools instance;
    /**
     * This is the default logging level. Logs below this level will not be saved. Default is typically Info.
     */
    public static LogProducer.Level loggingLevel;
    /**
     * The trigonometry utility. Since certain math functions are only available on the robot, we put this layer of abstraction here.
     * Tests will use their own from java SE, and the Robot will use squawk.math.something.
     */
    
    private CommonTools(Time time, LogWriter logWriter, LogWriter telemetryWriter,
            PermanentStorageProxy permStore, ITableProxy randomStore) {
        this.propertyManager = new PropertyManager(permStore, randomStore);
        this.logConsumer = new LogConsumer(logWriter, LogProducer.LOGGING);
        this.telemetryConsumer = new LogConsumer(telemetryWriter, LogProducer.TELEMETRY);
        this.time = time;
        InitializeCommonTools();
    }
    
    private void InitializeCommonTools()
    {
        lowTasks = new LowPriorityTasks();
        logQueue = new LoggingQueue();
        telemetryQueue = new LoggingQueue();
        loggingLevel = LogProducer.VERBOSE;
    }
    
    /**
     * Creates the common tools. This must be done first, before anything else.
     * @param time  The time system to use
     * @param logWriter The logWriter to use
     * @param telemetryWriter   The TelemetryWriter to use.
     * @param permStore the PermanentStore to use for properties
     * @param randomStore   the RandomStore to use for properties
     */
    public static void CreateCommonTools(Time time, LogWriter logWriter, 
            LogWriter telemetryWriter, PermanentStorageProxy permStore, 
            ITableProxy randomStore)
    {
        instance = new CommonTools(time, logWriter, telemetryWriter, permStore, randomStore);
        
        // This needs to happen early, and this is probably the best place for it.
        CommonTools.Get().propertyManager.LoadPropertiesFromStorage();
        CommonTools.Get().time.initialize();
    }
    
    /**
     * Gets the singleton instance of the CommonTools. Will only work if CreateCommonTools has already been called.
     * @return
     */
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
    /**
     * These Low Priority Tasks must be fed periodically by the robot. It takes care of a lot of "background" tasks
     * like logging.
     * @return
     */
    public LowPriorityTasks GetLowPriorityTasks()
    {
        return lowTasks;
    }
    
    private LoggingQueue logQueue;
    /**
     * Gets the single queue for Logging.
     * @return
     */
    public LoggingQueue GetLoggingQueue()
    {
        return logQueue;
    }
    
    private LoggingQueue telemetryQueue;
    /**
     * Gets the single queue for Telemetry.
     * @return
     */
    public LoggingQueue GetTelemetryQueue()
    {
        return telemetryQueue;
    }
}
