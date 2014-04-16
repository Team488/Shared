/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.threeballauto;

import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;

/**
 *
 * @author sterlingdorminey
 */
public class ConsumeAndFireWorker extends AerialStateMachineWorker {
    
    public ConsumeAndFireWorker(IAutonomousWorkerFactory factory, Collector main) {
        super("ConsumeAndFireWorker");
        
        // Step 0:
        // Consume the ball and finish cocking, while we are moving forward.
        int consumeAndFinishCocking = this.stateMachine.addWorker(
                factory.createConsumeBallAndFinishCockingWorker(main));

        // Step 1:
        // Wait for the ball to settle, bring the arms down and fire.
        int waitAndFire = this.stateMachine.addWorker(
                factory.createWaitForBallToSettleAndFireWorker(main));
    
        this.stateMachine.onStart(consumeAndFinishCocking);
        this.stateMachine.onSuccess(consumeAndFinishCocking, waitAndFire);
        this.stateMachine.onSuccess(waitAndFire, StateMachineWorker.SUCCESS);
    }
}
