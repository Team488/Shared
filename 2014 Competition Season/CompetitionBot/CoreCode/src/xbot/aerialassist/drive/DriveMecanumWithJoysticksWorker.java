/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.drive.RotationForceProviderWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class DriveMecanumWithJoysticksWorker extends AerialWorkerBase {
    DriveTranslationVelocityWorker translationWorker;
    RotationForceProviderWorker rotationProvider;
    //private boolean isFieldOriented;
    
    DoubleProperty topSpeedFtPerS = new DoubleProperty("topSpeedFtPerS", 15);
//    BooleanProperty useTranslationVelocityPID = 
//            new BooleanProperty("useTranslationVelocityPID", false);
//    BooleanProperty useFieldOrientedDrive = 
//            new BooleanProperty("useFieldOrientedDrive", true);
    
    /**
     * 
     */
    public DriveMecanumWithJoysticksWorker() {
        super("DriveMecanumWithJoysticksWorker");
      //  isFieldOriented = fieldOriented;
    }
    
    /**
     *
     */
    public void init() {
        translationWorker = new DriveTranslationVelocityWorker();
        rotationProvider = new RotationForceProviderWorker();
        rotationProvider.init();
        Important("is running Mecanum drive");
    }
    
    /**
     *
     */
    public void exec() {
        double[] translationForces = new double[2];
        if(robot().GetDriveCore().useTranslationVelocityPID.get()) {
            // velocity PID!
            translationForces = GetPidTranslationForces();
        } else {
            // Just give power to wheels.
            translationForces[0] = robot().GetOICore().getLeftJoyX();
            translationForces[1] = robot().GetOICore().getLeftJoyY();  
        }
        // TODO: Add rotation
        //double rotationForce = rotationWorker.CalculateRotationalPower();
        rotationProvider.exec();
        Drive(translationForces);
       
    }
    
    private double[] GetPidTranslationForces()
    {
        double[] translationForces;
        double[] velocityIntent = new double[2];
        // calculate desired velocity by taking joystick values * top speed
        velocityIntent[0] = robot().GetOICore().getLeftJoyX() * topSpeedFtPerS.get();
        velocityIntent[1] = robot().GetOICore().getLeftJoyY() * topSpeedFtPerS.get();
        
        translationForces = translationWorker.exec(velocityIntent, robot().GetDriveCore().mecanumFieldOriented.get());
        return translationForces;                
    }
    
    private void Drive(double[] translationForces)
    {
        double heading = robot().GetSensorCore().getCurrentHeading();
        
        if (!robot().GetDriveCore().mecanumFieldOriented.get())
        {
            heading = robot().GetSensorCore().GetDefaultOrientation(); //CommonUtils.GetDefaultOrientation();
        }
        
        robot().GetDriveCore().mecanumDrive_Cartesian(
                translationForces[0], translationForces[1], 
                robot().GetDriveCore().GetDesiredRotationalForce(), heading);
    }
}
