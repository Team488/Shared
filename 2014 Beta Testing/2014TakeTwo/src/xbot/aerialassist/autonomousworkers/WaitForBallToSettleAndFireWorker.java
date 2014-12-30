/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WaitWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class WaitForBallToSettleAndFireWorker extends AerialStateMachineWorker {
    public WaitForBallToSettleAndFireWorker(
            IAutonomousWorkerFactory factory,
            Collector main) {
        super("ConsumeBallAndFireWorker");
                
        // Step 0:
        // Stop the roller on the main arm.
        int stopRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(main, CollectorRollerState.STOP));
        
        // Step 1:
        // Wait for the ball to settle.
        // TODO: We should think about introducing a mandatory max timeout.
        int waitForSettle = this.stateMachine.addWorker(
                factory.createWaitForBallToSettleWorker());

        DoubleProperty waitAgainProperty = 
                new DoubleProperty("waitForBallToSettleAfterArmsOpen", 750);
        
        int waitForSettleAgain = this.stateMachine.addWorker(
                new WaitWorker("waitForBallToSettleAfterArmsOpen", waitAgainProperty));
        
        // Step 2:
        // Lower both arms to the firing position.
        int lowerArms = this.stateMachine.addWorker(
                factory.createDeployBothWorker(
                        CollectorDeployState.FIRING,
                        CollectorDeployState.FIRING));
        
        // Step 3:
        // Fire ze ball!
        int fireBall = this.stateMachine.addWorker(
                factory.createFireCatapultWorker());
        
        this.stateMachine.onStart(stopRoller);
        this.stateMachine.onSuccess(stopRoller, waitForSettle);
        this.stateMachine.onSuccess(waitForSettle, lowerArms);
        this.stateMachine.onSuccess(lowerArms, waitForSettleAgain);
        this.stateMachine.onSuccess(waitForSettleAgain, fireBall);
        this.stateMachine.onSuccess(fireBall, StateMachineWorker.SUCCESS);
    }
}
