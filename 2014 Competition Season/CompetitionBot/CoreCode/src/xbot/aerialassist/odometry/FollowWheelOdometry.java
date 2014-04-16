/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.odometry;

import xbot.aerialassist.RobotContext;
import xbot.common.CommonTools;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;

/**
 * This class encapsulates everything having to do with using the follow wheels.
 * It should be able to answer the following questions:
 * - Where am I (relative to where I started the match?)
 * - How fast am I moving? (relative to the field)
 * - How fast am I moving? (relative to the robot)
 * - Is the robot moving at all?
 * @author John
 */
public class FollowWheelOdometry extends Loggable {
    
    private WheelEncoder leftWheel;
    private WheelEncoder rightWheel;
    private WheelEncoder centerWheel;
    
    private XYPair[] robotOrientedVelocityHistory;
    private XYPair[] fieldOrientedVelocityHistory;
    private final int VELOCITY_HISTORY_LENGTH = 10;
    
    private XYPair fieldOrientedPosition;
    private XYPair robotOrientedPosition;
    
    private double lastUpdateTime;
    private double timeSpanInMs;
    
    private DoubleProperty leftWheelGain = 
            new DoubleProperty("LeftFollowWheelGain", 1.0);
    private DoubleProperty rightWheelGain = 
            new DoubleProperty("RightFollowWheelGain", 1.0);
    private DoubleProperty centerWheelGain = 
            new DoubleProperty("CenterFollowWheelGain", 1.0);
    
    private DoubleProperty samplesToAverage = 
            new DoubleProperty("VelocitySamplesToAverage", 5.0);
    private DoubleProperty sampleDecay = 
            new DoubleProperty("VelocityAverageDecayValue", 1.0);
    
    /**
     *
     */
    public FollowWheelOdometry()
    {
        super("FollowWheelOdometry");
        
        leftWheel = new WheelEncoder("Left", leftWheelGain);
        rightWheel = new WheelEncoder("Right", rightWheelGain);
        centerWheel = new WheelEncoder("Center", centerWheelGain);
        
        robotOrientedVelocityHistory = new XYPair[VELOCITY_HISTORY_LENGTH];
        fieldOrientedVelocityHistory = new XYPair[VELOCITY_HISTORY_LENGTH];
    
        fieldOrientedPosition = new XYPair();
        robotOrientedPosition = new XYPair();
    }
    
    // This is the critical method that must be called every time the robot has new information.
    /**
     *
     * @param robotHeading  Where is the robot pointing? This is assuming 0degrees is straight ahead at match start.
     * @param leftWheelTicks How many encoder ticks have happened since the start of the match on the left follow wheel
     * @param rightWheelTicks How many encoder ticks have happened since the start of the match on the right follow wheel
     * @param centerwheelTicks How many encoder ticks have happened since the start of the match on the center follow wheel
     */
    public void FeedSensorData(double robotHeading, double leftWheelTicks, double rightWheelTicks, double centerwheelTicks)
    {
        // Determine X and Y instantaneous distance change based on data
        double leftDistance = leftWheel.UpdateDistance(leftWheelTicks);
        double rightDistance = rightWheel.UpdateDistance(rightWheelTicks);
        double centerDistance = centerWheel.UpdateDistance(centerwheelTicks);
        
        // Simply assume that any rotation will cancel out with left and right encoders
        double yDistance = (leftDistance + rightDistance) / 2;
        
        CalculateRobotPose(robotHeading, centerDistance, yDistance);
    }
    
    private void UpdateTimeSlice()
    {
        double currentUpdateTime = CommonTools.Get().time.GetMarker();
        timeSpanInMs = currentUpdateTime - lastUpdateTime;
        lastUpdateTime = CommonTools.Get().time.GetMarker();
    }
    
    /**
     * This method is to support unit testing only. DO NOT CALL THIS IN A REAL WORKER!
     * @param xRobotTranslation
     * @param yRobotTranslation
     */
    public void TESTONLY_setManualPosition(double xRobotTranslation, double yRobotTranslation) {
        CalculateRobotPose(90, xRobotTranslation, yRobotTranslation);
    }
    
