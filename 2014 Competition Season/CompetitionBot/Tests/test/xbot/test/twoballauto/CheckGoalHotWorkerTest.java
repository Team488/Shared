/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.twoballauto.CheckGoalHotWorker;
import xbot.common.ExecState;
import xbot.common.properties.BooleanProperty;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class CheckGoalHotWorkerTest extends BaseTest {
    
    private final BooleanProperty isGoalHot = new BooleanProperty("IsGoalHot", true);
    
    @Test
    public void testGoalHot() {
        CheckGoalHotWorker worker = new CheckGoalHotWorker();
                
        //this.isGoalHot.set(true);
        RobotContext.Get().GetVisionCore().SET_DETECT_HOT_TEST_ONLY(true);
        
        worker.init();
        
        // We are done if we see the value, regardless of how time increments.
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
        mockTime.incrementTime(2500);
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
        
        // Even if it becomes not hot later.
        RobotContext.Get().GetVisionCore().SET_DETECT_HOT_TEST_ONLY(false);
        mockTime.incrementTime(50);
        Helpers.execAndAssert(worker, ExecState.SUCCESS);
    }
    
    @Test
    public void testGoalCold() {
        CheckGoalHotWorker worker = new CheckGoalHotWorker();
        
        this.isGoalHot.set(false);
        worker.init();
        
        // Initially, we are not done.
        Helpers.execAndAssert(worker, ExecState.NOT_DONE);
        mockTime.incrementTime(2500);
        Helpers.execAndAssert(worker, ExecState.FAILURE);
        
        this.isGoalHot.set(true);
        mockTime.incrementTime(50);
        Helpers.execAndAssert(worker, ExecState.FAILURE);
    }
}
