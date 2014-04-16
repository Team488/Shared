/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.threeballauto;

import xbot.aerialassist.autonomousworkers.AutoDriveForwardWorker;
import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WhenAllWorker;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndFireBallFromSideWhileMovingWorker extends AerialStateMachineWorker {

    public CollectAndFireBallFromSideWhileMovingWorker(IAutonomousWorkerFactory factory, Collector main, Collector side) {
        super("CollectAndFireBallFromSideWhileMovingWorker");
        
        // Step 0:
        // Prepare robot for firing, start cocking
        // and collect ball from the main side.
        int collectFromMain = this.stateMachine.addWorker(
                factory.createPrepareToCollectBallWorker(main, side));
        
        // Step 1: 
        // Consume the ball and finish cocking, while we are moving forward.
        WhenAllWorker consumeAndFireWhileMovingWorker =
                new WhenAllWorker();
        consumeAndFireWhileMovingWorker.addWorker(
                new ConsumeAndFireWorker(factory, main));
        consumeAndFireWhileMovingWorker.addWorker(
                new AutoDriveForwardWorker());
        
        int consumeAndFireWhileMoving = this.stateMachine.addWorker(
                consumeAndFireWhileMovingWorker);
        
        this.stateMachine.onStart(collectFromMain);
        this.stateMachine.onSuccess(collectFromMain, consumeAndFireWhileMoving);
        this.stateMachine.onSuccess(consumeAndFireWhileMoving, StateMachineWorker.SUCCESS);

    }
    
}
