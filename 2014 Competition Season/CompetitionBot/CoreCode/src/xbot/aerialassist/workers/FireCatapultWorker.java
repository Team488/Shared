/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.systems.CatapultCore;
import xbot.common.CommonTools;
import xbot.common.ExecState;


/**
 *
 * @author Ken
 */
public class FireCatapultWorker extends AerialWorkerBase{
   
    
        private boolean limitSwitch;
        private double startTime;
        private boolean firingComplete = false;

    
    /**
     *
     */
    public FireCatapultWorker()
    {
        super("FireCatapultWorker");
    }
    
    /**
     *
     */
    public void init()
    {
        robot().GetCatapultCore().SetIsFiringFlag();
        firingComplete = false;
        Info("Catapult firing");
        startTime = CommonTools.Get().time.GetMarker();
    }
    
    /**
     *
     */
    public void exec()
    {
        if(robot().GetCatapultCore().getIsFiring())
        {
            robot().GetCatapultCore().fireCatapult();
        }
        else
        {
            robot().GetCatapultCore().stopCatapult();
            firingComplete = true;
        }
        
    }
    
        /**
     *
     * @return
     */
    public ExecState isFinished() {
            if (firingComplete)
            {
                Info("Catapult finished firing");
                robot().GetCatapultCore().stopCatapult();
                return ExecState.SUCCESS;
            }
            return ExecState.NOT_DONE;
    }    
    
}
