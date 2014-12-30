/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

import xbot.aerialassist.RobotContext;

/**
 * Simple abstract class to allow telemetry items access to the robot() convenience method.
 * @author John
 */
public abstract class TelemetryItem implements ITelemetryItem {
    
    /**
     *
     * @return
     */
    protected RobotContext robot()
    {
        return RobotContext.Get();
    }
    
}
