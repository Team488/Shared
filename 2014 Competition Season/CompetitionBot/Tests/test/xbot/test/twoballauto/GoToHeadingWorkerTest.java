/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import xbot.aerialassist.autonomousworkers.twoballauto.GoToHeadingWorker;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class GoToHeadingWorkerTest extends BaseTest {
    @Test
    public void testWorker() {
        DoubleProperty testHeading = new DoubleProperty("Foo", 270);
        GoToHeadingWorker worker = new GoToHeadingWorker(testHeading);
        
        Helpers.setRobotHeading(180);
        worker.init();
        
        Helpers.execAndAssert(worker, ExecState.NOT_DONE);
        Helpers.setRobotHeading(testHeading.get());
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
    }
}
