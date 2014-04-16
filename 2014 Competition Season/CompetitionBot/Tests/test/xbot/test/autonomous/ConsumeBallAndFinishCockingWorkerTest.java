/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.ConsumeBallAndFinishCockingWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.ExecState;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class ConsumeBallAndFinishCockingWorkerTest extends BaseTest {
    @Test
    public void testSuccess() {
        Collector front = this.context.getCollectionCore().getFrontCollector();
        AutonomousWorkerFactory factory = new AutonomousWorkerFactory();
        ConsumeBallAndFinishCockingWorker worker =
                new ConsumeBallAndFinishCockingWorker(factory, front);
        
        Helpers.setCollectorState(front, CollectorDeployState.SAFE);
        Helpers.setCatapultCockedEnough();
        
        worker.init();
        // Worker needs to wait for the catapult to be cocked,
        // and for the arm to be raised.
        worker.exec();
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        Helpers.setCatapultCocked();
        worker.exec();
        // Still need to wait for the arm to be raised.
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        worker.exec();
        Helpers.setCollectorState(front, CollectorDeployState.UP);
        worker.exec();
        assertEquals(ExecState.SUCCESS, worker.isFinished());
    }
}
