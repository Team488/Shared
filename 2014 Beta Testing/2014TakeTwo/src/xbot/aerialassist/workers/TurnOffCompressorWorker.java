/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;

/**
 *
 * @author Anthony
 */
public class TurnOffCompressorWorker extends AerialWorkerBase{
    
    /**
     *
     */
    public TurnOffCompressorWorker() 
    { 
         super ("CompressorWorker");
    }
    /**
     *
     */
    public void init() {
        robot().GetPneumaticCore().TurnOffCompressor();
        Important("Compressor off");
    }
}
