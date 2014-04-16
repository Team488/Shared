/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.threeballauto.CollectAndFireBallFromSideWhileMovingWorker;
import xbot.aerialassist.collection.Collector;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.test.common.BaseTest;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndFireBallFromSideWhileMovingWorkerTest extends BaseTest {
    @Test
    public void testWorker() {
        Collector main = RobotContext.Get().getCollectionCore().getFrontCollector();
        Collector side = RobotContext.Get().getCollectionCore().getBackCollector();
        MockWorker mock = new MockWorker();
        mock.setExecState(ExecState.SUCCESS);
        MockAutonomousWorkerFactory factory = new MockAutonomousWorkerFactory(mock);
        CollectAndFireBallFromSideWhileMovingWorker worker = new CollectAndFireBallFromSideWhileMovingWorker(factory, main, side);
        
        worker.init();
        
        // Execute the "prepare and collect" stage.
        Helpers.execAndAssert(worker, 1);
        // We know that the "consume and fire" step has two states.
        // Therefore, call exec() twice.
        Helpers.execAndAssert(worker, 1);
        Helpers.execAndAssert(worker, 1);
        // But we can't make forward progress until we move.
        Helpers.execAndAssert(worker, 1);
        // Which we will be done with after 1500ms.
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
