/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public abstract class BaseRotationWorker extends AerialWorkerBase {
    
    DoubleProperty usingJoystickThreshold = new DoubleProperty("UsingJoystickThreshold", 0.1);
    
    public BaseRotationWorker(String name)
    {
        super(name);
    }

    protected void SetRotationForce(double force)
    {
        robot().GetDriveCore().SetDesiredRotationalForce(force);
    }
    
    protected boolean isJoystickBeingUsed()
    {
        return Math.abs(robot().GetOICore().getRightJoyX()) > usingJoystickThreshold.get();
    }
}
