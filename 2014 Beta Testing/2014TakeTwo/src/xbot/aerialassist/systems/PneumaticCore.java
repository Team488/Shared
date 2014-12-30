/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.common.Loggable;

/**
 * This system wraps the compressor. In the past, we've found that it's sometimes useful to disable the compressor
 * if all battery power is needed for some critical precision task. That may not be the case this year, but it's better
 * to have this capability in advance just in case.
 * @author Anthony
 */
public class PneumaticCore extends Loggable{
    /**
     *
     */
    public PneumaticCore() {
        super ("PneumaticCore");
        Info("Initializing");
    }
    
    private boolean compressorOn;

    /**
     * Get the value of compressorOn
     *
     * @return the value of compressorOn
     */
    public boolean isCompressorOn() {
        return compressorOn;
    }
    /**
     *
     */
    public void TurnOnCompressor(){
        compressorOn = true;
        
        Important("Compressor is ON");
    }
    /**
     *
     */
    public void TurnOffCompressor(){
        compressorOn = false;
        
        Important("Compressor is OFF");
    }
}