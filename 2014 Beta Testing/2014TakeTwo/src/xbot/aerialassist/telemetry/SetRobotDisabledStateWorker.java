/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class SetRobotDisabledStateWorker extends AerialWorkerBase {
    
    private boolean isDisabled;
    
    public SetRobotDisabledStateWorker(boolean isDisabled)
    {
        super("SetRobotDisabledStateWorker");
        this.isDisabled = isDisabled;
    }
    
    public void init()
    {
        Info("Initializing");
    }
    
    public void exec()
    {
        robot().GetTelemetryCore().setIsDisabled(isDisabled);
    }
    
    public ExecState isFinished()
    {
        return ExecState.SUCCESS;
    }
}
