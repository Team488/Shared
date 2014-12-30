/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.autonomousworkers.threeballauto.ConsumeAndFireWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;

/**
 *
 * @author John
 */
public class CollectAndLoadBallFromSideWorker  extends AerialStateMachineWorker {

    public CollectAndLoadBallFromSideWorker(IAutonomousWorkerFactory factory, Collector main, Collector side) {
        super("CollectAndLoadBallFromSideWorker");
        
        // Step 0:
        // Prepare robot for firing, start cocking
        // and collect ball from the main side.
        int collectFromMain = this.stateMachine.addWorker(
                factory.createPrepareToCollectBallWorker(main, side));
        
        // Step 1:
        // Consume the ball and finish cocking
        int consumeAndFinishCocking = this.stateMachine.addWorker(
                factory.createConsumeBallAndFinishCockingWorker(main));
        
        this.stateMachine.onStart(collectFromMain);
        this.stateMachine.onSuccess(collectFromMain, consumeAndFinishCocking);
        this.stateMachine.onSuccess(consumeAndFinishCocking, StateMachineWorker.SUCCESS);
    }
    
}
