/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core.systems;

import xbot.common.DoubleProperty;

/**
 *
 * @author Alex
 */
public class CollectorCore {
    
    //<editor-fold defaultstate="collapsed" desc="Device values">
    private double requestedDeviceMotorSpeed;
    /**
     * Get the value of requestedDeviceMotorSpeed
     *
     * @return the value of requestedDeviceMotorSpeed
     */
    public double getRequestedDeviceMotorSpeed() {
        return requestedDeviceMotorSpeed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    public DoubleProperty intakeSpeed = new DoubleProperty("intakeSpeed", 0.8);
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public methods">
    public void stop() {
        requestedDeviceMotorSpeed = 0.0;
    }
    
    public void collectDiscsSpeed() {
        requestedDeviceMotorSpeed = intakeSpeed.get();
    }
    //</editor-fold>
}
