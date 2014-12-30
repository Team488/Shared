/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist;

import xbot.common.WorkerBase;

/**
 * The AerialWorkerBase is the 2014-game specific (hence, Aerial) worker. It mostly exists so that workers
 * can have the convenience method robot() to get quick access to the RobotContext.
 * @author Alex
 */
public class AerialWorkerBase extends WorkerBase {
    /**
     * Returns the 2014 RobotContext.
     * @return RobotContext
     */
    protected static RobotContext robot() {
        return RobotContext.Get();
    }
    
    /**
     *
     * @param name  The name of the worker
     */
    public AerialWorkerBase(String name)
    {
        super(name);
    }
}
