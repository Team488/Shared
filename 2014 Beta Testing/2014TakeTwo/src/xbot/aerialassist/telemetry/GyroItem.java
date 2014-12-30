/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

/**
 * Logs the gyro heading via telemetry.
 * @author John
 */
public class GyroItem extends TelemetryItem {

    private double gyroHeading;
    
    /**
     *
     * @return
     */
    public String GetName() {
        return "Gyro";
    }

    /**
     *
     * @return
     */
    public String GetData() {
        return String.valueOf(gyroHeading);
    }

    /**
     *
     */
    public void GatherData() {
        gyroHeading = robot().GetSensorCore().getCurrentHeading();
    }
    
}