    private void CalculateRobotPose(double heading, double xRobotTranslation, double yRobotTranslation)
    {
        UpdateTimeSlice();
        XYPair robotRelativePositionVector = new XYPair(xRobotTranslation, yRobotTranslation);
        
        double headingDifferenceFromStart = heading - RobotContext.Get().GetSensorCore().GetDefaultOrientation(); //CommonUtils.GetDefaultOrientation();
        
        XYPair fieldOrientedPositionVector = CommonUtils.rotateVector(xRobotTranslation, yRobotTranslation, headingDifferenceFromStart);
        
        // update positions
        robotOrientedPosition = robotOrientedPosition.add(robotRelativePositionVector);

        fieldOrientedPosition = fieldOrientedPosition.add(fieldOrientedPositionVector);
        
        // determine velocity based on timeslice
        XYPair robotVelocity = robotRelativePositionVector.multiplyScalar(1 / (timeSpanInMs / 1000));
        XYPair fieldVelocity = fieldOrientedPositionVector.multiplyScalar(1 / (timeSpanInMs / 1000));
        
        // update velocity histories
        ShiftAllVelocities(robotOrientedVelocityHistory);
        ShiftAllVelocities(fieldOrientedVelocityHistory);
        robotOrientedVelocityHistory[0] = robotVelocity;
        fieldOrientedVelocityHistory[0] = fieldVelocity;
    }
    
    /** 
     * 
     * @return field oriented position in feet
     */
    public XYPair GetFieldOrientedPosition()
    {
        return fieldOrientedPosition;
    }
    
    /**
     *
     * @param averaged Whether or not to average the velocity. False would return the instantaneous velocity.
     * @return The robot's field-oriented velocity
     */
    public XYPair GetFieldOrientedVelocity(boolean averaged)
    {
        if (averaged == false)
        {
            return fieldOrientedVelocityHistory[0];
        }
        else
        {
            return GetAverageVelocity(fieldOrientedVelocityHistory, (int)samplesToAverage.get());
        }
    }
    
    /**
     *
     * @param averaged Whether or not to average the velocity. False would return the instantaneous velocity.
     * @return The robot's velocity, oriented to itself.
     */
    public XYPair GetRobotOrientedVelocity(boolean averaged)
    {
        if (averaged == false)
        {
            return robotOrientedVelocityHistory[0];
        }
        else
        {
            return GetAverageVelocity(robotOrientedVelocityHistory, (int)samplesToAverage.get());
        }
    }
    
    private XYPair GetAverageVelocity(XYPair[] arrayToAverage, int howManyToAverage)
    {
        double weight = 1;
        
        if (howManyToAverage > VELOCITY_HISTORY_LENGTH)
        {
            Warning("Was asked to average "+ howManyToAverage + " samples, but this exceeds the available number of samples: " + VELOCITY_HISTORY_LENGTH);
            howManyToAverage = VELOCITY_HISTORY_LENGTH;
        }
        
        XYPair total = new XYPair(0,0);
        
        if (sampleDecay.get() < 0)
        {
            Warning("Sample decay less than zero is very bad. Setting to a safer value.");
            sampleDecay.set(1.0);
        }
        
        for (int i = 0; i < VELOCITY_HISTORY_LENGTH; i++)
        {
            total.X += (arrayToAverage[i].X * weight);
            total.Y += (arrayToAverage[i].Y * weight);
                
            weight *= sampleDecay.get();
        }
        
        XYPair average = new XYPair(total.X / howManyToAverage, total.Y / howManyToAverage);
        return average;
    }
    
    
    
    /**
     * Resets all the internal follow wheels for both position and velocity.
     */
    public void ResetPositionAndVelocity()
    {
        leftWheel.ResetInternalDistance();
        rightWheel.ResetInternalDistance();
        centerWheel.ResetInternalDistance();
    }
    
    private void ShiftVelocity(XYPair[] velocityArray, int position)
    {
        // This would, for example, moves the velocity in position 6 to position 7.
        if ((position >= VELOCITY_HISTORY_LENGTH -1 ) || position < 0)
        {
            return;
        }
        velocityArray[position+1] = velocityArray[position];
    }
    
    private void ShiftAllVelocities(XYPair[] velocityArray)
    {
        // so it starts at i = 8 for a length of 10.
        // shiftVelocity of 8 would move 8 to 9 (which is the end of the array, since
        // 0 length arrays.
        for (int i = VELOCITY_HISTORY_LENGTH-2; i >=0; i--)
        {
            ShiftVelocity(velocityArray, i);
        }
    }
}
