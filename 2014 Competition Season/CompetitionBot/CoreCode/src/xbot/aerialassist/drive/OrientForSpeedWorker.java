/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.math.Angles;
import xbot.common.math.XYPair;

/**
 *
 * @author John
 */
public class OrientForSpeedWorker extends AerialWorkerBase {
    
    public OrientForSpeedWorker()
    {
        super("OrientForSpeedWorker");
    }
    
    public void init()
    {
        Info("Initializing");
    }
    
    public void exec()
    {
        // Read the joystick values and turn that into a degree orientation
        XYPair joystickVector = robot().GetOICore().getLeftJoyVector();
        
        if (joystickVector.internalAbsoluteSum() < 0.1)
        {
            // If no joystick motion, don't mess with anything.
            return;
        }
        
        double joystickDirection = joystickVector.toDegrees();
        
        // Read the current orientation of the robot
        double currentDirection = robot().GetSensorCore().getCurrentHeading();
        
        // Now, if the difference between degrees is less than 180, set desired heading directly.
        // Else, if the difference is greater than 180, set the heading to the opposite direction.
        // This is to make sure the robot quickly moves to an orientation that will get it in the "fast orientation"
        // as quickly as possible - it's not important if the Front or Back of the robot is in the direction of travel.
        
        double potentialNewHeading = joystickDirection;

        double smallestDiff = Math.abs(Angles.DifferenceBetweenDegrees(currentDirection, joystickDirection));
        
        if (smallestDiff > 90)
        {
            potentialNewHeading += 180;
            potentialNewHeading = potentialNewHeading % 360;
        }
        
        robot().GetDriveCore().SetDesiredHeading(potentialNewHeading);
    }
}
