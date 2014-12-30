/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;
import xbot.common.properties.BooleanProperty;

/**
 *
 * @author Ken
 */
public class CockCatapultWorker extends AerialWorkerBase {
    
    protected boolean hasCockedCatapult;
    
    /**
     *
     */
    public CockCatapultWorker(){
        super("cockCatapultWorker");
    }
    
    /**
     *
     * @param name
     */
    public CockCatapultWorker(String name){
        super(name);
    }
    
    /**
     *
     */
    public void init(){
       
        Info("initializing");
        hasCockedCatapult = false;
    }
    
    /**
     *
     */
    public void exec(){
        if(robot().GetCatapultCore().isCatapultCocked() == false 
                && !hasCockedCatapult
                && robot().GetCatapultCore().shouldCockCatapult.get())
        {
            robot().GetCatapultCore().cockCatapult();
            //Verbose("Catapult is being cocked");
        }
        else
        {   
            // if we're stopped because we're cocked, don't try cocking again this round
            if(!robot().GetCatapultCore().shouldCockCatapult.get()) {
                hasCockedCatapult = true;
            }
            robot().GetCatapultCore().stopCatapult();
            //Verbose("Catapult is already cocked");
        }
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        if (robot().GetCatapultCore().isCatapultCocked()) {
            robot().GetCatapultCore().stopCatapult();
            hasCockedCatapult = true;
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}
