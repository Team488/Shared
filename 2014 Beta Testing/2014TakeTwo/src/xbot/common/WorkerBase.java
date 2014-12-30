/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 * The base class from which all Workers extend. This is essentially a kind of Command that does
 * not depend on WPILib.
 * @author Alex
 */
public abstract class WorkerBase extends Loggable {
   
    /**
     * The name of this worker
     * @param name
     */
    public WorkerBase(String name)
    {
        super(name);
    }
    
    /**
     * Override this - it's what the worker will be doing periodically while it is not finished
     */
    public void exec() {
        
    }
    
    /**
     * Override this - it's what the worker will do once when initialized.
     */
    public void init() {
        
    }
    
    public void interrupted() {
        Info(" was interrupted!");
    }
    
    /**
     * Returns the "finished" state of the robot. @see ExecState.
     * @return
     */
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }    
}
