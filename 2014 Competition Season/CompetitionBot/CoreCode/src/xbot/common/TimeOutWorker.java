/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.properties.DoubleProperty;

/**
 * A form of worker that can "time out" and stop operation automatically.
 * @author Alex
 */
public class TimeOutWorker extends WorkerBase {
    
    WorkerBase childWorker;
    double timeoutTotalMS;
    DoubleProperty timeoutTotalMSProperty;
    double startMarker;
    boolean timeoutIsSuccess;
    
    /**
     * TimeOutWorker wraps an existing worker and runs it until either it completes or the timeout elapses.
     * @param worker the worker to wrap
     * @param timeoutInMS how long to wait for this worker to finish
     * @param timeoutIsSuccess  If this worker times out - should that be considered Success or Failure?
     */
    public TimeOutWorker(
            WorkerBase worker,
            DoubleProperty timeoutInMS,
            boolean timeoutIsSuccess)
    {
        super("TimeOutWorker");
        this.childWorker = worker;
        this.timeoutIsSuccess = timeoutIsSuccess;
        this.timeoutTotalMSProperty = timeoutInMS;
    }
    /**
     *
     * @param worker
     * @param timeoutInMS
     * @param timeoutIsSuccess
     */
    public TimeOutWorker(WorkerBase worker, double timeoutInMS, 
            boolean timeoutIsSuccess){
        super("TimeOutWorker");
        this.childWorker = worker;
        this.timeoutIsSuccess = timeoutIsSuccess;
        this.timeoutTotalMS = timeoutInMS;
    }
    
    /**
     * Initialize the worker.
     */
    public void init() {
        childWorker.init();
        startMarker = CommonTools.Get().time.GetMarker();
    }
    
    /**
     * Execute the worker.
     */
    public void exec() {
        childWorker.exec();
    }
    
    /**
     * Is the worker complete / timed out?
     * @return
     */
    public ExecState isFinished() {
        ExecState childResult = childWorker.isFinished();
        
        double timeoutTotalMillis = 0;
        // If we constructed the timeout worker using the property,
        // then use the property.
        if (this.timeoutTotalMSProperty != null) {
            timeoutTotalMillis = this.timeoutTotalMSProperty.get();
        }
        else {
            timeoutTotalMillis = this.timeoutTotalMS;
        }
        
        if(childResult == ExecState.NOT_DONE && 
            (CommonTools.Get().time.GetElapsedMSecFromMarker(startMarker) 
                > timeoutTotalMillis)) {
            if(timeoutIsSuccess) {
                return ExecState.SUCCESS;
            } else {
                return ExecState.FAILURE;
            }
        } else {
            return childResult;
        }
    }    
}
