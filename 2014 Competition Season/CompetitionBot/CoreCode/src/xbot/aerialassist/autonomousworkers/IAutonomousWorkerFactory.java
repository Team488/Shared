/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.common.WorkerBase;

/**
 * Contains factory methods used by the AutonomousWorker to
 * construct its state machine.
 * 
 * There are two implementations of this class:
 * - AutonomousWorkerFactory, which creates real workers.
 * - MockAutonomousWorkerFactory, which returns the same mock worker
 *   over and over.
 * @author sterlingdorminey
 */
public interface IAutonomousWorkerFactory {
    WorkerBase createCollectAndFireBallFromSideWorker(Collector main, Collector side);
    
    WorkerBase createCollectFromSideWorker(Collector main, Collector side);
    
    WorkerBase createFireCatapultWorker();
    
    WorkerBase createDeployBothWorker(CollectorDeployState main, CollectorDeployState side);

    WorkerBase createCockCatapultWorker();
    
    WorkerBase createCockCatapultEnoughWorker();
    
    WorkerBase createRollerWorker(Collector collector, CollectorRollerState rollerState);
    
    WorkerBase createDeployWorker(Collector collector, CollectorDeployState deployState);

    WorkerBase createWaitForBallToSettleAndFireWorker(Collector collector);
    
    WorkerBase createPrepareToCollectBallWorker(Collector main, Collector side);

    WorkerBase createConsumeBallAndFinishCockingWorker(Collector main);
    
    WorkerBase createWaitForBallToSettleWorker();
}
