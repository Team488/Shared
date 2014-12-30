/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

import xbot.common.properties.BooleanProperty;

/**
 * Worker which returns Success if the property is true,
 * and Failure otherwise.
 * 
 * @author sterlingdorminey
 */
public class BooleanPropertyWorker extends WorkerBase {
    private final BooleanProperty property;
    
    public BooleanPropertyWorker(BooleanProperty property) {
        super("BoolPropertyWorker:" + property.key);
        this.property = property;
    }
    
    public ExecState isFinished() {
        if (this.property.get()) {
            return ExecState.SUCCESS;
        }
        
        return ExecState.FAILURE;
    }
}
