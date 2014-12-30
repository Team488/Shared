/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.systems.DriveCore;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class HumanRotationWorker extends BaseRotationWorker {
    
    double deadband = 0.1;
    
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
        SetRotationForce(
                DriveCore.remapMinusDeadband(robot().GetOICore().getRightJoyX(), deadband));
    }
    
    public ExecState isFinished()
    {
        if (robot().GetDriveCore().shouldIgnoreJoystickWhenRotating())
        {
            return ExecState.FAILURE;
        }
                
        if (Math.abs(robot().GetOICore().getRightJoyX()) < deadband)
        {
            return ExecState.FAILURE;
        }
        return ExecState.NOT_DONE;
    }
}
