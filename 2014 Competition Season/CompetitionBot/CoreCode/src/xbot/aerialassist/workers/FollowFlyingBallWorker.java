/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.PIDArray;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class FollowFlyingBallWorker extends AerialWorkerBase {
    
    private PIDArray followPid;
    
    DoubleProperty FlyingBallP = 
            new DoubleProperty("FlyingBallP", 0.1);
    DoubleProperty FlyingBallI = 
            new DoubleProperty("FlyingBallI", 0.0);
    DoubleProperty FlyingBallD = 
            new DoubleProperty("FlyingBallD", 0.0);
    
    /**
     *
     */
    public FollowFlyingBallWorker()
    {
        super("FollowFlyingBallWorker");
    }
    
    /**
     *
     */
    public void init()
    {
        followPid = new PIDArray(2);
        Info("followPid = " + followPid);
    }
    
    /**
     *
     */
    public void exec() {
        
        // pull data from vision
        double[] xyScaledDisplacement = robot().GetVisionCore().GetBallDisplacementFromCenter();
        
        // need to invert the X goal, so the robot heads towards the ball (not away)
        // for example: if the ball is to the left (negative), the robot needs to move to the left (negative)
        // if the ball is high (negative, since it is already inverted by visionCore), the robot needs to move back (negative)
        
        xyScaledDisplacement[0] = -xyScaledDisplacement[0];
        xyScaledDisplacement[1] = -xyScaledDisplacement[1];
        
        // give data to drive
        // goal, current, pid
        double[] goal = new double[] { 0, 0};
        double[] power = followPid.calculate(
                goal, 
                xyScaledDisplacement,
                FlyingBallP.get(),
                FlyingBallI.get(),
                FlyingBallD.get());
        
        // use PID? or just raw driving.
        // Don't use field-oriented drive here - too many frames of reference would need to change.
        //  instead, just use robot-relative drive, since the location of the ball is robot-relative.
        robot().GetDriveCore().mecanumDrive_Cartesian(power[0], power[1], 0, robot().GetSensorCore().GetDefaultOrientation());
        //Verbose("Robot followed ball");
    }
    
}
