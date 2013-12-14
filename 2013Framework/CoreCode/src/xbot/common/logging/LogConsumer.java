/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.common.CommonTools;



/**
 *
 * @author John
 */
public class LogConsumer {
    

    
    private int maxLogLinesToRead = 20;
    private StringBuffer storageLogBuffer;
    private double lastLogWriteMarker;
    private LogWriter writer;
    
    public LogConsumer(LogWriter writer) {
    	this.writer = writer;
        storageLogBuffer = new StringBuffer();
        lastLogWriteMarker = 0;
    }
    
        /**
     * This method consumes a fixed amount of log lines before returning control.
     * The number can be configured in LogConsumer.java.
     */
    public void Consume()
    {
        int i = 0;
        while (CommonTools.Get().GetLoggingQueue().Size() > 0)
        {
            i++;
            if (i > maxLogLinesToRead)
            {
                break;
            }
            
            String logLine = (String)CommonTools.Get().GetLoggingQueue().Dequeue();
            Write(logLine);
        }
    }
    
    
    private void Write(String msg)
    {
        writer.writeDebugLine(msg);
        cacheForStorage(msg);
    }
    
    private void cacheForStorage(String entry)
    {
        //append the entry to the cache
        storageLogBuffer.append(entry).append(System.getProperty("line.separator"));
        
        //once our cache approaches 8192 characters, we want to write it.
        boolean cacheLargeEnough = (storageLogBuffer.length() > 7950);
        //alternatively, if we have pending logs to write and it's been a while since
        //we were last called, we should write them anyways.
        boolean cacheTimeExpired = ((CommonTools.Get().time.GetElapsedMSecFromMarker(lastLogWriteMarker) > 60*1000) &&
                                    (storageLogBuffer.length() > 0));
        
        if (cacheLargeEnough || cacheTimeExpired)
        {
            FlushCache();
        }
        
        //since the cache method was called, we update the last access time
        lastLogWriteMarker = CommonTools.Get().time.GetMarker();
    }
    
    private void FlushCache()
    {
        //write to permanent storage
        writer.writeToStorage(storageLogBuffer.toString());
        //clear the buffer to be used in the future
        storageLogBuffer.delete(0, storageLogBuffer.length());
    }
}
