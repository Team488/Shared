/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import org.junit.Ignore;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.twoballauto.*;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.common.properties.DoubleProperty;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
@Ignore 
public class AutoGoalHotWorkerTest extends BaseTest {
    // AwayHeading is the heading of the robot
    // when it's facing the opposite goal (not the goal in front of it.)
    // In the future, we can make this property a little easier to adjust.
    private final DoubleProperty awayHeading =
            new DoubleProperty("AwayHeading", 165);
    
    @Test
    public void testHot() {
        Collector front = RobotContext.Get().getCollectionCore().getFrontCollector();
        Collector back = RobotContext.Get().getCollectionCore().getBackCollector();
        
        AutoGoalHotWorker worker = new AutoGoalHotWorker();
        
        // Test parameters:
        // - Catapult cocked.
        // - Arms are in safe, one ball is held in safe position.
        // - Robot is facing the hot goal.
        // - Robot orientation is 180 degrees.
        Helpers.setCollectorState(front, CollectorDeployState.SAFE);
        Helpers.setCollectorState(back, CollectorDeployState.SAFE);
        worker.init();
        
        Helpers.execAndAssert(worker, 0);
        // 0. Shoot.
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 1);
        // 1. Cock enough and turn away.
        Helpers.setCatapultCockedEnough();
        Helpers.execAndAssert(worker, 1);
        Helpers.setRobotHeading(awayHeading.get());
        Helpers.execAndAssert(worker, 2);
        // 2. Consume and wait for the ball to settle.
        Helpers.execAndAssert(worker, 2);
        Helpers.setCollectorState(front, CollectorDeployState.UP);
        Helpers.setCollectorState(back, CollectorDeployState.UP);
        Helpers.execAndAssert(worker, 2);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 2);
        Helpers.setBallSettled();
        Helpers.execAndAssert(worker, 2);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 3);
        // 3. Drop arms and fire.
        Helpers.execAndAssert(worker, 3);
        Helpers.setCollectorState(front, CollectorDeployState.FIRING);
        Helpers.setCollectorState(back, CollectorDeployState.FIRING);
        Helpers.execAndAssert(worker, 3);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
