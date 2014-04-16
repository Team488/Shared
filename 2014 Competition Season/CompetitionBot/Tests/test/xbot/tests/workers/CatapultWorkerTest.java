/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.systems.CatapultCore;
import xbot.aerialassist.workers.CockCatapultWorker;
import xbot.aerialassist.workers.FireCatapultWorker;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;

/**
 *
 * @author Ken
 */
public class CatapultWorkerTest extends BaseTest{
    
    public CatapultWorkerTest()
    {  
        
    }    
        
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    @Before
    public void setUp()
    {
        
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
    }
 
    @Test
    public void CatapultTest() throws InterruptedException
    {
        FireCatapultWorker catapult = new FireCatapultWorker();
        
        assertEquals("Catapult Worker motor stopped", 0, context.GetCatapultCore().getCatapultMotor() , .001);
        
        catapult.init();
        catapult.exec();
        
        double fireSpeed = context.GetCatapultCore().catapultFireSpeed.get();
        assertEquals("Catapult motor running", fireSpeed, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
        
        mockTime.incrementTime(4000);
        
        catapult.exec();
        assertEquals("Catapult motor is stopped", 0, context.GetCatapultCore().getCatapultMotor(), .001);
        catapult.isFinished();
        logConsumer.Consume();
        logConsumer.Consume();
     
    }
    
    @Test
    public void CockedCatapultTest() throws InterruptedException
    {
        // set catapult to be not cocked
        context.GetCatapultCore().setCatapultScaledPositionTESTONLY(1.0);
        
        CockCatapultWorker cockCatapult = new CockCatapultWorker();
        
        
    
        cockCatapult.init();
        context.GetCatapultCore().shouldCockCatapult.set(true);
        cockCatapult.exec();
        logConsumer.Consume();
        assertEquals("Catapult Worker motor being cocked", 0.65, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
        //assertEquals(string name, expected, actual, delta);
        
     
        //set condition to true. Expecting motor to stop
        context.GetCatapultCore().setCatapultScaledPositionTESTONLY(
                context.GetCatapultCore().getCatapultCockedPosition());
        cockCatapult.init();
        cockCatapult.exec();
        assertEquals("Catapult worker finished cocking", 0, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
    }
    
     @Test
    public void disableAutoCockCatapultTest() throws InterruptedException
    {
        CockCatapultWorker cockCatapult = new CockCatapultWorker();
        
        
    
        cockCatapult.init();
        cockCatapult.exec();
        logConsumer.Consume();
        context.GetCatapultCore().shouldCockCatapult.set(false);
        assertEquals("Catapult Worker not being cocked", 0, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
        //assertEquals(string name, expected, actual, delta);
        
     
        //set condition to true. Expecting motor to stop
        
        context.GetCatapultCore().setCatapultScaledPositionTESTONLY(
                context.GetCatapultCore().getCatapultCockedPosition());
        cockCatapult.init();
        cockCatapult.exec();
        assertEquals("Catapult worker finished cocking", 0, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
    }
    
    @Test
    public void FireTwiceTest() throws InterruptedException
    {
        FireCatapultWorker catapult = new FireCatapultWorker();
        
        assertEquals("Catapult Worker motor stopped", 0, context.GetCatapultCore().getCatapultMotor() , .001);
        
        catapult.init();
        catapult.exec();
        
        double fireSpeed = context.GetCatapultCore().catapultFireSpeed.get();
        assertEquals("Catapult motor running", fireSpeed, context.GetCatapultCore().getCatapultMotor(), .001);
        logConsumer.Consume();
        
        mockTime.incrementTime(4000);
        
        catapult.exec();
        assertEquals("Catapult motor is stopped", 0, context.GetCatapultCore().getCatapultMotor(), .001);
        
        catapult.init();
        catapult.exec();
        mockTime.incrementTime(500);
        assertEquals("Catapult motor running",fireSpeed,context.GetCatapultCore().getCatapultMotor(),.001);
        logConsumer.Consume();
     
    }
    
}
