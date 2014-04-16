/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import xbot.aerialassist.autonomousworkers.twoballauto.LoadBallWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class LoadBallWorkerTest extends BaseTest {
    @Test
    public void testLoadBall() {
        Collector front = this.context.getCollectionCore().getFrontCollector();
        Collector back = this.context.getCollectionCore().getBackCollector();
        
        // Initial robot state:
        // - Facing 180 degrees.
        // - Both collectors are up.
        // - Catapult is cocked.
        // - Ball in its belly.
        LoadBallWorker worker = new LoadBallWorker();
        Helpers.setCollectorState(front, CollectorDeployState.UP);
        Helpers.setCollectorState(front, CollectorDeployState.UP);
        worker.init();
        
        // 1. Bring both arms to down.
        Helpers.execAndAssert(worker, 0);
        Helpers.setCollectorState(front, CollectorDeployState.DOWN);
        Helpers.execAndAssert(worker, 0);
        Helpers.setCollectorState(back, CollectorDeployState.DOWN);
        Helpers.execAndAssert(worker, 1);
        // 2. Load a ball.
        Helpers.execAndAssert(worker, 1);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 2);
        // 3. Bring both arms to safe.
        Helpers.execAndAssert(worker, 2);
        Helpers.setCollectorState(front, CollectorDeployState.SAFE);
        Helpers.execAndAssert(worker, 2);
        Helpers.setCollectorState(back, CollectorDeployState.SAFE);
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
