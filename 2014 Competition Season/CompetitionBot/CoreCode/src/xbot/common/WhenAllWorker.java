/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

import java.util.Vector;

/**
 *
 * @author sterlingdorminey
 */
public class WhenAllWorker extends WorkerBase {
    private final Vector workers;
    
    protected WhenAllWorker(String workerName) {
        super(workerName);
        this.workers = new Vector();        
    }
    
    public WhenAllWorker() {
        super("WhenAllWorker");
        
        this.workers = new Vector();
    }
    
    public void addWorker(WorkerBase worker) {
        this.workers.addElement(worker);
    }
    
    public void init() {
        for (int i = 0; i < this.workers.size(); i++) {
            ((WorkerBase) this.workers.elementAt(i)).init();
        }
    }
    
    public void exec() {
        // Do not attempt to do any work if any of the tasks have failed.
        if (ExecState.FAILURE == this.isFinished()) {
            return;
        }
        
        for (int i = 0; i < this.workers.size(); i++) {
            WorkerBase worker = this.getWorker(i);
            
            // Don't do any work if the work has succeeded.
            if (ExecState.NOT_DONE == worker.isFinished()) {
                worker.exec();
            }
        }
    }

    
    public ExecState isFinished() {
        ExecState state = ExecState.SUCCESS;
        for (int i = 0; i < this.workers.size(); i++) {
            WorkerBase worker = (WorkerBase) this.workers.elementAt(i);
            // Concatenate all of the ExecStates together.
            state = ExecState.Join(state, (ExecState) worker.isFinished());
        }
        
        return state;
    }    
    
    private WorkerBase getWorker(int index) {
        return (WorkerBase) this.workers.elementAt(index);
    }
}
