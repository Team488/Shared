/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.properties.DoubleProperty;
import xbot.common.PIDArray;
import xbot.common.math.XYPair;

/**
 *
 * @author Alex
 */
public class DriveTranslationVelocityWorker extends AerialWorkerBase {
    
    PIDArray pidArray = new PIDArray();
    
    DoubleProperty translationVelocityP = 
            new DoubleProperty("translationVelocityP", 1.0);
    DoubleProperty translationVelocityI = 
            new DoubleProperty("translationVelocityI", 1.0);
    DoubleProperty translationVelocityD = 
            new DoubleProperty("translationVelocityD", 1.0);
    
    double[] outputForce = new double[2];
    
    /**
     *
     */
    public DriveTranslationVelocityWorker()
    {
        super("DriveTranslationVelocityWorker");
    }
    
    /**
     *
     */
    public void init() {
        // reset to 0 as starting point
        outputForce = new double[2];
    }
    
    /**
     *
     * @param velocityIntent
     * @param fieldOriented
     * @return
     */
    public double[] exec(double[] velocityIntent, boolean fieldOriented) {
        
        XYPair velocity = new XYPair();
        
        if (fieldOriented)
        {
            velocity = robot().GetSensorCore().odometry.GetFieldOrientedVelocity(false);
        }
        else
        {
            // robot oriented!
            velocity = robot().GetSensorCore().odometry.GetRobotOrientedVelocity(false);
        }
        
        double[] currentVelocity = new double[] { velocity.X, velocity.Y };
        // TODO: populate this from the field oriented information
        // TODO: depending on the value of fieldRelativeSpeed, either get
        // the speed of the robot relative to itself, or relative to the field.
        
        double[] deltaForce = pidArray.calculate(velocityIntent, 
                currentVelocity,
                translationVelocityP.get(), 
                translationVelocityI.get(), 
                translationVelocityD.get());
        
        // integrate deltaForce with previous value
        outputForce[0] += deltaForce[0];
        outputForce[1] += deltaForce[1];
        
        return outputForce;
    }
}
