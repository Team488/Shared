/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class KeepCatapultCockedWorker extends CockCatapultWorker {
    /**
     *
     */
    public KeepCatapultCockedWorker() {
        super("KeepCatapultCockedWorker");
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }
}
