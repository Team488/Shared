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
public class DisableFieldOrientedDriveWorker extends AerialWorkerBase {
    
    public DisableFieldOrientedDriveWorker()
    {
        super("DisableFieldOrientedDriveWorker");
    }
    
    public void init()
    {
        
    }
    
    public void exec()
    {
        robot().GetDriveCore().mecanumFieldOriented.set(false);
        robot().GetDriveCore().tankFieldOriented.set(false);
        robot().GetDriveCore().useTranslationVelocityPID.set(false);   
    }
}
