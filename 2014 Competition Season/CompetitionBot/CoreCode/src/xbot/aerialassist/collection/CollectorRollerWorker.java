/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;
import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.collection.Collector;
import xbot.common.ExecState;
/**
 * This worker, given a Collector to act upon and a Roller action, will perform that action
 * on that collector.
 * @author Nikhil
 */
public class CollectorRollerWorker extends AerialWorkerBase {
    
    private Collector collector;
    private CollectorRollerState targetCollectorState;
    /**
     *
     * @param collector The collector to act upon
     * @param collectorState    How to move the rollers.
     */
    public CollectorRollerWorker(Collector collector, CollectorRollerState collectorState){
        super("CollectorRollerWorker:" + collector.getName() + ":" + collectorState.name);
        this.collector = collector;
        targetCollectorState = collectorState;
    }
    /**
     *
     */
    public void init(){
        
        Info("Initializing CollectorRollerWorker on Collector: " + collector.getName() + "with direction: " + targetCollectorState.name);
        
        if(targetCollectorState == CollectorRollerState.COLLECT){
            collector.intakeBalls();
        }
        else if(targetCollectorState == CollectorRollerState.REVERSE){
            collector.ejectBalls();
        }
        else 
            collector.stopRoller();
        }
    
//    public ExecState isFinished() {
//        return ExecState.SUCCESS;
//    }
     
}
