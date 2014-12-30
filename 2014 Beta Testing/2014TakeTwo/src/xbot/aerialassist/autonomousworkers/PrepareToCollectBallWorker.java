/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.common.*;

/**
 *
 * @author sterlingdorminey
 */
public class PrepareToCollectBallWorker extends WhenAllWorker {
        
    public PrepareToCollectBallWorker(
            IAutonomousWorkerFactory workerFactory,
            Collector main,
            Collector side) {
        super("PrepareToCollectBallWorker");
        
        // Simultaneously:
        // 0) Cock the catapult enough - we'll finish it in the ConsumeBallAndFinishCockingWorker.
        this.addWorker(workerFactory.createCockCatapultEnoughWorker());
        // 1) Grab the ball and hold it ready to put it into the catapult.
        this.addWorker(new CollectAndHoldBallWorker(workerFactory, main));
        // 2) Start the roller.
        this.addWorker(
                workerFactory.createRollerWorker(
                        main,
                        CollectorRollerState.COLLECT));
        // 3) Bring the side collector up.
        this.addWorker(workerFactory.createDeployWorker(side, CollectorDeployState.UP));
    }
    
}
