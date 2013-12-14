/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core.workers;

import xbot.core.CoreWorkerBase;

/**
 *
 * @author Alex
 */
public class DriveWithJoysticksWorker extends CoreWorkerBase {
    
    public DriveWithJoysticksWorker()
    {
        super("DriveWithJoysticksWorker");
    }
    
    public void exec() {
        robot().GetDriveCore().tankDrive(
                robot().GetOICore().getLeftJoyY(), 
                robot().GetOICore().getRightJoyY());
    }
}
