/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;
import static org.junit.Assert.*;

/**
 *
 * @author John
 */
public class TelemetryTest extends BaseTest {
    LogConsumer telemetryConsumer;
    LogProducer telemetryProducer;
    StringLogWriter telemetryWriter;
    
    public TelemetryTest() {
    }
    
    @Before
    public void setUp() {
        telemetryWriter = new StringLogWriter("Telemetry");
        telemetryConsumer = new LogConsumer(telemetryWriter, LogProducer.TELEMETRY);
        telemetryProducer = new LogProducer("test", LogProducer.TELEMETRY);
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
    public void basicTelemetry() throws InterruptedException {
        
        RobotContext.Get().GetTelemetryCore().ReportTelemetry();
        RobotContext.Get().GetTelemetryCore().ReportTelemetry();
        telemetryConsumer.Consume();
        telemetryConsumer.ForceFlushToStorage();
        
        assertTrue("Telemetry log should contain Gyro", telemetryWriter.storageLog.contains("Gyro"));
        assertTrue("Telemetry log should contain basic gyro data", telemetryWriter.storageLog.contains("180"));
    }
}
