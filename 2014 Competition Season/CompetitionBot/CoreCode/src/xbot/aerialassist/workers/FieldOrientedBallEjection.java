/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.systems.CollectionCore;
import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class FieldOrientedBallEjection extends AerialWorkerBase {
    CollectionCore collection;
    
    public FieldOrientedBallEjection() {
        super("FieldOrientedBallEjection");
        collection = robot().getCollectionCore();
    }
    
    public void init() {
        
    }
    
    public void exec() {
        Collector front = collection.getFrontCollector();
        Collector back = collection.getBackCollector();
         if(!collection.isOperatorJoystickSufficientlyDisplaced()) {
            front.stopRoller();
            back.stopRoller();
        } else if(collection.isInFrontCollectorRange()) {
            front.ejectBalls();
            back.intakeBalls();
        } else if(collection.isInBackCollectorRange()) {
            back.ejectBalls();
            front.intakeBalls();
        }
    }
    
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }
}
