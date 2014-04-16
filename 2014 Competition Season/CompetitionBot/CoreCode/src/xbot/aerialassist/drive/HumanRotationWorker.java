/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class HumanRotationWorker extends BaseRotationWorker {
    
    public HumanRotationWorker()
    {
        super("HumanRotationWorker");
    }
    
    public void init()
    {
        Info("Initializing");
    }
    
    public void exec()
    {
        SetRotationForce(robot().GetOICore().getRightJoyX());
    }
    
    public ExecState isFinished()
    {
        if (robot().GetDriveCore().shouldIgnoreJoystickWhenRotating())
        {
            return ExecState.FAILURE;
        }
                
        if (Math.abs(robot().GetOICore().getRightJoyX()) < 0.1)
        {
            return ExecState.FAILURE;
        }
        return ExecState.NOT_DONE;
    }
}
