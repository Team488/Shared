/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;

/**
 *
 * @author Maya
 */
public class EnableFieldOrientedDriveWorker extends AerialWorkerBase {
    
    public EnableFieldOrientedDriveWorker()
    {
        super("EnableFieldOrientedDriveWorker");
    }
    
    public void init()
    {
        
    }
    
    public void exec()
    {
        robot().GetDriveCore().mecanumFieldOriented.set(true);
        robot().GetDriveCore().tankFieldOriented.set(true);
        robot().GetDriveCore().useTranslationVelocityPID.set(true);
        
    }
}
