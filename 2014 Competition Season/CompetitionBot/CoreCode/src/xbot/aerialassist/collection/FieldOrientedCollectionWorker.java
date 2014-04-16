/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.aerialassist.collection.fieldOrientedMachine.BackInRangeCollectionWorker;
import xbot.aerialassist.collection.fieldOrientedMachine.FrontInRangeCollectionWorker;
import xbot.aerialassist.collection.fieldOrientedMachine.RaiseBothArmsWorker;
import xbot.aerialassist.workers.AerialStateMachineWorker;

/**
 *
 * @author John
 */
public class FieldOrientedCollectionWorker extends AerialStateMachineWorker {
    
    public FieldOrientedCollectionWorker()
    {
        super("FieldOrientedCollectionWorker");
        BuildMachine();
    }
    
    private void BuildMachine()
    {
        int armsUp = this.stateMachine.addWorker(new RaiseBothArmsWorker());
        int front = this.stateMachine.addWorker(new FrontInRangeCollectionWorker());
        int back = this.stateMachine.addWorker(new BackInRangeCollectionWorker());
        
        this.stateMachine.onStart(armsUp);
        
        this.stateMachine.onSuccess(armsUp, front);
        this.stateMachine.onFailure(armsUp, back);
        
        this.stateMachine.onFailure(front, armsUp);
        this.stateMachine.onFailure(back, armsUp);
        
        // stupid validation
        this.stateMachine.onSuccess(front, front);
        this.stateMachine.onSuccess(back, back);
    }
}
