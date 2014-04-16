/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.SafeArmForFiringWorker;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class SafeArmForFiringTest extends BaseTest {
    SafeArmForFiringWorker worker;
    SafeArmForFiringWorker duressWorker;
    Collector collector;
    
    public SafeArmForFiringTest() {
    }
    
    @Before
    public void setUp() {
        collector = new Collector("testCollector");
        worker = new SafeArmForFiringWorker(collector,false);
        duressWorker = new SafeArmForFiringWorker(collector,true);
    }
    
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testManual() {
        collector.setIsManual(true);
        worker.init();
        worker.exec();
        worker.exec();
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());
               
    }
    
    @Test
    public void testAlreadySafe() {
        collector.getSensors().SetScaledPositionTESTONLY(1.0);
        worker.init();
        worker.exec();
        worker.exec();
        assertEquals(ExecState.SUCCESS, worker.isFinished());
    }
    
    @Test
    public void testNotSafe() {
        collector.getSensors().SetScaledPositionTESTONLY(0.0);
        worker.init();
        worker.exec();
        worker.exec();
        worker.exec();
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        collector.getSensors().SetScaledPositionTESTONLY(
                collector.getSensors().firingPositionLowerLimit.get());
        worker.exec();
        assertEquals(ExecState.SUCCESS, worker.isFinished());

    }
    
    @Test
    public void testManualUnderDuress() {
        collector.setIsManual(true);
        duressWorker.init();
        duressWorker.exec();
        duressWorker.exec();
        
        assertEquals(ExecState.SUCCESS, duressWorker.isFinished());
               
    }
    
    @Test
    public void testAlreadySafeUnderDuress() {
        collector.getSensors().SetScaledPositionTESTONLY(1.0);
        duressWorker.init();
        duressWorker.exec();
        duressWorker.exec();
        assertEquals(ExecState.SUCCESS, duressWorker.isFinished());
    }
    
    @Test
    public void testNotSafeUnderDuress() {
        collector.getSensors().SetScaledPositionTESTONLY(0.0);
        duressWorker.init();
        duressWorker.exec();
        duressWorker.exec();
        duressWorker.exec();
        assertEquals(ExecState.NOT_DONE, duressWorker.isFinished());
        collector.getSensors().SetScaledPositionTESTONLY(
                collector.getSensors().duressPositionLowerLimit.get());
        duressWorker.exec();
        assertEquals(ExecState.SUCCESS, duressWorker.isFinished());

    }
    
}