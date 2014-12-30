/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class ShouldAbortAutoUsingVisionWorker extends AerialWorkerBase {
    
    public DoubleProperty maxWaitTime = 
            new DoubleProperty("autoAbortMaxWaitTime", 3500);
    
    public DoubleProperty minWaitTime = 
            new DoubleProperty("autoAbortMinWaitTime", 2000);
    
    public DoubleProperty minHotTime = 
            new DoubleProperty("autoAbortMinHotTime", 1000);
    
    double hotTime;
    
    double initWorkerMarker;
    double previousExecTime;
    
    boolean wasEverHot;
    
    public ShouldAbortAutoUsingVisionWorker() {
        super("ShouldAbortAutoUsingVisionWorker");
    }
    
    public void init() {
        hotTime = 0;
        wasEverHot = false;
        initWorkerMarker = CommonTools.Get().time.GetMarker();
        previousExecTime = CommonTools.Get().time.GetMarker();
    }
    
    public void exec() {
        boolean isHot = robot().GetVisionCore().isGoalHot();
        
        double elapsedTime = CommonTools.Get().time.GetElapsedMSecFromMarker(previousExecTime);
        previousExecTime = CommonTools.Get().time.GetMarker();
        if(isHot) {
            wasEverHot = true;
            hotTime += elapsedTime;
        }
    }
    
    public ExecState isFinished() {
        double elapsedWorkerTime = 
                CommonTools.Get().time.GetElapsedMSecFromMarker(initWorkerMarker);
        if(wasEverHot) {
            if(hotTime > minHotTime.get()) {
                return ExecState.SUCCESS;
            } else if (elapsedWorkerTime > maxWaitTime.get()) {
                return ExecState.FAILURE;
            }
        } else if(elapsedWorkerTime > minWaitTime.get()) {
            return ExecState.FAILURE;
        }
        
        return ExecState.NOT_DONE;
    }
    
    
}
