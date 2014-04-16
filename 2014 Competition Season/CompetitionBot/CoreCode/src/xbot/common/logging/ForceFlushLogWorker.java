/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;

/**
 * This worker will force logs to flush to permanent storage. This is a good thing to do
 * every time the robot is disabled.
 * @author John
 */
public class ForceFlushLogWorker extends AerialWorkerBase {
    
    /**
     *
     */
    public ForceFlushLogWorker()
    {
        super("ForceFlushLogWorker");
    }
    
    /**
     *
     */
    public void exec()
    {
        CommonTools.Get().logConsumer.ForceFlushToStorage();
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }  
}
