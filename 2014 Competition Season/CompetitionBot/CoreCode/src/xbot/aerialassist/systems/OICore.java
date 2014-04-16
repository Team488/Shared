/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.common.CommonTools;
import xbot.common.math.XYPair;
import xbot.common.properties.StringProperty;

/**
 * This represents all the inputs (like joysticks) and outputs (like LEDs) on the Operator Interface.
 * @author Alex
 */
public class OICore {
// <editor-fold defaultstate="collapsed" desc="properties">    
        private double leftJoyX;

    /**
     * Get the vector of the joystick.
     * @return 
     */
    public XYPair getLeftJoyVector() {
        return new XYPair(this.leftJoyX, this.leftJoyY);
    }
    
    /**
     * Get the value of leftJoyX
     *
     * @return the value of leftJoyX
     */
    public double getLeftJoyX() {
        return leftJoyX;
    }

    /**
     * Set the value of leftJoyX
     *
     * @param leftJoyX new value of leftJoyX
     */
    public void setLeftJoyX(double leftJoyX) {
        this.leftJoyX = leftJoyX;
    }
    
    private double leftJoyY;

    /**
     * Get the value of leftJoyY
     *
     * @return the value of leftJoyY
     */
    public double getLeftJoyY() {
        return leftJoyY;
    }

    /**
     * Set the value of leftJoyY
     *
     * @param leftJoyY new value of leftJoyY
     */
    public void setLeftJoyY(double leftJoyY) {
        this.leftJoyY = leftJoyY;
    }
    
        private double rightJoyX;

    /**
     * Get the value of rightJoyX
     *
     * @return the value of rightJoyX
     */
    public double getRightJoyX() {
        return rightJoyX;
    }

    /**
     * Set the value of rightJoyX
     *
     * @param rightJoyX new value of rightJoyX
     */
    public void setRightJoyX(double rightJoyX) {
        this.rightJoyX = rightJoyX;
    }

    
    private double rightJoyY;

    /**
     * Get the value of rightJoyY
     *
     * @return the value of rightJoyY
     */
    public double getRightJoyY() {
        return rightJoyY;
    }

    /**
     * Set the value of rightJoyY
     *
     * @param rightJoyY new value of rightJoyY
     */
    public void setRightJoyY(double rightJoyY) {
        this.rightJoyY = rightJoyY;
    }
    
    private double operatorJoyX;

    /**
     * Get the value of testJoyX
     *
     * @return the value of testJoyX
     */
    public double getOperatorJoyX() {
        return operatorJoyX;
    }

    /**
     * Set the value of testJoyX
     *
     * @param testJoyX new value of testJoyX
     */
    public void setOperatorJoyX(double testJoyX) {
        this.operatorJoyX = testJoyX;
    }
    private double operatorJoyY;

    /**
     * Get the value of testJoyY
     *
     * @return the value of testJoyY
     */
    public double getOperatorJoyY() {
        return operatorJoyY;
    }

    /**
     * Set the value of testJoyY
     *
     * @param testJoyY new value of testJoyY
     */
    public void setOperatorJoyY(double testJoyY) {
        this.operatorJoyY = testJoyY;
    }
    
    private boolean isRedAlliance;

    /**
     * Get the value of isRedAlliance
     *
     * @return the value of isRedAlliance
     */
    public boolean isRedAlliance() {
        return isRedAlliance;
    }

    /**
     * Set the value of isRedAlliance
     *
     * @param isRedAlliance new value of isRedAlliance
     */
    public void setIsRedAlliance(boolean isRedAlliance) {
        this.isRedAlliance = isRedAlliance;
    }
    
    private boolean shouldIntake;
    
    /**
     * True if we should intake with the rollers.
     * @param shouldIntake 
     */
    public void setRollerShouldIntake(boolean shouldIntake) {
        this.shouldIntake = shouldIntake;
    }
    
    public boolean getRollerShouldIntake() {
        return this.shouldIntake;
    }
    
    private boolean shouldExpel;
    
    /**
     * True if we should expel with the rollers.
     * @param shouldExpel 
     */
    public void setRollerShouldExpel(boolean shouldExpel) {
        this.shouldExpel = shouldExpel;
    }
    
    public boolean getRollerShouldExpel() {
        return this.shouldExpel;
    }
    
        private int LeftAutoSwitch;

    /**
     * Get the value of LeftAutoSwitch
     *
     * @return the value of LeftAutoSwitch
     */
    public int getLeftAutoSwitch() {
        return LeftAutoSwitch;
    }

    /**
     * Set the value of LeftAutoSwitch
     *
     * @param LeftAutoSwitch new value of LeftAutoSwitch
     */
    public void setLeftAutoSwitch(int LeftAutoSwitch) {
        this.LeftAutoSwitch = LeftAutoSwitch;
        UpdateDS();
    }
    private int RightAutoSwitch;

    /**
     * Get the value of RightAutoSwitch
     *
     * @return the value of RightAutoSwitch
     */
    public int getRightAutoSwitch() {
        return RightAutoSwitch;
    }

       
    /**
     * Set the value of RightAutoSwitch
     *
     * @param RightAutoSwitch new value of RightAutoSwitch
     */
    public void setRightAutoSwitch(int RightAutoSwitch) {
        this.RightAutoSwitch = RightAutoSwitch;
        UpdateDS();
    }
    private int MiddleAutoSwitch;

    /**
     * Get the value of MiddleAutoSwitch
     *
     * @return the value of MiddleAutoSwitch
     */
    public int getMiddleAutoSwitch() {
        return MiddleAutoSwitch;
    }

    /**
     * Set the value of MiddleAutoSwitch
     *
     * @param MiddleAutoSwitch new value of MiddleAutoSwitch
     */
    public void setMiddleAutoSwitch(int MiddleAutoSwitch) {
        this.MiddleAutoSwitch = MiddleAutoSwitch;
        UpdateDS();
    }
    
    double lastUpdateTime = -10000;
    private StringProperty autoDisplay = new StringProperty("AutoDisplay", "???");
    
    private void UpdateDS()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastUpdateTime) > 5000)
        {
            autoDisplay.set("" + LeftAutoSwitch + "" + MiddleAutoSwitch + "" + RightAutoSwitch);
            lastUpdateTime = CommonTools.Get().time.GetMarker();
        }
    }

        
    // </editor-fold>

    
}
