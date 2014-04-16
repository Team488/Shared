/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.*;
import xbot.test.common.BaseTest;
import xbot.aerialassist.autonomousworkers.*;
import xbot.common.ExecState;

/**
 *
 * @author sterlingdorminey
 */
public class CockCatapultEnoughWorkerTest extends BaseTest {
    @Test
    public void testSuccess() {
        CockCatapultEnoughWorker worker = new CockCatapultEnoughWorker();
        
        Helpers.setCatapultNotCocked();
        
        worker.init();
        Helpers.execAndAssert(worker, ExecState.NOT_DONE);
        Helpers.setCatapultCockedEnough();
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
    }
}
