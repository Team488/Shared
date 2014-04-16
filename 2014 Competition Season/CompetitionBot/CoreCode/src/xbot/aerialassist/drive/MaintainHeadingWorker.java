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
public class MaintainHeadingWorker extends BaseRotationWorker {
    
    private DriveHeadingMaintainer maintainer;
    
    public MaintainHeadingWorker()
    {
        super("MaintainHeadingWorker");
        maintainer = new DriveHeadingMaintainer();
    }
    
    public void init()
    {
        // Take the current heading and apply it!
        double desiredHeading = robot().GetSensorCore().getCurrentHeading();
        robot().GetDriveCore().SetDesiredHeading(desiredHeading);
    }
    
    public void exec()
    {
         SetRotationForce(maintainer.MaintainHeading());
    }
    
    public ExecState isFinished()
    {
        if (robot().GetDriveCore().shouldIgnoreJoystickWhenRotating())
        {
            return ExecState.NOT_DONE;
        }
        if (robot().GetSensorCore().headingWasResetRecently())
        {
            return ExecState.SUCCESS;
        }
        if (isJoystickBeingUsed())
        {
            return ExecState.FAILURE;
        }
        return ExecState.NOT_DONE;
    }
}
