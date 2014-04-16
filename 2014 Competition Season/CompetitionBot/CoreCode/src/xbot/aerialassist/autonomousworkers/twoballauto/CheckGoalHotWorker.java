/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.Time;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 * This worker checks whether or not the goal that the robot
 * is currently facing is hot.
 * 
 * If it is, it returns SUCCESS.
 * If it is not, it returns FAILURE.
 * If we don't know yet, we return NOT_DONE.
 * 
 * NOT_DONE will be returned for a fixed delay, after which
 * we check whether the driver's station has signaled.
 * after this fixed delay, we will return either SUCCESS or FAILURE.
 * @author sterlingdorminey
 */
public class CheckGoalHotWorker extends AerialWorkerBase {
    
    private final Time time;
    
    private double startMarker;
    
    // Result to return in isFinished() once the time has elapsed.
    private ExecState result;
    
    public CheckGoalHotWorker() {
        super("CheckGoalHotWorker");
        
        this.time = CommonTools.Get().time;
    }
    
    public void init() {
        Info("Initializing");
        this.startMarker = this.time.GetMarker();
        this.result = null;
    }
    
    public ExecState isFinished() {
        // If we've already calculated ExecState, return that.
        if (this.result != null) {
            return this.result;
        }
        
        double timeElapsed = this.time.GetElapsedMSecFromMarker(startMarker);
        
        
        if (robot().GetVisionCore().isGoalHot()) {
            this.result = ExecState.SUCCESS;
            return this.result;
        }
        // If we are due but haven't calculated a result, go calculate.
        else if (timeElapsed > robot().getAutonomousCore().goalHotWaitTime.get()) {
            this.result = ExecState.FAILURE;
            return this.result;
        }
        
        // Otherwise, return not done.
        return ExecState.NOT_DONE;
    }
}
