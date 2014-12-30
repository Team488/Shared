/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.RobotContext;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author test
 */
public class WaitForAutoTimeWorker extends AerialWorkerBase {
    
private DoubleProperty timeout;
    
    public WaitForAutoTimeWorker(String name, DoubleProperty timeout)
    {
        super(name);
        this.timeout = timeout;
    }
    
    public WaitForAutoTimeWorker(DoubleProperty timeout) 
    {
        super("WaitWorker");
        this.timeout = timeout;
    }
    
    /**
     * Initialize the worker.
     */
    public void init() {
    }
    
    public ExecState isFinished() {
        if (RobotContext.Get().getAutonomousCore().timeInMSecSinceStartofAuto()> timeout.get())
        {
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}

