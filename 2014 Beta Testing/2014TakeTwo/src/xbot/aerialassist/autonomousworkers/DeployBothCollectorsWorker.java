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
import xbot.aerialassist.collection.CollectorDeployWorker;
import xbot.common.ExecState;
import xbot.common.WorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public class DeployBothCollectorsWorker extends AerialWorkerBase {

    private WorkerBase mainWorker;
    private WorkerBase otherWorker;
    
    public DeployBothCollectorsWorker(
            CollectorDeployState frontState,
            CollectorDeployState backState) {
        super("DeployCollectors:" + frontState.name + "," + backState.name);
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        this.mainWorker = new CollectorDeployPIDWorker(
                back,
                backState);
        
        this.otherWorker = new CollectorDeployPIDWorker(
                front,
                frontState);
    }
    
    public DeployBothCollectorsWorker(
            Collector mainCollector,
            CollectorDeployState mainState,
            Collector otherCollector,
            CollectorDeployState otherState) {
        super("Open collectors");
    
        this.mainWorker = new CollectorDeployPIDWorker(
                mainCollector,
                mainState);
        
        this.otherWorker = new CollectorDeployPIDWorker(
                otherCollector,
                otherState);
    }
    
    public void init() {
        this.mainWorker.init();
        if(useBoth) {
            this.otherWorker.init();
        }
    }
    
    /**
     *
     */
    public void exec() {
        this.mainWorker.exec();
        if(useBoth) {
            this.otherWorker.exec();
        }
    }
    private boolean useBoth = true;
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        if(!useBoth) {
            return mainWorker.isFinished();
        } else {
            return ExecState.Join(
                mainWorker.isFinished(),
                otherWorker.isFinished());
        }
    }
}
