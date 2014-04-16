/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.Test;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.CollectAndFireBallFromSideWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.test.common.BaseTest;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndFireBallFromSideWorkerTest extends BaseTest {
    @Test
    public void testSuccess() {
        Collector front = this.context.getCollectionCore().getFrontCollector();
        Collector back = this.context.getCollectionCore().getBackCollector();
        MockWorker mock = new MockWorker();
        mock.setExecState(ExecState.SUCCESS);
        MockAutonomousWorkerFactory factory = new MockAutonomousWorkerFactory(mock);
        
        CollectAndFireBallFromSideWorker worker = new CollectAndFireBallFromSideWorker(factory, front, back);
        worker.init();
        Helpers.execAndAssert(worker, 1);
        Helpers.execAndAssert(worker, 1);
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
