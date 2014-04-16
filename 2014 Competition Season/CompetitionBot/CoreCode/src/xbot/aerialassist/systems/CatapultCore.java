/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.systems;

import xbot.aerialassist.collection.RangeSensor;
import xbot.common.CommonTools;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;


/**
 * Represents the catapult mechanism on the robot.
 * This mechanism is fairly simple overall - it only manipulates one motor and a few sensors.
 * @author Ken
 */
public class CatapultCore extends Loggable{
   
    //<editor-fold desc="properties" defaultstate="collapsed">
    private RangeSensor ballSensor;
    
    public DoubleProperty catapultIsCockedThreshold = 
            new DoubleProperty("catapultIsCockedThreshold", 0.05);
    
    public DoubleProperty catapultCockedPosition = new DoubleProperty("catapultCockedPosition", 
            0.0);
    
    public DoubleProperty catapultLoadPosition = new DoubleProperty("CatapultSafeLoadPosition", 0.2);
    
    public BooleanProperty shouldCockCatapult = new BooleanProperty("shouldCockCatapult", false);
    
    public DoubleProperty catapultPositionOffset = 
            new DoubleProperty("catapultPositionOffset", 0.0);
    public DoubleProperty catapultPositionScale = 
            new DoubleProperty("catapultPositionScale", 0.2);
    double internalScaledCatapultPosition = 0;
    
    /**
     *
     */
    public DoubleProperty catapultCockSpeed = 
        new DoubleProperty("catapultCockSpeed", 0.65);
    
    /**
     *
     */
    public DoubleProperty catapultFireSpeed = 
        new DoubleProperty("catapultFireSpeed", 1.0);
    
    /**
     *
     */
    public DoubleProperty catapultStopSpeed =
        new DoubleProperty("catapultStopSpeed", 0.0);
        
    private double catapultMotor;
    
    private double lastTime = -5000;
    
    /**
     * 
     * @return the reading the catapult sensor will have when it is perfectly
     *  cocked.
     */
    public double getCatapultCockedPosition() {
        return catapultCockedPosition.get();
    }
    
    
    DoubleProperty catapultCurrentScaledPosition = 
            new DoubleProperty("catapultCurrentScaledPosition", 
            getCatapultCockedPosition());

    /**
     * Get the value of catapultScaledPosition. 
     *
     * @return the value of catapultScaledPosition where 0 is just fired and 1 is 
     * perfectly cocked
     */
    public double getCatapultScaledPosition() {
        return internalScaledCatapultPosition;
    }

    /**
     * Set the value of catapultScaledPosition
     *
     * @param catapultScaledPosition new value of catapultScaledPosition
     */
    public void setCatapultScaledPositionTESTONLY(double catapultScaledPosition) {
        this.internalScaledCatapultPosition = catapultScaledPosition;
        this.catapultCurrentScaledPosition.set(this.internalScaledCatapultPosition);
    }

    /**
     *
     * @return
     */
    public boolean isCatapultCocked()
    {
        double current = this.getCatapultScaledPosition();
        
        double maxCockedPosition = this.getCatapultCockedPosition() + catapultIsCockedThreshold.get();
        
        return current <= maxCockedPosition;
    }
    
    private boolean ejectorSolenoidPunching;

    /**
     * Get the value of ejectorSolenoidPunching
     *
     * @return the value of ejectorSolenoidPunching
     */
    public boolean isEjectorSolenoidPunching() {
        return ejectorSolenoidPunching;
    }

    /**
     * Set the value of ejectorSolenoidPunching
     *
     * @param ejectorSolenoidPunching new value of ejectorSolenoidPunching
     */
    public void setEjectorSolenoidPunching(boolean ejectorSolenoidPunching) {
        this.ejectorSolenoidPunching = ejectorSolenoidPunching;
    }

           
    

    /**
     * Get the value of catapultMotor
     *
     * @return the value of catapultMotor
    */
    public double getCatapultMotor() {
        return catapultMotor;
    }
    
    private DoubleProperty ballRange = new DoubleProperty("CatapultBallDetectionRange", 7);
    
    ///</editor-fold>
    
    /**
     *
     */
    public CatapultCore() {
        super("CatapultCore");
        Info("Initializing");
        ballSensor = new RangeSensor("catapult");
    }
    
    
    public void setCatapultPositionVoltage(double voltage) {
        double calcuatedValue = voltage * catapultPositionScale.get() +
                catapultPositionOffset.get();
        // wrap around 1 back to zero
        calcuatedValue = calcuatedValue % 1.00001;
        
        // protect against negative numbers as well, should always return [0,1]
        internalScaledCatapultPosition = Math.max(0, calcuatedValue);
        
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastTime) > 5000)
        {
            this.catapultCurrentScaledPosition.set(internalScaledCatapultPosition);
            lastTime = CommonTools.Get().time.GetMarker();
        }
    }
    
    private boolean oldBallInRange;
    private double ballStartedInRangeAtThisTime;
    public void SetBallSensorVoltage(double voltage)
    {
        ballSensor.SetVoltage(voltage);
        
        boolean ballInCatapult = IsBallInCatapult();
        
        if (ballInCatapult && !oldBallInRange)
        {
            oldBallInRange = true;
            ballStartedInRangeAtThisTime = CommonTools.Get().time.GetMarker();
        }
        else if (!ballInCatapult)
        {
            oldBallInRange = false;
        }
    }
    
    private DoubleProperty settleTime = new DoubleProperty("CatapultBallSettleTime", 100);
    
    public boolean IsBallSettled()
    {
        if (IsBallInCatapult() && 
                CommonTools.Get().time.GetElapsedMSecFromMarker(ballStartedInRangeAtThisTime) > settleTime.get())
        {
            return true;
        }
        return false;
    }
    
    public boolean IsBallInCatapult()
    {
        if (ballSensor.GetRange() < ballRange.get())
        {
            return true;
        }
        return false;
    }
    
    public boolean IsCatapultReadyToLoad()
    {
        // If we are at the CatapultSafeLoadPosition or below, the catapult is ready to load.
        return CommonUtils.InRange(internalScaledCatapultPosition, catapultLoadPosition.get(), 0);
    }
    
    /**
     * Set the catapult to cocking speed.
     */
    public void cockCatapult(){
        catapultMotor = catapultCockSpeed.get();
    }
    
    /**
     * Set the catapult to Fire speed.
     */
    public void fireCatapult(){
        catapultMotor = catapultFireSpeed.get();
    }
    
    /**
     * Stop the catapult.
     */
    public void stopCatapult(){
        catapultMotor = catapultStopSpeed.get();
    }
    
    /**
     * Set the motor speed.
     * @param value 
     */
    public void setCatapultMotorSpeed(double value) {
        catapultMotor = value;
    }
    
    public void ejectBall(){
        ejectorSolenoidPunching = true;
    }
    
    public void retractEjector(){
        ejectorSolenoidPunching = false;
        
    }
    
    private double startedFiringAtTime = -10000;
    
    public void SetIsFiringFlag()
    {
        Info("IsFiring flag has been set!");
        startedFiringAtTime = CommonTools.Get().time.GetMarker();
    }
    
    public boolean getIsFiring()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(startedFiringAtTime) > firingTime.get())
        {
            return false;
        }
        return true;
    }
    
    public static DoubleProperty firingTime = new DoubleProperty("firingTime", 300);

}
