/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import xbot.aerialassist.workers.TurnOffCompressorWorker;
import xbot.aerialassist.workers.TurnOnCompressorWorker;
import xbot.common.logging.LogConsumer;
import xbot.common.CommonTools;
import xbot.common.logging.LogProducer;
import xbot.test.common.StringLogWriter;
import xbot.test.common.BaseTest;

/**
 *
 * @author Anthony
 */
public class CompressorWorkerTest extends BaseTest{
    
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    @Before
    public void setUp(){
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
    }
    
    @Test 
    public void compressorWorkerTest() {
        assertEquals("Compressor is off", false, context.GetPneumaticCore().isCompressorOn());
        logConsumer.Consume();
        
        TurnOnCompressorWorker worker = new TurnOnCompressorWorker();
        logConsumer.Consume();
        worker.init();
        
        assertEquals("Compressor is on", true, context.GetPneumaticCore().isCompressorOn());
        logConsumer.Consume();
        
        TurnOffCompressorWorker  otherworker = new TurnOffCompressorWorker();
        otherworker.init();
        logConsumer.Consume();
        assertEquals("Compressor is off", false, context.GetPneumaticCore().isCompressorOn());
        logConsumer.Consume();
    }
}