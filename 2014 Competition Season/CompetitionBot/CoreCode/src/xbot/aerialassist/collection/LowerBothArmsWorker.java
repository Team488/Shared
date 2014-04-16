/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.autonomousworkers.DeployBothCollectorsWorker;
import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class LowerBothArmsWorker extends AerialWorkerBase {
    Collector front;
    Collector back;
    public LowerBothArmsWorker() {
        super("LowerBothArmsWorker");
        front = robot().getCollectionCore().getFrontCollector();
        back = robot().getCollectionCore().getBackCollector();
    }
    
    public void init() {
        front.setTargetDeployState(CollectorDeployState.DOWN);
        back.setTargetDeployState(CollectorDeployState.DOWN);
    }
    
    public void interrupted() {
        front.setTargetDeployState(CollectorDeployState.UP);
        back.setTargetDeployState(CollectorDeployState.UP);
    }
    
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }
}
