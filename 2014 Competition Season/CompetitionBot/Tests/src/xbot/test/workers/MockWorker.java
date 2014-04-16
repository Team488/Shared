/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.workers;

import xbot.common.ExecState;
import xbot.common.WorkerBase;

/**
 *
 * @author jogilber
 */
public class MockWorker extends WorkerBase {

    private boolean wasInitCalled;
    private int timesExecuted;
    private ExecState execState;
    
    public MockWorker() {
        super("Mock");
        this.reset();
    }
    
    public int getTimesExecuted() {
        return this.timesExecuted;
    }
    
    public void setExecState(ExecState execState) {
        this.execState = execState;
    }
    
    public void reset() {
        this.wasInitCalled = false;
        this.timesExecuted = 0;
        this.execState = ExecState.NOT_DONE;
    }
    
    public void init() {
        this.wasInitCalled = true;
    }
    
    public void exec() {
        if (!this.wasInitCalled) {
            throw new RuntimeException("tried to exec without init.");
        }
        this.timesExecuted++;
    }
    
    public ExecState isFinished() {
        return this.execState;
    }
}
