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
public class TurnOnCompressorWorker extends AerialWorkerBase {
    
    /**
     *
     */
    public TurnOnCompressorWorker()
    {
        super("CompressorWorker");
    }
    /**
     *
     */
    public void init() {
        robot().GetPneumaticCore().TurnOnCompressor();
        Important("Compressor on");
    }
}
