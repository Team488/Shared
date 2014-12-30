/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection.fieldOrientedMachine;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class RaiseBothArmsWorker extends AerialWorkerBase {
    
    public RaiseBothArmsWorker()
    {
        super("RaiseBothArmsWorker");
    }
    
    public void init()
    {
        Info("Setting arms up");
        robot().getCollectionCore().ReturnToStartingPosition();
    }
    
    public ExecState isFinished()
    {
        // Success -> Front is in range
        // Failures -> Back is in range
        if (robot().getCollectionCore().isOperatorJoystickSufficientlyDisplaced())
        {
            if (robot().getCollectionCore().isInFrontCollectorRange())
            {
                return ExecState.SUCCESS;
            }
            if (robot().getCollectionCore().isInBackCollectorRange())
            {
                return ExecState.FAILURE;
            }
        }
        
        return ExecState.NOT_DONE;
    }
}
