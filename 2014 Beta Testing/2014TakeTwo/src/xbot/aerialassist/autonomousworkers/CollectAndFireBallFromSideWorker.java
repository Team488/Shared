/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.autonomousworkers.threeballauto.ConsumeAndFireWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndFireBallFromSideWorker extends AerialStateMachineWorker {

    public CollectAndFireBallFromSideWorker(IAutonomousWorkerFactory factory, Collector main, Collector side) {
        super("CollectAndFireBallFromSideWorker");
        
        // Step 0:
        // Prepare robot for firing, start cocking
        // and collect ball from the main side.
        int collectFromMain = this.stateMachine.addWorker(
                factory.createPrepareToCollectBallWorker(main, side));
        
        // Step 1:
        // Consume the ball and finish cocking.
        int consumeAndFire = this.stateMachine.addWorker(
                new ConsumeAndFireWorker(factory, main));
        
        this.stateMachine.onStart(collectFromMain);
        this.stateMachine.onSuccess(collectFromMain, consumeAndFire);
        this.stateMachine.onSuccess(consumeAndFire, StateMachineWorker.SUCCESS);
    }
    
}
