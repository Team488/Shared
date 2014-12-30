package xbot.aerialassist.collection;



import xbot.common.CommonTools;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 * The collector sensor package gives a Collector a small layer of abstraction between it
 * and whatever sensors we use on the robot to determine where the collector arm is.
 * This way, the Collector & its associated workers are insulated from changes in how
 * the sensors are implemented on the robot (limit switches, force sensors, encoders, whatever).
 * @author John
 */
public class CollectorSensorPackage extends Loggable {
    
    private boolean moveOutForFiring;
    public DoubleProperty upVoltage;
    public DoubleProperty downVoltage; 
    public DoubleProperty currentVoltage;
    public DoubleProperty scaledPosition;
    public DoubleProperty firingPosition;
    public DoubleProperty firingPositionLowerLimit;
    public DoubleProperty safePosition;
    public DoubleProperty duressPosition;
    public DoubleProperty duressPositionLowerLimit;
    
    public BooleanProperty upLimitSwitch;
    public BooleanProperty downLimitSwitch;

    
    private boolean upLimitSwitchPressed;
    private boolean downLimitSwitchPressed;
    
    private boolean inBadState;
    public boolean isManual = false;
    
    private double localScaledPosition;
    private double localVoltage;
    
    private DoubleProperty ballRange = new DoubleProperty("CollectorBallDetectionRange", 7.0);
    
    private RangeSensor ballSensor;
   
    private boolean intakeBalls;
    private boolean expelBalls;
    
    private DoubleProperty armConsideredUpAt = new DoubleProperty("CollectorUpAt", 0.1);
    private DoubleProperty armConsideredDownAt = new DoubleProperty("CollectorDownAt", 0.9);
    
    /**
     *
     * @param position  Front or Back
     */
    public CollectorSensorPackage(String position)
    {
        super(position+"CollectorSensorPackage");
        // We expect to start arms up
        moveOutForFiring = true;
        scaledPosition  = new DoubleProperty(position + ":scaledPos", 0);
        upVoltage = new DoubleProperty(position + ":upVoltage", 1);
        downVoltage = new DoubleProperty(position + ":downVoltage", 4);
        firingPosition = new DoubleProperty(position + ":firingPosition", 0.6);
        firingPositionLowerLimit = new DoubleProperty(position+":firingPositionLowerLimit", 0.5);
        duressPosition = new DoubleProperty(position+" :duressPosition",0.6);
        duressPositionLowerLimit = new DoubleProperty(position+" :duressPositionLowerLimit",0.5);
        safePosition = new DoubleProperty(position + ":safePosition", 0.6);
        currentVoltage = new DoubleProperty(position + ":currentVoltage", 0);
        upLimitSwitch = new BooleanProperty(position + ":upLimitSwitch", false);
        downLimitSwitch = new BooleanProperty(position + ":downLimitSwitch", false);
        
        upLimitSwitchPressed = false;
        downLimitSwitchPressed = false;
        
        inBadState = false;
        localScaledPosition = 0;
        localVoltage = 0;
        ballSensor = new RangeSensor(position);
    }
    
    boolean pidEnabled = true;
    boolean manualUp = false;
    boolean manualDown = false;
    
    public void setPidEnabled(boolean enabled)
    {
        pidEnabled = enabled;
    }
    
    public boolean isPidEnabled()
    {
        return pidEnabled;
    }
    
    public void setManualUp(boolean up)
    {
        manualUp = up;
    }
    
    public boolean isManualUp()
    {
        return manualUp;
    }
    
    public void setIsInManualMode(boolean manual)
    {
        isManual = manual;
    }
    
    public void setManualDown(boolean down)
    {
        manualDown = down;
    }
    
    public boolean isManualDown()
    {
        return manualDown;
    }
    
    public void setManualIntake(boolean intake)
    {
        intakeBalls = intake;
    }
    
    public boolean isManualIntake()
    {
        return intakeBalls;
    }
    
    public void setManualExpel(boolean expel)
    {
        expelBalls = expel;
    }
    
    public boolean isManualExpel()
    {
        return expelBalls;
    }
    
    public RangeSensor GetRangeSensorTESTONLY()
    {
        return ballSensor;
    }
    
