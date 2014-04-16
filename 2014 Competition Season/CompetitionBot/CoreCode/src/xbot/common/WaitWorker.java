/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class WaitWorker extends WorkerBase {
    private DoubleProperty timeout;
    private double startMarker;
    
    public WaitWorker(String name, DoubleProperty timeout)
    {
        super(name);
        this.timeout = timeout;
    }
    
    public WaitWorker(DoubleProperty timeout) {
        super("WaitWorker");
        
        this.timeout = timeout;
    }
    
    /**
     * Initialize the worker.
     */
    public void init() {
        startMarker = CommonTools.Get().time.GetMarker();
    }
    
    public ExecState isFinished() {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(startMarker) 
                > timeout.get()) {
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}
