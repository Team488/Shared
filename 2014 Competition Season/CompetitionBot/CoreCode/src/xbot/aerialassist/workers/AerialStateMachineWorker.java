/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.workers;

import xbot.aerialassist.*;
import xbot.common.*;

/**
 *
 * @author sterlingdorminey
 */
public abstract class AerialStateMachineWorker extends AerialWorkerBase {

    protected final StateMachineWorker stateMachine;
    
    public AerialStateMachineWorker(String name) {
        super(name);
        
        this.stateMachine = new StateMachineWorker(name);
    }
    
    public void init() {
        this.stateMachine.init();
    }
    
    public void exec() {
        this.stateMachine.exec();
    }
    
    public String getCurrentWorkerName() {
        return this.stateMachine.getCurrentWorkerName();
    }
    
    public int getCurrentState() {
        return this.stateMachine.getCurrentState();
    }
    
    public ExecState isFinished() {
        return this.stateMachine.isFinished();
    }
}
