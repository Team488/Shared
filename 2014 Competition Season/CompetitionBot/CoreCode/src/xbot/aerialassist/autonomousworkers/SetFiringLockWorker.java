/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class SetFiringLockWorker extends AerialWorkerBase {
    
    public SetFiringLockWorker()
    {
        super("SetFiringLockWorker");
    }
    
    public void init()
    {
        Info("Initializing");
        robot().GetCatapultCore().SetIsFiringFlag();
    }
    
    public ExecState isFinished()
    {
        return ExecState.SUCCESS;
    }
}
