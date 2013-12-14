/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.ActualRobot;

import xbot.core.RobotContext;
import xbot.core.systems.BucketCore;
import xbot.core.systems.CollectorCore;
import xbot.core.systems.DriveCore;
import xbot.core.systems.OICore;
import xbot.core.systems.ShooterCore;

/**
 *
 * @author John
 */
public class RoboAdapter {
    
    private static RoboAdapter instance;
    
    protected RoboAdapter()
    {
        //just to make sure people don't accidentially initialize the object directly.
    }
    
    public static RoboAdapter GetInstance()
    {
        if (instance == null)
        {
            instance = new RoboAdapter();
        }
        
        return instance;
    }
    
    public void UpdateNormal()
    {
        //here, we grab the RobotContext instance and assign values to the motors.
        //drive system
        DriveCore drive = RobotContext.Get().GetDriveCore();
        BucketCore bucket = RobotContext.Get().GetBucketCore();
        CollectorCore collector = RobotContext.Get().getCollectorCore();
        ShooterCore shooter = RobotContext.Get().getShooterCore();
        OICore oi = RobotContext.Get().GetOICore();
        
        //Drive
        RobotMap.actuatorsLeftFrontController.set(drive.getLeftMotor1());
        RobotMap.actuatorsLeftRearController.set(drive.getLeftMotor2());
        RobotMap.actuatorsRightFrontController.set(drive.getRightMotor1());
        RobotMap.actuatorsRightRearController.set(drive.getRightMotor2());
        
        //Bucket
        RobotMap.actuatorsBucketSolenoid.set(bucket.getRequestSolenoidUp());
        bucket.setDeviceBucketDownLimitSwitchPressed(RobotMap.sensorsBucketDownLimitSwitch.get());
        bucket.setDeviceBucketUpLimitSwitchPressed(RobotMap.sensorsBucketUpLimitSwitch.get());
        
        //Collector
        RobotMap.actuatorsCollectorSpeedController.set(collector.getRequestedDeviceMotorSpeed());
        
        //Shooter
        RobotMap.actuatorsInnerShooterSpeedController.set(shooter.getInnerShooterWheelSpeed());
        RobotMap.actuatorsOuterShooterSpeedController.set(shooter.getOuterShooterWheelSpeed());
        RobotMap.actuatorsFingerSolenoid.set(shooter.isDesiredFingerSolonoidExtended());
        shooter.setOuterShooterEncoderRate(RobotMap.sensorsOuterShooterWheelTach.getRPM());
        
        //make the compressor go?
        RobotMap.actuatorsCompressor.start();
        
        //now, we grab all our "sensors" and publish them to the Context.
        //  Right now, that's just the OI joysticks.
        oi.setLeftJoyY(Robot.oi.leftJoystick.getY());
        oi.setRightJoyY(Robot.oi.rightJoystick.getY());        
    }
}
