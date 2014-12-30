/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author sterlingdorminey
 */
public class CockCatapultEnoughWorker extends AerialWorkerBase {

    private boolean flag;
    
    public CockCatapultEnoughWorker() {
        super("CockCatapultEnoughWorker");
    }
    
    public void init()
    {
        Info("Initializing");
        flag = false;
    }
    
    
    public void exec() {
        if(this.isFinished() == ExecState.NOT_DONE)
        {
            robot().GetCatapultCore().cockCatapult();
            //Verbose("Catapult is being cocked");
        }
    }
    
    public ExecState isFinished() {
        if (robot().GetCatapultCore().IsCatapultReadyToLoad()) {
            // This is safer, but in autonomous there will be a quick stop
            // between this, and the next step.
            robot().GetCatapultCore().stopCatapult();
            
            if (!flag)
            {
                Info("goal reached:" + robot().GetCatapultCore().getCatapultScaledPosition());
                flag = true;
            }
            
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}
