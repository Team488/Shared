/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class SetDesiredHeadingWorker extends AerialWorkerBase {
    
    private double heading;
    
    public SetDesiredHeadingWorker(double heading)
    {
        super("SetDesiredHeadingWorker");
        this.heading = heading;
    }
    
    public void init()
    {
        Info("Initializing");
    }
    
    public void exec()
    {
        double potentialHeading = heading % 360;        
        
        if (potentialHeading < 0)
        {
            potentialHeading += 360;
        }
        
        robot().GetDriveCore().SetDesiredHeading(potentialHeading, true);
    }
     
        
    public ExecState isFinished() 
    {
        return ExecState.SUCCESS;
    }    
}
