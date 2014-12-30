/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.WorkerBase;

/**
 * This worker has one simple job - to ask the PropertyManager to save all its properties
 * to storage.
 * @author John
 */
public class SavePropertiesWorker extends WorkerBase {
    
    /**
     *
     */
    public SavePropertiesWorker()
    {
        super("SavePropertiesWorker");
    }
    
    /**
     *
     */
    public void exec()
    {
        CommonTools.Get().propertyManager.saveOutAllProperties();
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }  
}
