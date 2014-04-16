/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.common.WorkerBase;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class MockAutonomousWorkerFactory implements IAutonomousWorkerFactory {
    public MockWorker worker;
    
    public MockAutonomousWorkerFactory(MockWorker worker) {
        this.worker = worker;
    }
    
    @Override
    public WorkerBase createCollectFromSideWorker(Collector main, Collector side) {
        return this.worker; 
    }

    @Override
    public WorkerBase createFireCatapultWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createDeployBothWorker(CollectorDeployState main, CollectorDeployState side) {
        return this.worker;
    }

    @Override
    public WorkerBase createCockCatapultWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createRollerWorker(Collector collector, CollectorRollerState rollerState) {
        return this.worker;
    }

    @Override
    public WorkerBase createDeployWorker(Collector collector, CollectorDeployState deployState) {
        return this.worker;
    }

    @Override
    public WorkerBase createPrepareToCollectBallWorker(Collector main, Collector side) {
        return this.worker;
    }
    
    @Override
    public WorkerBase createWaitForBallToSettleWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createCockCatapultEnoughWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createWaitForBallToSettleAndFireWorker(Collector collector) {
        return this.worker;
    }

    @Override
    public WorkerBase createConsumeBallAndFinishCockingWorker(Collector main) {
        return this.worker;
    }

    @Override
    public WorkerBase createCollectAndFireBallFromSideWorker(Collector main, Collector side) {
        return this.worker;
    }
}
