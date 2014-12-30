/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.systems.DriveCore;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;

/*
 *
 * @author Alex
 */


/**
 *
 * @author John
 */
public class DriveTankWithJoysticksWorker extends AerialWorkerBase {
    /**
     * Based on the current joystick values,
     * sets a desired heading for the robot at which the robot
     * will move fastest towards the direction on the field
     * pointed to by the joystick.
     */
    private final OrientForSpeedWorker orientForSpeed;
    
    /**
     * Maintains the desired heading for the robot, using a PID.
     */
    private final DriveHeadingMaintainer headingMaintainer;
    
    /**
     *
     */
    private DoubleProperty tankDriveDeadbandRightJoystick = new DoubleProperty("Deadband",.3);
    
    public DriveTankWithJoysticksWorker()
    {
        super("DriveTankWithJoysticksWorker");
        
        this.orientForSpeed = new OrientForSpeedWorker();
        this.headingMaintainer = new DriveHeadingMaintainer("Tank");
        this.headingMaintainer.Reset();
    }
    
    /**
     *
     */
    public void init() {
        Info("Is running tank drive.");
        
        this.orientForSpeed.init();
    }

    /**
     *
     */
    public void exec() {
        if(robot().GetDriveCore().tankFieldOriented.get()) {
            
            // If the rotation joystick is being actuated, use that. Otherwise use the heading maintainer.
            XYPair rightJoy = new XYPair(robot().GetOICore().getRightJoyX(), robot().GetOICore().getRightJoyY());
            double rotation = 0;
            
            if (rightJoy.internalAbsoluteSum() > tankDriveDeadbandRightJoystick.get())
            {
                rotation = DriveCore.remapMinusDeadband(rightJoy.X, tankDriveDeadbandRightJoystick.get());
                
                // Set the desired heading to the current heading so we don't thrash once the joystick is released.
                robot().GetDriveCore().SetDesiredHeading(robot().GetSensorCore().getCurrentHeading());
            }
            else
            {
                // We'll first get the joystick vector, and set a desired
                // heading for the robot which maximizes speed towards that vector.
                this.orientForSpeed.exec();

                // Get the heading we should drive the arcade at from the PID.
                // This value is the rotation speed that we pass to the arcade drive.
                rotation = this.headingMaintainer.MaintainHeading();
            }
            
            // Our translation speed should be the magnitude of the joystick vector,
            // projected onto the robot heading.
            // So when the robot is at 90 degrees to the joystick heading,
            // it rotates rather than translating.
            // Conversely, when the robot is exactly the same as the joystick
            // heading, the rotation speed should be 0 and the translation speed should be 1.
            // Magnitude will be capped at 1.
            double currentRobotHeading = robot().GetSensorCore().getCurrentHeading();
            
            XYPair robotOrientation = XYPair.fromPolarCoordinates(1, currentRobotHeading);
            
            double movement = robotOrientation.project(robot().GetOICore().getLeftJoyVector());
            if (movement > 1.0) {
                movement = 1.0;
            }
            else if (movement < -1.0) {
                movement = -1.0;
            }
            
            robot().GetDriveCore().arcadeDrive(movement, rotation);
            
        } else {
            robot().GetDriveCore().tankDrive(
            robot().GetOICore().getLeftJoyY(), 
            robot().GetOICore().getRightJoyY());
        }
                
    }
}
