/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import xbot.common.logging.*;
import xbot.test.common.*;
import org.junit.*;
import static org.junit.Assert.*;
import xbot.aerialassist.autonomousworkers.CollectFromSideWorker;
import xbot.aerialassist.collection.*;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.properties.ITableProxy;

/**
 * Tests the collect from side state machine worker.
 * We just use the front collector, since the worker is general.
 * 
 * @author sterlingdorminey
 */
@Ignore
public class CollectFromSideWorkerTest extends AutonomousTest {

    private final Collector front;
    
    private final Collector back;
    
    private final CollectFromSideWorker worker;
    
    public CollectFromSideWorkerTest()
    {
        this.front = context.getCollectionCore().getFrontCollector();
        this.back = context.getCollectionCore().getBackCollector();
        this.worker = new CollectFromSideWorker(this.front, this.back);
    }
  
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
    }

    @Test
    public void testSuccessPath() throws InterruptedException {
        
        Helpers.setCollectorState(this.front, CollectorDeployState.FIRING);
        Helpers.setCollectorState(this.back, CollectorDeployState.FIRING);

        this.worker.init();
        this.changeTimeouts();
        
        // Step 0: Start the rollers
        this.execAndAssert(1);
        
        // Step 1: Bring front down, back up.
        this.execAndAssert(1);
        Helpers.setCollectorState(this.front, CollectorDeployState.DOWN);
        this.execAndAssert(1);
        Helpers.setCollectorState(this.back, CollectorDeployState.UP);
        this.execAndAssert(2);
        
        // Step 2: wait to collect the ball.
        this.execAndAssert(2);
        mockTime.incrementTime(50);
        this.execAndAssert(3);
        
        // Step 3: Bring front up.
        this.execAndAssert(3);
        Helpers.setCollectorState(this.front, CollectorDeployState.UP);
        this.execAndAssert(4);
        
        // Step 4: Wait for ball to settle.
        this.execAndAssert(4);
        mockTime.incrementTime(50);
        this.execAndAssert(5);
        
        // Step 5: Firing.
        this.execAndAssert(5);
        Helpers.setCollectorState(this.front, CollectorDeployState.FIRING);
        Helpers.setCollectorState(this.back, CollectorDeployState.FIRING);
        this.execAndAssert(6);
        
        // Step 6: Stop roller.
        this.worker.exec();
        assertTrue(this.worker.isFinished() == ExecState.SUCCESS);
    }

    private void execAndAssert(int state) {
        this.worker.exec();
        assertEquals(state, this.worker.getCurrentState());
    }
    
    private void changeTimeouts() {
        ITableProxy properties = CommonTools.Get().propertyManager.randomAccessStore;
        properties.setDouble("BallCollectTime", 25);
        properties.setDouble("BallSettleTime", 25);
    }
    
}
