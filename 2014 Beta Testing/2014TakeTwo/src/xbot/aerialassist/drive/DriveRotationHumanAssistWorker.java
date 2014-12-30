/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.drive.DriveHeadingMaintainer;
import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.systems.DriveCore;
import xbot.common.CommonTools;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.BooleanProperty;

/**
 *
 * @author John
 */
public class DriveRotationHumanAssistWorker extends AerialWorkerBase {
    
    private double lastHumanInputTime;
    private boolean robotPermittedToMaintainHeading;
    private boolean robotReadyForAlignment;
    private boolean robotHasChosenAlignment;
    private DriveHeadingMaintainer headingMaintainer;
    DoubleProperty rotationalDeadband = 
            new DoubleProperty("rotationalDeadband", 0.1);
    DoubleProperty robotResumeRotationControlTime = 
            new DoubleProperty("robotResumeRotationControlTimeMS", 1000);
    BooleanProperty propEnableRotationPID =
            new BooleanProperty("RotationalPID Allowed", true);
    
    /**
     *
     */
    public DriveRotationHumanAssistWorker()
    {
        super("DriveRotationHumanAssist");
    }
    
    /**
     *
     * @param allowRobotToMaintainHeading
     */
    public void init(boolean allowRobotToMaintainHeading)
    {
        lastHumanInputTime = 0;
        headingMaintainer = new DriveHeadingMaintainer();
        robotPermittedToMaintainHeading = allowRobotToMaintainHeading;
        robotReadyForAlignment = false;
        robotHasChosenAlignment = false;
    }
    
    /**
     *
     */
    public void init()
    {
        init(false);
    }
    
    private boolean RotationalPIDAllowed()
    {
        return (propEnableRotationPID.get()) && (robotPermittedToMaintainHeading);
    }
    
    /**
     *
     * @return
     */
    public double CalculateRotationalPower()
    {
        double humanIntent = RobotContext.Get().GetOICore().getRightJoyX();
        
        // Very generally, the logic for robot-assisted rotation goes like this:
        // -- If the human is providing rotational input above a deadband, then
        //      translate that into a force and apply it directly.
        // -- If the human stops providing rotational input above a deadband,
        //      then stop applying new force for about 1 second
        // -- After that second, read the robot's current field orientation. This
        //      is now the setpoint for the rotational PID until the human uses
        //      the joysticks again.
        
        if (RotationalPIDAllowed() == false)
        {
            // If the robot isn't allowed to maintain heading, then we just immediately
            // return the joystick value from the human driver. All further code
            // is essentially bypassed.
            return humanIntent;
        }
        
        if (Math.abs(humanIntent) > rotationalDeadband.get())
        {
            // map human intent back to a full range of values (since otherwise we clip at deadband value
            humanIntent = DriveCore.remapMinusDeadband(humanIntent, rotationalDeadband.get());
            
            // A human wants to rotate the robot directly.
            // Take note of the current time.
            lastHumanInputTime = CommonTools.Get().time.GetMarker();
            robotReadyForAlignment = false;
            robotHasChosenAlignment = false;
            return humanIntent;
        }
        else if ((CommonTools.Get().time.GetElapsedMSecFromMarker(lastHumanInputTime) < robotResumeRotationControlTime.get()) 
                || robotReadyForAlignment == false
                || robot().GetSensorCore().headingWasResetRecently())
        {
            // The human no longer wants to control robot rotation.
            // Start tracking the robot's current angle. Once we're no longer waiting,
            // then the robot will attempt to hold that angle.
            
            headingMaintainer.SetSetpoint(robot().GetSensorCore().getCurrentHeading());
            headingMaintainer.Reset();
            robotReadyForAlignment = true;
            return 0;
        }
        else if (robotReadyForAlignment)
        {
            // The robot now has control of rotation.
            return headingMaintainer.MaintainHeading();
        }


        return 0;
    }
    
}
