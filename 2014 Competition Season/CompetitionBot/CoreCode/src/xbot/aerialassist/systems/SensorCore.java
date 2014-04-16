/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.aerialassist.odometry.FollowWheelOdometry;
import xbot.common.CommonTools;
import xbot.common.properties.DoubleProperty;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;

/**
 * This represents all the sensors related to robot motion across the field.
 * Primarily, it's focused on the gyro feedback from the IMU as well as the follow wheels.
 * @author Alex
 */
public class SensorCore extends Loggable {
    private boolean hasInitGyroAngle;
    // Current angle from the gyro
    private double CurrentAngle = 0;
    // Delta Angle
    private double DeltaAngle;
    // Angle adjustor for the gyrosensor because of field orientation (front = 90)
    private double GyroOffset = 90;
    
    // Store the raw heading so that we can multiply by gain and add offset only
    // on heading retrieval
    // Note: The robot starts turned 90 degrees in order to fire the catapult immediately.
    private double RawHeading = 90;
    // The initial current heading is the rawheading plus the offset. 
    // This is set to the offset at initialization
    private double CurrentHeading;
    // The IMU offset allows the range of gyro values to be positive
    // Without the offset, the IMU heading range is [-180,180]
    private double IMUOffset = 180;
    
    private void setCurrentHeading(double newHeading) {
        
        this.CurrentHeading = newHeading;
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastTime) > 5000) {
            currentHeadingProperty.set(newHeading);
            lastTime = CommonTools.Get().time.GetMarker();
        }
    }
    
    /**
     *
     */
    public FollowWheelOdometry odometry;

    //Get the direct angle from the gyroSensor
    private DoubleProperty currentHeadingProperty = new DoubleProperty("currentHeading", GyroOffset);
    private BooleanProperty enableOdometry = new BooleanProperty("EnableOdometry", false);
    private DoubleProperty defaultOrientation = new DoubleProperty("DefaultOrientation", 90.0);
    public DoubleProperty startingOrientation = new DoubleProperty("StartingOrientation", 180);
    
    private double oldLeftWheel;
    private double oldRightWheel;
    private double oldCenterWheel;
    
    
    /**
     *
     */
    public SensorCore() {
        super("SensorCore");
        Info("Constructing, gyroOffset:" + GyroOffset);
        setCurrentHeading(GyroOffset + 90);
        odometry = new FollowWheelOdometry();
        hasInitGyroAngle = false;
    }
    
    public void resetCurrentAngleTESTONLY() {
        this.CurrentAngle = 0;
    }
    
    /**
     *  Returns the orientation that the robot starts the match at. The robot assumes that it is facing
     * away from the driver at match start.
     * @return
     */
    public double GetDefaultOrientation()
    {
        //Verbose("robot got default orientation");
        return defaultOrientation.get();
    }
    

    /**
     * This method is where the Robot places all the sensor data for processing.
     * @param gyroHeading   Heading from the IMU.
     * @param leftWheelTicks    Encoder ticks from the left wheel.
     * @param rightWheelTicks   Encoder ticks from the right wheel.
     * @param centerWheelTicks  Encoder ticks from the center wheel.
     */
    public void DepositLocationInformationFromRobot(double gyroHeading, double leftWheelTicks, double rightWheelTicks, double centerWheelTicks)
    {
        boolean throwAwayGyroValue = false;
        
        if (enableOdometry.get())
        {
            //Verbose("Odometry is enabled");
            if (
                (leftWheelTicks == oldLeftWheel) && 
                (rightWheelTicks == oldRightWheel) && 
                (centerWheelTicks == oldCenterWheel))
            {
                throwAwayGyroValue = true;
                //Verbose("threw away Gyro values");
            }

            oldLeftWheel = leftWheelTicks;
            oldRightWheel = rightWheelTicks;
            oldCenterWheel = centerWheelTicks;
        }
        
        if (!hasInitGyroAngle){
            this.CurrentAngle = gyroHeading;
            hasInitGyroAngle = true;
        }
        setGyroAngles(gyroHeading, throwAwayGyroValue);
        
        // Update the WheelEncoders with the new data
        odometry.FeedSensorData(getCurrentHeading(), leftWheelTicks, rightWheelTicks, centerWheelTicks);
    }
    

    
    /**
     *This method takes the Gyro Angle from the sensor and converts it to a raw
     *heading irrespective of the angle on the sensor.
     *Essentially takes the delta of the current and past angle and increments
     *it on the Raw Heading
     * @param gyroAngleIn
     * @param throwAwayGyroValue
     */
    public void setGyroAngles(double gyroAngleIn, boolean throwAwayGyroValue){
        //Find the delta angle for gyro
        DeltaAngle = gyroAngleIn - CurrentAngle;
        CurrentAngle = gyroAngleIn;
        //Set the new raw heading
        
        if (Math.abs(DeltaAngle) > 180 && DeltaAngle < 0) {
            DeltaAngle = 360 - Math.abs(DeltaAngle);
        }
        else if (Math.abs(DeltaAngle) > 180 && DeltaAngle > 0) {
            DeltaAngle = -1 * (360 - Math.abs(DeltaAngle));
        }
        
        if (throwAwayGyroValue == false)
        {
            RawHeading = RawHeading + DeltaAngle;
        }
        this.updateCurrentHeading();
    }
    
    
    double lastTime = -10000;
    // This method takes the raw heading and adds any gain or offset if the 
    // sensor output needs to be adjusted. It also bounds the heading at [0,360)
    private void updateCurrentHeading() {
        double newHeading = RawHeading + GyroOffset;
        if (Math.abs(newHeading) > 360) {
            newHeading = newHeading % 360;
        }
        if (newHeading < 0) {
            newHeading = newHeading + 360;
        }
        
        // Update the SmartDashboard occasionally, so we can at least see if it's
        // generically working.
        
            setCurrentHeading(newHeading);
            
    }
    
    /**
     * Returns the robot's current field-oriented heading.
     * @return
     */
    public double getCurrentHeading() {
        //Verbose("getCurrentHeading returns: " + CurrentHeading);
        return CurrentHeading;
    }
    
    /**
     * This resets the heading (for example, if there is drift) back to the default orientation.
     */
    public void resetHeading() {
        RawHeading = 0;
        setCurrentHeading(GyroOffset);
        Info("has reset Gyro heading");
        lastResetTime = CommonTools.Get().time.GetMarker();
    }
    
    private double lastResetTime = -1000;
    /**
     * This returns whether or not the heading was reset recently.
     * @return
     */
    public boolean headingWasResetRecently()
    {
        if(CommonTools.Get().time.GetElapsedMSecFromMarker(lastResetTime) < 1000)
        {
            return true;
        }
        return false;
    }
}
