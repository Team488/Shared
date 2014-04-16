/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.catapult.CatapultPIDThatFinishesWorker;
import xbot.aerialassist.collection.*;
import xbot.aerialassist.workers.CockCatapultWorker;
import xbot.aerialassist.workers.FireCatapultWorker;
import xbot.common.WorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public class AutonomousWorkerFactory implements IAutonomousWorkerFactory {

    public WorkerBase createCollectFromSideWorker(Collector main, Collector side) {
        return new CollectFromSideWorker(main, side);
    }

    public WorkerBase createFireCatapultWorker() {
        return new FireCatapultWorker();
    }

    public WorkerBase createDeployBothWorker(CollectorDeployState main, CollectorDeployState side) {
        return new DeployBothCollectorsWorker(main, side);
    }

    public WorkerBase createCockCatapultWorker() {
        return new CockCatapultWorker();
        //return new CatapultPIDThatFinishesWorker();
    }

    public WorkerBase createRollerWorker(Collector collector, CollectorRollerState rollerState) {
        return new ImmediateRollerWorker(collector, rollerState);
    }

    public WorkerBase createDeployWorker(Collector collector, CollectorDeployState deployState) {
        return new CollectorDeployPIDWorker(collector, deployState);
    }

    public WorkerBase createWaitForBallToSettleAndFireWorker(Collector collector) {
        return new WaitForBallToSettleAndFireWorker(this, collector);
    }

    public WorkerBase createPrepareToCollectBallWorker(Collector main, Collector side) {
        return new PrepareToCollectBallWorker(this, main, side);
    }

    public WorkerBase createWaitForBallToSettleWorker() {
        return new WaitForBallToSettleWorker();
    }

    public WorkerBase createCockCatapultEnoughWorker() {
        //return new CockCatapultEnoughWorker();
        return new CatapultPIDThatFinishesWorker();
    }

    public WorkerBase createConsumeBallAndFinishCockingWorker(Collector main) {
        return new ConsumeBallAndFinishCockingWorker(this, main);
    }
    
    public WorkerBase createCollectAndFireBallFromSideWorker(Collector main, Collector side) {
        return new CollectAndFireBallFromSideWorker(this, main, side);
    }
}
