/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core;

import xbot.common.WorkerBase;

/**
 *
 * @author Alex
 */
public class CoreWorkerBase extends WorkerBase {
    protected RobotContext robot() {
        return RobotContext.Get();
    }
    
    public CoreWorkerBase(String name)
    {
        super(name);
    }
}
