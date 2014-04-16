/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;


/**
 *
 * @author Alex
 */
public class ResetGyroWorker extends AerialWorkerBase {
    
    /**
     *
     */
    public ResetGyroWorker()
    {
        super("ResetGyroWorker");
    }
        
    /**
     *
     */
    public void exec() {
        robot().GetSensorCore().resetHeading();
        Important("Resetting heading");
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }
}
