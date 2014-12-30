/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

/**
 *
 * @author sterlingdorminey
 */
public class PropertySaverWorker extends WorkerBase {

    private boolean hasAlreadySaved;
    
    public PropertySaverWorker() {
        super("PropertySaver");
        this.init();
    }
    
    public void init() {
        this.hasAlreadySaved = false;
    }
    
    public void exec() {
        if (!this.hasAlreadySaved) {
            CommonTools.Get().propertyManager.saveOutAllProperties();
            this.hasAlreadySaved = true;
        }
    }
    
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }
}
