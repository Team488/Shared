/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection.fieldOrientedMachine;

import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class BackInRangeCollectionWorker extends BaseFOSideCollectionWorker {
    
    public BackInRangeCollectionWorker()
    {
        super("Back", robot().getCollectionCore().getBackCollector(), robot().getCollectionCore().getFrontCollector());
    }
    
    public ExecState isFinished()
    {
        // If back in range, success
        // If no FO demanded, failure
        if (!robot().getCollectionCore().isOperatorJoystickSufficientlyDisplaced())
        {
            return ExecState.FAILURE;
        }
        return ExecState.NOT_DONE;
    }
}
