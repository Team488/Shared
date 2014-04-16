/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.common.CommonTools;



/**
 * The LogConsumer is responsible for buffering logs in memory and only writing them to disc when certain criteria are met:
 * -No new logs appear for a minute
 * -The amount of logs in memory reaches a specific threshold (right now that's 31k)
 * -An external caller demands that the current cache is flushed to storage
 * The logConsumer lives in the LowPriorityTasks, and is constantly pulling log statements off of the logging queue
 * and putting them into its own cache.
 * @author John
 */
public class LogConsumer {
    
    private int maxLogLinesToRead = 20;
    private StringBuffer storageLogBuffer;
    private double lastLogWriteMarker;
    private LogWriter writer;
    private int flushSizeLimit = 31000;
    private double cacheExpiryTime = 60*1000;
    private LogProducer.LogMode mode;
    
    /**
     *
     * @param writer
     * @param mode
     */
    public LogConsumer(LogWriter writer, LogProducer.LogMode mode) {
    	this.writer = writer;
        storageLogBuffer = new StringBuffer();
        lastLogWriteMarker = -1;
        this.mode = mode;
    }
    
        /**
     * This method consumes a fixed amount of log lines before returning control.
     * The number can be configured in LogConsumer.java.
     */
    public void Consume()
    {
        LoggingQueue queue = new LoggingQueue();
        if (mode == LogProducer.LOGGING)
        {
            queue = CommonTools.Get().GetLoggingQueue();
        }
        if (mode == LogProducer.TELEMETRY)
        {
            queue = CommonTools.Get().GetTelemetryQueue();
        }
        
        int i = 0;
        while (queue.Size() > 0)
        {
            i++;
            if (i > maxLogLinesToRead)
            {
                break;
            }
            
            String logLine = (String)queue.Dequeue();
            Write(logLine);
        }
    }
    
    
    private void Write(String msg)
    {
        if (mode == LogProducer.LOGGING)
        {
            writer.writeDebugLine(msg);
        }
        cacheForStorage(msg);
    }
    
    private void cacheForStorage(String entry)
    {
        if (lastLogWriteMarker == -1)
        {
            lastLogWriteMarker = CommonTools.Get().time.GetMarker();
        }
        
        //append the entry to the cache
        storageLogBuffer.append(entry).append("\n");
        
        //once our cache approaches 8192 characters, we want to write it.
        boolean cacheLargeEnough = (storageLogBuffer.length() > flushSizeLimit);
        
        // No longer do this, since we're pretty happy with the cache
        ///writer.writeDebugLine("[LogConsumer] Cache is at: " + storageLogBuffer.length());
        
        //alternatively, if we have pending logs to write and it's been a while since
        //we were last called, we should write them anyways.
        boolean cacheTimeExpired = ((CommonTools.Get().time.GetElapsedMSecFromMarker(lastLogWriteMarker) > cacheExpiryTime) &&
                                    (storageLogBuffer.length() > 0));
        if (cacheTimeExpired)
        {
            writer.writeDebugLine("[LogConsumer] Cache time expired! Flushing logs.");
        }
        
        if (cacheLargeEnough || cacheTimeExpired)
        {
            FlushCache();
        }
        
        //since the cache method was called, we update the last access time
        lastLogWriteMarker = CommonTools.Get().time.GetMarker();
    }
    
    /**
     * Immediately flush all logs to storage.
     */
    public void ForceFlushToStorage()
    {
        writer.writeDebugLine("[LogConsumer] We are being forced to flush to storage by external caller.");
        if (storageLogBuffer.length() > 0)
        {
            FlushCache();
        }
    }
    
    private void FlushCache()
    {
        //write to permanent storage
        writer.writeToStorage(storageLogBuffer.toString());
        //clear the buffer to be used in the future
        storageLogBuffer.delete(0, storageLogBuffer.length());
    }
}
