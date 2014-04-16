/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.After;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.ConsumeBallAndFinishCockingWorker;
import xbot.aerialassist.autonomousworkers.WaitForBallToSettleAndFireWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.CommonTools;
import xbot.common.StateMachineWorker;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class WaitForBallToSettleAndFireWorkerTest extends BaseTest {
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
        CommonTools.Get().logConsumer.Consume();
        CommonTools.Get().logConsumer.Consume();
    }
    
    @Test
    public void testConsumeBallAndFireWorker() {
        // Start the test by setting the main arm "UP", and the other arm in "Safe."
        Collector main = this.context.getCollectionCore().getFrontCollector();
        Collector side = this.context.getCollectionCore().getBackCollector();
        
        Helpers.setCollectorState(main, CollectorDeployState.SAFE);
        Helpers.setCollectorState(side, CollectorDeployState.UP);
        
        AutonomousWorkerFactory factory = new AutonomousWorkerFactory();
        
        WaitForBallToSettleAndFireWorker worker =
                new WaitForBallToSettleAndFireWorker(factory, main);
        
        worker.init();
        // Stop the roller.
        Helpers.execAndAssert(worker, 1);
        Helpers.execAndAssert(worker, 1);
        // Wait for the ball to settle.
        Helpers.setBallSettled();
        Helpers.execAndAssert(worker, 1);
        // Ball is settled, but doesn't have a history of being settled.
        this.mockTime.incrementTime(2000);
        // Now it's legit settled.
        Helpers.execAndAssert(worker, 2);
        Helpers.execAndAssert(worker, 2);
        // Bring the arms down.
        Helpers.setCollectorState(main, CollectorDeployState.FIRING);
        Helpers.setCollectorState(side, CollectorDeployState.FIRING);
        Helpers.execAndAssert(worker, 3);
        Helpers.execAndAssert(worker, 3);
        // Wait for us to pass the firing timeout.
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
