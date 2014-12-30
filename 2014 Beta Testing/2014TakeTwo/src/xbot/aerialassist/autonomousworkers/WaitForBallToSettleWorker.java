/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.RobotContext;
import xbot.common.ExecState;

/**
 *
 * @author sterlingdorminey
 */
public class WaitForBallToSettleWorker extends AerialWorkerBase {
    
    public WaitForBallToSettleWorker() {
        super("WaitForBallToSettleWorker");
    }
    
    public ExecState isFinished() {
        if(RobotContext.Get().GetCatapultCore().IsBallSettled()) {
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}
