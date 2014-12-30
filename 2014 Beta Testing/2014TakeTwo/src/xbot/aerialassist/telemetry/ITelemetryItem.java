/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

/**
 * The definition of what makes a telemetry item.
 * @author John
 */
public interface ITelemetryItem {
    
    /**
     * The name of the Telemetry item (i.e. Gyro)
     * @return
     */
    public String GetName();
    
    /**
     * The data collected by the telemetry item (i.e. "90 degrees")
     * @return
     */
    public String GetData();
    
    /**
     * If you need to do any work to gather your data, do it here.
     */
    public void GatherData();
    
}
