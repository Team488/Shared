/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.autonomousworkers.CockCatapultEnoughWorker;
import xbot.aerialassist.autonomousworkers.DeployBothCollectorsWorker;
import xbot.aerialassist.autonomousworkers.WaitForBallToSettleWorker;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.aerialassist.workers.FireCatapultWorker;
import xbot.aerialassist.workers.SetArmsAndFireCatapultWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WhenAllWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class AutoGoalHotWorker extends AerialStateMachineWorker {

    // Heading that the robot should be facing when it turns away.
    private final DoubleProperty awayHeading =
            new DoubleProperty("AwayHeading", 165);
    
    public AutoGoalHotWorker() {
        super("AutoGoalHotWorker");
        
        // 0. Shoot.
        int shoot = this.stateMachine.addWorker(
                new FireCatapultWorker());
        
        WhenAllWorker cockAndTurn = new WhenAllWorker();
        cockAndTurn.addWorker(new CockCatapultEnoughWorker());
        cockAndTurn.addWorker(new GoToHeadingWorker(this.awayHeading));
        
        // 1. Simultaneously: cock the catapult enough, and turn away.
        int cockEnoughAndTurnAway = this.stateMachine.addWorker(
                cockAndTurn);
                
        WhenAllWorker consumeAndSettle = new WhenAllWorker();
        consumeAndSettle.addWorker(
                new BringArmsUpAndStopRollersWorker());
        consumeAndSettle.addWorker(
                new WaitForBallToSettleWorker());
        
        // 2. Simultaneously: consume the ball, and wait for it to settle.
        //    Note: this can be done serially if we find this is useful.
        int consumeAndWaitToSettle = this.stateMachine.addWorker(
                consumeAndSettle);
        
        // 3. Drop arms and fire.
        int dropArmsAndFire = this.stateMachine.addWorker(
                new SetArmsAndFireCatapultWorker());
        
        this.stateMachine.onStart(shoot);
        this.stateMachine.onSuccess(shoot, cockEnoughAndTurnAway);
        this.stateMachine.onSuccess(cockEnoughAndTurnAway, consumeAndWaitToSettle);
        this.stateMachine.onSuccess(consumeAndWaitToSettle, dropArmsAndFire);
        this.stateMachine.onSuccess(dropArmsAndFire, StateMachineWorker.SUCCESS);
    }
    
}
