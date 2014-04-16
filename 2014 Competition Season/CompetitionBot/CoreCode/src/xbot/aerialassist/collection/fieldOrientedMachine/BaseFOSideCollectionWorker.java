/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection.fieldOrientedMachine;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class BaseFOSideCollectionWorker extends AerialWorkerBase {
    
    private Collector toLower;
    private Collector toRaise;
    
    public BaseFOSideCollectionWorker(String side, Collector toLower, Collector toRaise)
    {
        super(side+"FOSideCollectionWorker");
        this.toLower = toLower;
        this.toRaise = toRaise;
    }
    
    public void init()
    {
        Info("Lowering:"+toLower.getName()+", Raising:" + toRaise.getName());
        toLower.setTargetDeployState(CollectorDeployState.DOWN);
        toRaise.setTargetDeployState(CollectorDeployState.UP);
    }
}
