/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.properties.DoubleProperty;
import xbot.common.PID;
import xbot.common.math.Angles;

/**
 *
 * @author John
 */
public class DriveHeadingMaintainer extends AerialWorkerBase {
    
    PID rotationalPid = new PID();
    DoubleProperty translationVelocityP;
    DoubleProperty translationVelocityI;
    DoubleProperty translationVelocityD;
    
    double outputForce = 0;
    
    public DriveHeadingMaintainer(String pidPropertyPrefix) {
        super("DriveHeadingMaintainer:" + pidPropertyPrefix);
        this.construct(pidPropertyPrefix);
    }
    /**
     *
     */
    public DriveHeadingMaintainer()
    {
        super("DriveHeadingMaintainer");
        this.construct("");
    }
    
    /**
     * Constructs the PID properties so that they are named
     * with the given prefix.
     * @param pidPropertyPrefix 
     */
    private void construct(String pidPropertyPrefix) {
        this.translationVelocityP =
                new DoubleProperty(pidPropertyPrefix + "rotationHeadingP", 0.02);
        this.translationVelocityI =
                new DoubleProperty(pidPropertyPrefix + "rotationHeadingI", 0);
        this.translationVelocityD =
                new DoubleProperty(pidPropertyPrefix + "rotationHeadingD", 0);
    }
    /**
     *
     */
    public void Reset()
    {
        rotationalPid.reset();
        outputForce = 0;
    }
    
    /**
     *
     * @param heading
     */
    public void SetSetpoint(double heading)
    {
        robot().GetDriveCore().SetDesiredHeading(heading);
    }
    
    /**
     *
     * @return
     */
    public double MaintainHeading()
    {
        // First, we need to calculate our error.        
        double currentHeading = robot().GetSensorCore().getCurrentHeading();
        double error = Angles.DifferenceBetweenDegrees(currentHeading, robot().GetDriveCore().GetDesiredHeading());
        
        // Let's think this through robot frame of reference.
        // If you're at 50 degrees, and your desired is 20, then the Angles
        // utility will return a -30 result. In our defined robot coordinate system,
        // negative values mean a turn to the right, which is consistent with
        // the result.
        
        // If we pass -30 as the process variable into a PID, where it is 
        // targeting 0 degrees, it will return some positive number if the gains
        // (P, I, and D) are above 0. Essemtially it will attempt to turn left.
        
        // Therefore, we need to do some inversion somewhere. I suggest negating
        // the "error" before handing it to the PID. That way the PID is already
        // returning the correct direction, and we'll just need to tune the magnitude
        // of its response.
        error = -error;
        
        // We want to be right on our heading, so our setpoint is 0, and our 
        // process variable is how many degrees we are away from our goal.
        return rotationalPid.calculate(
                0,
                error,
                translationVelocityP.get(),
                translationVelocityI.get(),
                translationVelocityD.get());
    }
    
    public boolean isOnTarget(double tolerance) {
        return this.rotationalPid.isOnTarget(tolerance);
    }
}
