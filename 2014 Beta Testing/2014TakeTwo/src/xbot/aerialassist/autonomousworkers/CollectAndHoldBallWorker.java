/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployPIDWorker;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.*;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class CollectAndHoldBallWorker extends AerialStateMachineWorker {

    private final DoubleProperty collectBallWaitTime =
            new DoubleProperty("CollectBallWaitTime", 500);
    
    public CollectAndHoldBallWorker(IAutonomousWorkerFactory factory, Collector collector) {
        super("HoldAndCollectBallWorker");
        
        // State 0:
        // Bring the arm down.
        int armDown = this.stateMachine.addWorker(factory.createDeployWorker(
                collector,
                CollectorDeployState.DOWN));
        
        // State 1:
        // Wait for a fixed timeout, to make sure we got the ball.
        // In the future, we should consider using sensors instead.
        int wait = this.stateMachine.addWorker(new WaitWorker(collectBallWaitTime));
        
        // State 2:
        // Bring the arm up to 'safe'.
        /*int armUp = this.stateMachine.addWorker(factory.createDeployWorker(
                collector,
                CollectorDeployState.SAFE));
        */
        // Can we replace state 2 with an immediate state 2?
        int armUp = this.stateMachine.addWorker(new ImmediateWorker(
            new CollectorDeployPIDWorker(
                collector, 
                CollectorDeployState.SAFE)));

        this.stateMachine.onStart(armDown);
        this.stateMachine.onSuccess(armDown, wait);
        this.stateMachine.onSuccess(wait, armUp);
        this.stateMachine.onSuccess(armUp, StateMachineWorker.SUCCESS);
    }
    
}
