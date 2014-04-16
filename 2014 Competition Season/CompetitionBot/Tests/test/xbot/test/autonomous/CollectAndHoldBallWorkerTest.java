/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.After;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.CollectAndHoldBallWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.CommonTools;
import xbot.common.StateMachineWorker;
import xbot.test.common.*;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndHoldBallWorkerTest extends BaseTest {
    
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
    }
    /*
    @Test
    public void testSuccess() {
        Collector collector = this.context.getCollectionCore().getFrontCollector();
        AutonomousWorkerFactory factory = new AutonomousWorkerFactory();
        CollectAndHoldBallWorker machine =
                new CollectAndHoldBallWorker(factory, collector);
        
        machine.init();
        Helpers.execAndAssert(machine, 0);
        Helpers.setCollectorState(collector, CollectorDeployState.DOWN);
        Helpers.execAndAssert(machine, 1);
        // Need to wait until the time passes.
        Helpers.execAndAssert(machine, 1);
        mockTime.incrementTime(1000);
        // Now will wait until we go to SAFE.
        Helpers.setCollectorState(collector, CollectorDeployState.UP);
        Helpers.execAndAssert(machine, 2);
        Helpers.execAndAssert(machine, 2);
        Helpers.setCollectorState(collector, CollectorDeployState.SAFE);
        Helpers.execAndAssert(machine, StateMachineWorker.SUCCESS);
    }*/
    
    @Test
    public void testSuccessWithImmediateEnding()
    {
        Collector collector = this.context.getCollectionCore().getFrontCollector();
        AutonomousWorkerFactory factory = new AutonomousWorkerFactory();
        CollectAndHoldBallWorker machine =
                new CollectAndHoldBallWorker(factory, collector);
        
        machine.init();
        Helpers.execAndAssert(machine, 0);
        Helpers.setCollectorState(collector, CollectorDeployState.DOWN);
        Helpers.execAndAssert(machine, 1);
        // Need to wait until the time passes.
        Helpers.execAndAssert(machine, 1);
        mockTime.incrementTime(1000);
        // Now will wait until we go to SAFE.
        Helpers.setCollectorState(collector, CollectorDeployState.UP);
        Helpers.execAndAssert(machine, 2);
        Helpers.execAndAssert(machine, StateMachineWorker.SUCCESS);
    }
}
