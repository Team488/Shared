/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 * This worker, given a Collector to act upon and an arm position to achieve, will move
 * that Collector to that position.
 * @author Nikhil
 */
public class CollectorDeployWorker extends AerialWorkerBase {
    Collector collector;
    CollectorDeployState targetState;
    
    /**
     *
     * @param collector The collector to act upon
     * @param collectorState Where you want the arm to end up
     */
    public CollectorDeployWorker(Collector collector, CollectorDeployState collectorState){
        super("CollectorDeployWorker:" + collector.getName() + ":" + collectorState.name);
        this.collector = collector;
        this.targetState = collectorState;
    }
    
    /**
     *
     */
    public void init() {
        Info("Initializing CollectorDeployWorker on Collector: " + collector.getName() + "with direction: " + targetState.name);
    }
    
    /**
     *
     */
    public void exec() {
        if(targetState == CollectorDeployState.STOP) {
             collector.stopDeploy();
        } else if(targetState == CollectorDeployState.DOWN) {
              collector.goToDeployedState();
        } else if(targetState == CollectorDeployState.UP) {
            collector.goToUpState();
        } else if(targetState == CollectorDeployState.FIRING) {
            collector.goToFiringState();
        }
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        
        ExecState potentialExecState = ExecState.NOT_DONE;
        
        if(targetState == CollectorDeployState.STOP) {
            potentialExecState = ExecState.NOT_DONE;
        } else if(targetState == CollectorDeployState.DOWN 
                && collector.getSensors().getIsFullyDown()) {
            potentialExecState = ExecState.SUCCESS;
        } else if(targetState == CollectorDeployState.UP 
                && collector.getSensors().getIsFullyUp()) {
            potentialExecState = ExecState.SUCCESS;
        } else if(targetState == CollectorDeployState.FIRING 
                && (collector.getSensors().GetIsSafeToFire())) {
            potentialExecState = ExecState.SUCCESS;
        }
        
        if ((potentialExecState == ExecState.SUCCESS) || (potentialExecState == ExecState.FAILURE))
        {
            collector.stopDeploy();
            return potentialExecState;
        }
        else
        {
            return ExecState.NOT_DONE;
        }
    }
}
