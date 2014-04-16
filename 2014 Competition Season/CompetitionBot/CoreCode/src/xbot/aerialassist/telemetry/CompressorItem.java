/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

/**
 * Logs compressor state via telemetry.
 * @author Maya
 */
public class CompressorItem extends TelemetryItem {

    private boolean isCompressorOn;
    
    /**
     *
     * @return
     */
    public String GetName() {
        return "Compressor";
    }

    /**
     *
     * @return
     */
    public String GetData() {
        return String.valueOf(isCompressorOn);
        
    }

    /**
     *
     */
    public void GatherData() {
        isCompressorOn = robot().GetPneumaticCore().isCompressorOn();
    }
    
}
