/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import xbot.test.common.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.CommonTools;
import xbot.common.logging.ForceFlushLogWorker;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.common.logging.LogProducer.LogMode;
import xbot.test.common.StringLogWriter;

/**
 *
 * @author Alex
 */
public class LoggerTest extends BaseTest {
    
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    public LoggerTest() {
    }
    
    @Before
    public void setUp() {
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void loggerTest() throws InterruptedException {
        String message = "Test log message";
        logProducer.Log(LogProducer.ERROR, message);
        logConsumer.Consume();
        
        assertTrue("something was written", !writer.log.isEmpty());
        assertTrue("Contains message", writer.log.contains(message));
        assertTrue("Contains ERROR", writer.log.contains("ERROR"));
        
        mockTime.incrementTime(200);
        
        String message2 = "Another message but later";
        logProducer.Log(LogProducer.INFO, message2);
        logConsumer.Consume();
        assertTrue("Contains message", writer.log.contains(message2));
        assertTrue("Contains Info", writer.log.contains("Info"));        
    }
    
    @Test
    public void HiddenLevels() throws InterruptedException {
        CommonTools.loggingLevel = LogProducer.WARNING;
        
        logConsumer.Consume();
        writer.log = "";
        
        String message = "Should see this RROR message";
        logProducer.Log(LogProducer.ERROR, message);
        logConsumer.Consume();
        
        assertTrue("something was written", !writer.log.isEmpty());
        assertTrue("Contains message", writer.log.contains(message));
        assertTrue("Contains ERROR", writer.log.contains("ERROR"));
        
        mockTime.incrementTime(200);
        
        String message2 = "Should not see this NFO message";
        logProducer.Log(LogProducer.INFO, message2);
        logConsumer.Consume();
        assertFalse("Contains message", writer.log.contains(message2));
        assertFalse("Contains Info", writer.log.contains("Info"));  
    }
    
    @Test
    public void manyMessages() throws InterruptedException {
        String message = "This message is going to repeat a bajillion times";
        logProducer.Log(LogProducer.INFO, message);
        logConsumer.Consume();
        
        // This is no longer a valid assumption - there are things that log at initialization
        // time.
        ///assertTrue("nothing written to permanent store", writer.storageLog.isEmpty());
        
        for (int i = 0; i < 2000; i++)
        {
            logProducer.Log(LogProducer.INFO, message);
            if (i % 10 == 0)
            {
                logConsumer.Consume();
            }
        }
        
        logConsumer.Consume();
        
        assertTrue("nothing written to permanent store", !writer.storageLog.isEmpty());
    }
    
    @Test
    public void ForceFlush() throws InterruptedException{
        writer.storageLog = "";
        logProducer.Log(LogProducer.INFO, "This message is going to be forced to permanent storage.");
        
        logConsumer.Consume();
        logConsumer.ForceFlushToStorage();
        
        assertTrue("Permanent store not written to!", !writer.storageLog.isEmpty());
    }
}