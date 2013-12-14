/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.logging.LogProducer;

/**
 *
 * @author Alex
 */
public class WorkerBase extends Loggable{
   
    public WorkerBase(String name)
    {
        super(name);
    }
    
    public void exec() {
        
    }
    
    public boolean isFinished() {
        return false;
    }    
}
