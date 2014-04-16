/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;
import xbot.common.Loggable;
/**
 *
 * @author Ken
 */
public class CollectorDeployPIDWorker extends AerialWorkerBase {

    private CollectorDeployState deployState;
    private Collector collector;
    double position;
    
    public CollectorDeployPIDWorker(Collector collector, CollectorDeployState collectorDeployState) {
        super("CollectorDeployPIDWorker:" + collector.getName() + ":" + collectorDeployState.name);
        collector.setTargetDeployState(collectorDeployState);
        deployState = collectorDeployState;
        this.collector = collector;
    }
    
    public void init()
    {
        collector.setTargetDeployState(deployState);
        Info("initializing");
    }
    
    public void exec()
    {

    }
    
    public ExecState isFinished() {
        if(deployState == CollectorDeployState.UP 
                && collector.getSensors().getIsFullyUp())
        {
            Info("Finished Up, position: " + collector.getSensors().GetCollectorPosition());
            return ExecState.SUCCESS;
        }
        else if(deployState == CollectorDeployState.DOWN 
                && collector.getSensors().getIsFullyDown())
        {
            Info("Finished Down, position: " + collector.getSensors().GetCollectorPosition());
             return ExecState.SUCCESS;
        }
        else if(deployState == CollectorDeployState.FIRING
                && collector.getSensors().GetIsSafeToFire())
        {
            Info("Finished Firing, position: " + collector.getSensors().GetCollectorPosition());
             return ExecState.SUCCESS;
        } 
        else if(deployState == CollectorDeployState.DURESS 
                && collector.getSensors().GetIsSafeToFireUnderDuress())
        {
            Info("Finished Firing Under Duress");
             return ExecState.SUCCESS;
        }
        else if (deployState == CollectorDeployState.SAFE
                && collector.getSensors().getIsInSafePosition()) {
            Info("Finished Safe, position: " + collector.getSensors().GetCollectorPosition());
            return ExecState.SUCCESS;
        } else {
            return ExecState.NOT_DONE;
        }
        
    }
    
}
