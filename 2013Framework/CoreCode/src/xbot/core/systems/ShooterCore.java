/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core.systems;

import xbot.common.CommonTools;
import xbot.common.DoubleProperty;
import xbot.common.SystemBase;
import xbot.common.wpilibextensions.PIDController;
import xbot.common.wpilibextensions.PIDOutput;
import xbot.common.wpilibextensions.PIDSource;

/**
 *
 * @author Alex
 */
public class ShooterCore extends SystemBase implements PIDSource, PIDOutput{
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    public DoubleProperty outerShooterSafeSpeedP =
            new DoubleProperty("outerShooterSafeSpeed", 0.5);
    public DoubleProperty innerShooterSafeSpeedP =
            new DoubleProperty("innerShooterSafeSpeed", 0.4);
    
    public DoubleProperty outerShooterManualSpeedP =
            new DoubleProperty("outerShooterManualSpeed", 0.8);
    public DoubleProperty innerShooterManualSpeedP =
            new DoubleProperty("innerShooterManualSpeed", 0.5);
    
    public DoubleProperty outerShooterPIDPP =
            new DoubleProperty("outerShooterPIDP", 0.01);
    public DoubleProperty outerShooterPIDIP =
            new DoubleProperty("outerShooterPIDI", 0.0);
    public DoubleProperty outerShooterPIDDP =
            new DoubleProperty("outerShooterPIDD", 1.0);
    public DoubleProperty outerShooterPIDToleranceP =
            new DoubleProperty("outerShooterPIDTolerance", 100);
    
    public DoubleProperty outerShooterTargetRPMP =
            new DoubleProperty("outerShooterTargetRPM", 8000);
    //</editor-fold>
  
    public static class ShootingPowerMode {}
    
    public final static ShootingPowerMode 
            SAFE_POWER = new ShootingPowerMode(),
            MANUAL_POWER = new ShootingPowerMode(),
            PID_POWER = new ShootingPowerMode(),
            STOP = new ShootingPowerMode();
    
    protected ShootingPowerMode powerMode;
    
    // <editor-fold defaultstate="collapsed" desc="Device values">
    private double outerShooterWheelSpeed;
    /**
     * Get the value of outerShooterWheelSpeed
     *
     * @return the value of outerShooterWheelSpeed
     */
    public double getOuterShooterWheelSpeed() {
        return outerShooterWheelSpeed;
    }
    
    private double innerShooterWheelSpeed;
    /**
     * Get the value of innerShooterWheelSpeed
     *
     * @return the value of innerShooterWheelSpeed
     */
    public double getInnerShooterWheelSpeed() {
        return innerShooterWheelSpeed;
    }
    
    private double outerShooterEncoderRate;
    /**
     * Set the value of outerShooterEncoderRate
     *
     * @param outerShooterEncoderRate new value of outerShooterEncoderRate
     */
    public void setOuterShooterEncoderRate(double outerShooterEncoderRate) {
        this.outerShooterEncoderRate = outerShooterEncoderRate;
    }
    protected double getOuterShooterEncoderRate() {
        return outerShooterEncoderRate;
    }
    
    private boolean desiredFingerSolonoidExtended;
    /**
     * Get the value of desiredFingerSolonoidExtended
     *
     * @return the value of desiredFingerSolonoidExtended
     */
    public boolean isDesiredFingerSolonoidExtended() {
        return desiredFingerSolonoidExtended;
    }
    
    private boolean fingerRetractedLimitSwitchPressed;
    /**
     * Get the value of fingerRetractedLimitSwitchPressed
     *
     * @return the value of fingerRetractedLimitSwitchPressed
     */
    protected boolean isFingerRetractedLimitSwitchPressed() {
        return fingerRetractedLimitSwitchPressed;
    }
    /**
     * Set the value of fingerRetractedLimitSwitchPressed
     *
     * @param fingerRetractedLimitSwitchPressed new value of
     * fingerRetractedLimitSwitchPressed
     */
    public void setFingerRetractedLimitSwitchPressed(boolean fingerRetractedLimitSwitchPressed) {
        this.fingerRetractedLimitSwitchPressed = fingerRetractedLimitSwitchPressed;
    }
    // </editor-fold>
    
    protected PIDController outerShooterPIDController;
    
    public ShooterCore() 
    {
        super("ShooterCore");
        outerShooterPIDController = new PIDController(
                outerShooterPIDPP.get(), 
                outerShooterPIDIP.get(), 
                outerShooterPIDDP.get(), this, this);

    }
    
    //<editor-fold defaultstate="collapsed" desc="Pid read/write functions">
    public double pidGet() {
        // TODO: the units of this should be RPMs, might need to convert
        Verbose("pidGet:" + outerShooterEncoderRate);
        return outerShooterEncoderRate;
    }
    
    public void pidWrite(double output) {
        Verbose("pidWrite:" + output);
        outerShooterWheelSpeed = output;
    }
    //</editor-fold>
    
    public void extendFinger() {
        desiredFingerSolonoidExtended = true;
    }
    
    public void retractFinger() {
        desiredFingerSolonoidExtended = false;
    }
    
    public boolean isFingerDefinitelyRetracted() {
        return isFingerRetractedLimitSwitchPressed();
    }
    
    public void setShootingPowerMode(ShootingPowerMode newMode) {
        if(powerMode == PID_POWER && powerMode != newMode) {
            outerShooterPIDController.disable();
        }
        
        powerMode = newMode;
        if(powerMode == STOP) {
            innerShooterWheelSpeed = 0;
            outerShooterWheelSpeed = 0;
        } else if (powerMode == MANUAL_POWER) {
            innerShooterWheelSpeed = innerShooterManualSpeedP.get();
            outerShooterWheelSpeed = outerShooterManualSpeedP.get();
        } else if (powerMode == SAFE_POWER) {
            innerShooterWheelSpeed = innerShooterSafeSpeedP.get();
            outerShooterWheelSpeed = outerShooterSafeSpeedP.get();
        } else if (powerMode == PID_POWER) {
            // update PID values
            outerShooterPIDController.setPID(
                outerShooterPIDPP.get(), 
                outerShooterPIDIP.get(), 
                outerShooterPIDDP.get());        
            outerShooterPIDController.setAbsoluteTolerance(
                    outerShooterPIDToleranceP.get());
            
            // set target speed
            outerShooterPIDController.setSetpoint(outerShooterTargetRPMP.get());
            
            // ensure controller is enabled
            outerShooterPIDController.enable();
        }
    }
    
    // TODO: work out if this should return in-determinate for fixed power speed
    // modes?
    public Boolean isAtSpeed() {
        // TODO: return correct value depending on shooting mode
        if(powerMode == STOP) {
            return Boolean.TRUE;
        } else if (powerMode == PID_POWER) {
            return Boolean.valueOf(outerShooterPIDController.onTarget());
        } else {
            return null;
        }
    }
}
