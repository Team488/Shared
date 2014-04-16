/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import xbot.aerialassist.autonomousworkers.twoballauto.TwoBallAutonomousWorker;
import xbot.common.ExecState;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class TwoBallAutoWorkerTest extends BaseTest {
    @Test
    public void testHot() {
        MockWorker mock = new MockWorker();
        MockTwoBallAutoWorkerFactory factory = new MockTwoBallAutoWorkerFactory(mock);
        TwoBallAutonomousWorker worker = new TwoBallAutonomousWorker(factory);
        
        worker.init();
        mock.setExecState(ExecState.SUCCESS);
        
        // Execute the hot program.
        Helpers.execAndAssert(worker, 1);
        // Drive forward.
        Helpers.execAndAssert(worker, 3);
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
    }
    
    @Test
    public void testCold() {
        MockWorker mock = new MockWorker();
        MockTwoBallAutoWorkerFactory factory = new MockTwoBallAutoWorkerFactory(mock);
        TwoBallAutonomousWorker worker = new TwoBallAutonomousWorker(factory);
        
        worker.init();
        // Make the state machine choose the "cold" path.
        mock.setExecState(ExecState.FAILURE);
        
        // Execute the cold program.
        Helpers.execAndAssert(worker, 2);
        mock.setExecState(ExecState.SUCCESS);
        // Drive forward.
        Helpers.execAndAssert(worker, 3);
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
    }
}
