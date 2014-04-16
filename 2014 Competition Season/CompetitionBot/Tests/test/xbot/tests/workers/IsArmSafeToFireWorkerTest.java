/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.IsArmSafeToFireWorker;
import xbot.common.ExecState;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class IsArmSafeToFireWorkerTest extends BaseTest {
    IsArmSafeToFireWorker worker;
    Collector collector;
    public IsArmSafeToFireWorkerTest() {
        collector = context.getCollectionCore().getFrontCollector();
        
    }
    
    @Before
    public void setUp() {
        collector.setIsManual(false);
        worker = new IsArmSafeToFireWorker(collector);
    }
    
    @Test
    public void successWhenAlreadySafe() {
        collector.getSensors().SetScaledPositionTESTONLY(1.0);
        assertEquals(ExecState.SUCCESS, worker.isFinished());
    }
    
    @Test
    public void successWhenManual() {
        collector.setIsManual(true);
        assertEquals(ExecState.SUCCESS, worker.isFinished());
    }
    
    @Test
    public void failureWhenNotSafe() {
        collector.getSensors().SetScaledPositionTESTONLY(0);
        assertEquals(ExecState.FAILURE, worker.isFinished());
    }
}