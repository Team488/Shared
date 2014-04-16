/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

import xbot.aerialassist.RobotContext;
import xbot.aerialassist.systems.CollectionCore;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 * This represents a Collector on the robot. A collector contains two systems: The 
 * motors which deploy the Collector arm, and rollers that intake or expel a Ball.
 * @author Nikhil
 */
public class Collector extends Loggable {
    
    private CollectorSensorPackage sensors;
    BooleanProperty isManual;
    /**
     *
     * @param collectorName
     */
    public Collector(String collectorName){
        super(collectorName);
        sensors = new CollectorSensorPackage(collectorName);
        isManual = new BooleanProperty(collectorName + ":Manual", false);
        targetScaledPositionProperty = new DoubleProperty(collectorName + ":targetScaledPosition", 0);
        targetScaledPositionProperty.set(0);
    }
    
    public boolean getIsManual()
    {
        return isManual.get();
    }
    
   public void setIsManual(boolean isManual)
   {
       if(this.isManual.get() != isManual) {
           Info("IsManual = " + isManual);
       }
       this.isManual.set(isManual);
       sensors.setIsInManualMode(isManual);
   }
    //<editor-fold defaultstate="collapsed" desc="properties">
    private double collectorRollerMotor;
    /**
     *
     * @return
     */
    public double getCollectorRollerMotor() {
        return collectorRollerMotor;
    }
    
    private double collectorDeployMotor;
    /**
     *
     * @return
     */
    public double getDeployMotor() {
        return collectorDeployMotor;
    }

    /**
     * Set the value of targetDeployState
     *
     * @param targetDeployState new value of targetDeployState
     */
    public void setTargetDeployState(CollectorDeployState targetDeployState) {
        if(targetDeployState == CollectorDeployState.UP)
        {
            setTargetScaledPosition(0);
        }
        else if(targetDeployState == CollectorDeployState.DOWN)
        {
            setTargetScaledPosition(1.0);
        }
        else if(targetDeployState == CollectorDeployState.FIRING)
        {
            setTargetScaledPosition(getSensors().firingPosition.get());
        }
        else if (targetDeployState == CollectorDeployState.SAFE) {
            setTargetScaledPosition(getSensors().safePosition.get());
        }   
        else if (targetDeployState == CollectorDeployState.DURESS) {
            setTargetScaledPosition(getSensors().duressPosition.get());
        }
        
    }
    
    private DoubleProperty targetScaledPositionProperty;
    private double targetScaledPosition = 0;

    /**
     * Get the value of targetScaledPosition
     *
     * @return the value of targetScaledPosition
     */
    public double getTargetScaledPosition() {
        //return targetScaledPositionProperty.get();
        return targetScaledPosition;
    }

    /**
     * Set the value of targetScaledPosition
     *
     * @param targetScaledPosition new value of targetScaledPosition
     */
    public void setTargetScaledPosition(double targetScaledPosition) {
        if (oldTargetScaledPosition != targetScaledPosition)
        {
            Info("ScaledPosition set to: " + targetScaledPosition);
        }
        this.oldTargetScaledPosition = targetScaledPosition;
        targetScaledPositionProperty.set(targetScaledPosition);
        this.targetScaledPosition = targetScaledPosition;
    }

    private double oldTargetScaledPosition = 0;
    //</editor-fold>
    
    /**
     * Gets the sensor package associated with this Collector.
     * @return
     */
    public CollectorSensorPackage getSensors()
    {
        return sensors;
    }
    
    /**
     * Moves the rollers in such a way as to intake balls from the field.
     */
    public void intakeBalls() {
        collectorRollerMotor = CollectionCore.rollerCollectSpeed.get();
    }
    
    /**
     * Stops the collection rollers.
     */
    public void stopRoller() {
        collectorRollerMotor = 0;
    }
    /**
     * Moves the rollers in such a way as to expel balls from the robot onto the field.
     * This will only expel balls that are still captured by the collector, and not yet
     * passed to the catapult.
     */
    public void ejectBalls() {
        collectorRollerMotor = -CollectionCore.rollerCollectSpeed.get();
    }
 
    /**
     * Halt the motion of the arm.
     */
    public void stopDeploy() {
        DangerousSetDeployMotor(0);
    }
    
    /**
     * Move the arm down (deployed)
     */
    public void goToDeployedState() {
        double potentialSpeed = CollectionCore.downDeploySpeed.get();
        SafelyOperateArm(potentialSpeed);
    }
    
    /**
     * Move the arm to the firing position - just open enough to let the catapult launch the ball.
     */
    public void goToFiringState() {
        
        double potentialSpeed = CollectionCore.upDeploySpeed.get();
        if (sensors.getIsInFiringPosition())
        {
            potentialSpeed = 0;
        }
        else if (!sensors.shouldMoveOutToGetToFiringPosition())
        {
            potentialSpeed = -potentialSpeed;
        }
        
        SafelyOperateArm(potentialSpeed);
    }
    
    /**
     * Move the collection arm to a vertical/retracted/up state.
     */
    public void goToUpState() {
        double potentialSpeed = CollectionCore.upDeploySpeed.get();
        SafelyOperateArm(potentialSpeed);      
    }    
    
    private void SafelyOperateArm(double potentialSpeed)
    {
        if (sensors.getIsFullyDown())
        {
            potentialSpeed = CommonUtils.CoerceDouble(potentialSpeed, 0, -1);
        }
        if (sensors.getIsFullyUp())
        {
            potentialSpeed = CommonUtils.CoerceDouble(potentialSpeed, 1, 0);
        }
        
        RespectLimitSwitches(potentialSpeed);
    }
    
    public void setDeployMotor(double speed){
        SafelyOperateArm(speed);
    }
    
    public void DangerousSetDeployMotor(double speed)
    {
        RespectLimitSwitches(speed);
    }
    
    private void RespectLimitSwitches(double potentialSpeed)
    {
        if (RobotContext.Get().getCollectionCore().useLimitSwitchesForArms.get())
        {
            if (sensors.isArmAtLowerLimit())
            {
                potentialSpeed = CommonUtils.CoerceDouble(potentialSpeed, 0, -1);
            }
            if (sensors.isArmAtUpperLimit())
            {
                potentialSpeed = CommonUtils.CoerceDouble(potentialSpeed, 1, 0);
            }
        }
        collectorDeployMotor = potentialSpeed;
    }
}
