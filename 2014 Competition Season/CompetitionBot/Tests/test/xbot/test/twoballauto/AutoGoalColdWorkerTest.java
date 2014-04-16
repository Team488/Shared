/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.twoballauto.AutoGoalColdWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.StateMachineWorker;
import xbot.common.properties.DoubleProperty;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class AutoGoalColdWorkerTest extends BaseTest {
    private final DoubleProperty awayHeading =
            new DoubleProperty("AwayHeading", 165);
    
    @Test
    public void testWorker() {
        Collector front = RobotContext.Get().getCollectionCore().getFrontCollector();
        Collector back = RobotContext.Get().getCollectionCore().getBackCollector();
        
        AutoGoalColdWorker worker = new AutoGoalColdWorker();
        
        // Test parameters:
        // - Catapult cocked.
        // - Arms are in safe, one ball is held in safe position.
        // - Robot is facing the cold goal.
        // - Robot orientation is 180 degrees.
        Helpers.setCollectorState(front, CollectorDeployState.SAFE);
        Helpers.setCollectorState(back, CollectorDeployState.SAFE);
        worker.init();        
        
        // 0. Turn away. Shun the cold goal! Shunnn
        Helpers.execAndAssert(worker, 0);
        Helpers.setRobotHeading(this.awayHeading.get());
        Helpers.execAndAssert(worker, 1);
        // 1. Fire!
        Helpers.execAndAssert(worker, 1);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 2);
        // 2. Simultaneously:
        //    a. Turn back
        //    b. Consume ball.
        //    c. Wait for ball to settle.
        // Haven't turned yet.
        Helpers.execAndAssert(worker, 2);
        Helpers.setRobotHeading(this.context.GetSensorCore().startingOrientation.get());
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 2);
        // Front arm up, but back arm not and not settled.
        Helpers.setCollectorState(front, CollectorDeployState.UP);
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 2);
        // Both arms up, but haven't had time to settle yet.
        Helpers.setCollectorState(back, CollectorDeployState.UP);
        Helpers.execAndAssert(worker, 2);
        Helpers.setBallSettled();
        Helpers.execAndAssert(worker, 2);
        // Time to settle.
        this.mockTime.incrementTime(2000);
        Helpers.execAndAssert(worker, 3);
        // 3. Drop arms and fire!
        Helpers.execAndAssert(worker, 3);
        Helpers.setCollectorState(front, CollectorDeployState.FIRING);
        Helpers.execAndAssert(worker, 3);
        Helpers.setCollectorState(back, CollectorDeployState.FIRING);
        Helpers.execAndAssert(worker, 3);
        this.mockTime.incrementTime(2000);
        // 4. The end, we scored a ton of points! Good job.
        Helpers.execAndAssert(worker, StateMachineWorker.SUCCESS);
    }
}