    private double lastVoltageTime = -10000;
    public void SetCurrentVoltage(double voltage)
    {
        localVoltage = voltage;
        
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastVoltageTime) > 2500)
        {
            currentVoltage.set(voltage);
            lastVoltageTime = CommonTools.Get().time.GetMarker();
            
            // recalculate current position for debug purposes, this will send
            // it to the smart dashboard
            GetCollectorPosition();
        }
    }
    
    public void SetBallSensorVoltage(double voltage)
    {
        ballSensor.SetVoltage(voltage);
    }
    
    public boolean IsBallInArm()
    {        
        double range = ballSensor.GetRange();
        if (range < ballRange.get())
        {
            return true;
        }
        return false;
    }
    
    private double lastTime = -10000;
    private void sendScaledPositionToDashboard()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastTime) > 5000)
        {
            scaledPosition.set(localScaledPosition);
            lastTime = CommonTools.Get().time.GetMarker();
        }
    }
    
    public void setUpVoltageTESTONLY(double voltage)
    {
        upVoltage.set(voltage);
    }
    
    public void setDownVoltageTESTONLY(double voltage)
    {
        downVoltage.set(voltage);
    }
    
    public void SetScaledPositionTESTONLY(double scaledPos)
    {
        localScaledPosition = scaledPos;
        
        //also need ot set the voltage
        //(localScaled * (down-up) + up = local
        localVoltage = scaledPos * (downVoltage.get() - upVoltage.get()) + upVoltage.get();
        currentVoltage.set(localVoltage);
        
        sendScaledPositionToDashboard();
    }
    
    
    private double delayMarker = -100000;
    private double upValue = 1;
    private double downValue = 2;
    
    public double GetUpVoltageTESTONLY()
    {
        return upValue;
    }
    
    public double GetDownVoltageTESTONLY()
    {
        return downValue;
    }
    
    public double GetCollectorPosition()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(delayMarker) > 2500)
        {
            upValue = upVoltage.get();
            downValue = downVoltage.get();
            delayMarker = CommonTools.Get().time.GetMarker();
        }
        
        if ((downValue - upValue) == 0)
        {
            if (inBadState == false)
            {
                Warning("The difference between down and up voltages is 0. The arm cannot be safely operated.");
                // TODO - somehow prevent the arm motor from being driven unless given explicit permission.
            }
            inBadState = true;
            localScaledPosition = 0;
        }
        else
        {
            localScaledPosition = (localVoltage - upValue) / (downVoltage.get() - upValue); 
        }
        
        //coerce the value
        localScaledPosition = CommonUtils.CoerceDouble(localScaledPosition, 1, 0);
        
        sendScaledPositionToDashboard();
        
        return localScaledPosition;
    }
    /**
     * Is the collector completely deployed/extended?
     * @return
     */
    public boolean getIsFullyDown()
    {   
        if (isManual)
        {
            return true;
        }
        
        if(localScaledPosition > armConsideredDownAt.get()) // .2 < sPos and sPos >= .5 
        {
            return true; // the collector is down
        }
        else
        {
            return false || inBadState;
        }
        
    }
    
    /**
     * Is the collector in Firing Position?
     * @return
     */
    public boolean getIsInFiringPosition() {
        if (isManual)
        {
            return true;
        }
        return this.getPastLowerLimit(firingPositionLowerLimit);
    }
    
    public boolean getIsInDuressPosition() {
        if (isManual)
        {
            return true;
        }
        return this.getPastLowerLimit(duressPositionLowerLimit);
    }
    
    public boolean getIsInSafePosition() {
        
        if (isManual)
        {
            return true;
        }
        
        double distanceFromSafe = Math.abs(
                this.localScaledPosition
                - this.safePosition.get());
        
        return distanceFromSafe <= 0.1;
    }
    
    private boolean getPastLowerLimit(DoubleProperty property) {
        
        if(localScaledPosition >= property.get())
        {
            return true;
        }
        else
        {
            return false || inBadState;
        }
    }
    /**
     * Can we fire the catapult without interference from a Collector?
     * @return
     */
    public boolean GetIsSafeToFire()
    {
        if (isManual)
        {
            return true;
        }
        
        return getIsInFiringPosition();
    }
    
    public boolean GetIsSafeToFireUnderDuress()
    {
        if (isManual)
        {
            return true;
        }
        
        return getIsInDuressPosition();
    }
    /**
     * Is the collector closed?
     * @return
     */
    public boolean getIsFullyUp()
    {
        if (isManual)
        {
            return true;
        }
        
        if(localScaledPosition <= armConsideredUpAt.get()) // 0 <= sPos <= .2
        {
            return true; // actually means up
        } 
        else
        {
            return false || inBadState;
        }
    }
    
    /**
     * If I want to move to firing position, should I move the collector in (closed)
     * or out (deployed)?
     * @return
     */
    public boolean shouldMoveOutToGetToFiringPosition()
    {
        if (isManual)
        {
            return true;
        }
        
        return moveOutForFiring;
    }
   
    public void printScaledPosition()
    { //just for testing purposes
        Info("Scaled Position: " + scaledPosition.get() + ". CurrentVoltage: " + currentVoltage.get() + ". CollectorPosition: " + GetCollectorPosition());
    }
    
    public void setLimitSwitches(boolean up, boolean down)
    {
        upLimitSwitchPressed = up;
        downLimitSwitchPressed = down;
        setLimitSwitchProperties();
    }
    
    public boolean isArmAtUpperLimit()
    {
        return upLimitSwitchPressed;
    }
    
    public boolean isArmAtLowerLimit()
    {
        return downLimitSwitchPressed;
    }
    
    
    
    private double limitSwitchTime = -10000;
    private void setLimitSwitchProperties()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(limitSwitchTime) > 2500)
        {
            upLimitSwitch.set(upLimitSwitchPressed);
            downLimitSwitch.set(downLimitSwitchPressed);
            
            limitSwitchTime = CommonTools.Get().time.GetMarker();
        }
    }
}