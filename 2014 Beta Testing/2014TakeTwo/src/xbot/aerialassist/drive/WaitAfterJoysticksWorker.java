/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class WaitAfterJoysticksWorker extends BaseRotationWorker {
    
    double initTime;
    DoubleProperty robotResumeRotationControlTime = 
            new DoubleProperty("robotResumeRotationControlTimeMS", 1000);
    
    public WaitAfterJoysticksWorker()
    {
        super("WaitAfterJoystickWorker");
    }
    
    public void init()
    {
        Info("Initializing");
        initTime = CommonTools.Get().time.GetMarker();
    }
    
    public void exec()
    {
        SetRotationForce(0);
    }
    
    public ExecState isFinished()
    {
        double elapsedTime = CommonTools.Get().time.GetElapsedMSecFromMarker(initTime);
        // If we wait for long enough, or if somebody changes the desired heading, we should then start trying to
        // go to that heading.
        if (elapsedTime > robotResumeRotationControlTime.get() ||
            robot().GetDriveCore().DesiredHeadingWasChangedRecently())
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
