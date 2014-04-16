/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.catapult;

import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class CatapultPIDThatFinishesWorker extends CatapultPIDWorker {
        
    public CatapultPIDThatFinishesWorker()
    {
        super("CatapultPIDThatFinishesWorker");
    }
    
    public ExecState isFinished()
    {
        if (robot().GetCatapultCore().isCatapultCocked())
        {
            Info("Finished! Currently at position:" + robot().GetCatapultCore().getCatapultScaledPosition());
            return ExecState.SUCCESS;
        }
        return ExecState.NOT_DONE;
    }
}
