/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.aerialassist.collection.CollectorRollerWorker;
import xbot.common.ExecState;

/**
 *
 * @author sterlingdorminey
 */
public class ImmediateRollerWorker extends CollectorRollerWorker {
    boolean hasStarted = false;
    
    public ImmediateRollerWorker(Collector collector, CollectorRollerState collectorState) {
        super(collector, collectorState);
    }
    
    public void exec() {
        this.hasStarted = true;
        super.exec();
    }
    
    public ExecState isFinished() {
        if (!this.hasStarted) {
            return ExecState.NOT_DONE;
        }
        return ExecState.SUCCESS;
    }
}
