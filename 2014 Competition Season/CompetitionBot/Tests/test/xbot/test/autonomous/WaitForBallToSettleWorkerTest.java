/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.WaitForBallToSettleWorker;
import xbot.common.ExecState;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class WaitForBallToSettleWorkerTest extends BaseTest {
    @Test
    public void testWaitForBallToSettle() {
        Helpers.setBallNotSettled();
        WaitForBallToSettleWorker worker = new WaitForBallToSettleWorker();
        worker.init();
        worker.exec();
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        Helpers.setBallSettled();
        // We set the voltage, but we don't have the history, man.
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        this.mockTime.incrementTime(2000);
        assertEquals(ExecState.SUCCESS, worker.isFinished());
    }
}
