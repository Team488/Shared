/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.odometry;

import xbot.common.CommonTools;
import xbot.common.Loggable;
import xbot.common.math.SimpleMath;
import xbot.common.properties.DoubleProperty;

/**
 * This class encapsulates a lot of the odometry logic for a single follow wheel.
 * @author John
 */
public class WheelEncoder extends Loggable {
    
    private double lastUpdateTime;
    private double distanceTraveled;
    private double lastTicksFromRobot;
    private double timeSpanInMs;
    private double deltaTicksFromRobot;
    private double[] velocityHistory;
    private final int VELOCITY_HISTORY_LENGTH = 10;
    private DoubleProperty gain;
    
    private DoubleProperty wheelCircumference = 
            new DoubleProperty("WheelCircumference", 1.0);
    private DoubleProperty ticksPerRevolution = 
            new DoubleProperty("TicksPerWheelEncoderRevolution", 360.0);
    
    private DoubleProperty ticks;
    
    /**
     *
     * @param whichWheel Left, Right, or Center
     * @param gain This is used to fine-tune the wheel. Hopefully it should not need to be used if our calculations are correct.
     */
    public WheelEncoder(String whichWheel, DoubleProperty gain)
    {
        super("WheelEncoder" + whichWheel);
        velocityHistory = new double[VELOCITY_HISTORY_LENGTH];
        lastUpdateTime = 0;
        distanceTraveled = 0;
        lastTicksFromRobot = 0;
        deltaTicksFromRobot = 0;
        timeSpanInMs = 1;
        this.gain = gain;
        ticks = new DoubleProperty(whichWheel+"Ticks", 0.0);
    }
    
    /**
     * Resets the internal distance held by this Wheel encoder object
     */
    public void ResetInternalDistance()
    {
        distanceTraveled = 0;
        velocityHistory = new double[VELOCITY_HISTORY_LENGTH];
    }
    
    // Encoders measure a certian number of "ticks." We'll need to change
    // those into deltas (changes since last update) to determine things like
    // velocity.
    /**
     *
     * @param newTicksFromRobot The total number of ticks that this wheel encoder has seen during the match (or since it was last reset).
     * @return
     */
    public double UpdateDistance(double newTicksFromRobot)
    {
        // Figure out how long it has been since this was last called
        // UpdateTimeSlice();
        ticks.set(newTicksFromRobot);
        
        // Calculate how many new ticks we got from the robot for this encoder
        deltaTicksFromRobot = newTicksFromRobot - lastTicksFromRobot;        
        lastTicksFromRobot = newTicksFromRobot;
        
        // account for gain
        deltaTicksFromRobot *= gain.get();
        
        // Turn ticks into Distance and Velocity using properties and time
        double distance = DetermineDistance(deltaTicksFromRobot);
        return distance;
        
        /*double velocity = DetermineVelocity(distance);
        
        // Update total distance traveled
        distanceTraveled += distance;
        // Store the velocity
        RememberVelocity(velocity);*/
    }
    
    private double DetermineDistance(double ticks)
    {
        double distance = (ticks / ticksPerRevolution.get()) * wheelCircumference.get();        
        return distance;
    }
    
    private double DetermineVelocity(double distance)
    {
        double velocity = distance / (timeSpanInMs / 1000);
        return velocity;
    }
    
    private void UpdateTimeSlice()
    {
        double currentUpdateTime = CommonTools.Get().time.GetMarker();
        timeSpanInMs = currentUpdateTime - lastUpdateTime;
        lastUpdateTime = CommonTools.Get().time.GetMarker();
    }
    
    /**
     * Get the total distance traveled by this encoder (not useful in most circumstances)
     * @return
     */
    public double GetDistance()
    {
        return distanceTraveled;
    }
    
    /**
     *
     * @return The instantaneous velocity of this wheel
     */
    public double GetInstantVelocity()
    {
        // TODO: return in terms of actual speed by multiplying by wheel circumference and dividing by timeslice.
        return velocityHistory[0];
    }
    
    /**
     *
     * @param howManyToAverage  The number of samples to average (typically around 5)
     * @param decay Whether or not to use a decaying average (more recent samples are more important)
     * @return
     */
    public double GetAverageVelocity(double howManyToAverage, boolean decay)
    {
        double weight = 1;
        
        if (howManyToAverage > VELOCITY_HISTORY_LENGTH)
        {
            Warning("Was asked to average "+ howManyToAverage + " samples, but this exceeds the available number of samples: " + VELOCITY_HISTORY_LENGTH);
            howManyToAverage = VELOCITY_HISTORY_LENGTH;
        }
        
        double total = 0;
        
        for (int i = 0; i < VELOCITY_HISTORY_LENGTH; i++)
        {
            total += velocityHistory[i] * weight;
            
            if (decay)
            {
                weight *= 0.5;
            }
        }
        // if we use decay, we have to divide by a different number.
        // sum of a finite geometric series: a((1-r^n)/(1-r))
        // a = 1, r = 1/2
        if (decay)
        {
            howManyToAverage = (1 - SimpleMath.XtotheY(0.5, VELOCITY_HISTORY_LENGTH)) / (1-.05);
        }
        
        double average = total / howManyToAverage;
        
        return average;
    }
    
    private void RememberVelocity(double velocityToRemember)
    {
        ShiftAllVelocities();
        velocityHistory[0] = velocityToRemember;
    }
    
    private void ShiftVelocity(int position)
    {
        // This would, for example, moves the velocity in position 6 to position 7.
        if ((position >= VELOCITY_HISTORY_LENGTH -1 ) || position < 0)
        {
            return;
        }
        velocityHistory[position+1] = velocityHistory[position];
    }
    
    private void ShiftAllVelocities()
    {
        // so it starts at i = 8 for a length of 10.
        // shiftVelocity of 8 would move 8 to 9 (which is the end of the array, since
        // 0 length arrays.
        for (int i = VELOCITY_HISTORY_LENGTH-2; i >=0; i--)
        {
            ShiftVelocity(i);
        }
    }
}
