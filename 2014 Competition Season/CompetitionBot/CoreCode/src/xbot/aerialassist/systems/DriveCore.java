/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.common.CommonTools;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 * The system responsible for moving the chassis around.
 * @author Alex
 */
public class DriveCore extends Loggable {
    
    public BooleanProperty tankFieldOriented = 
            new BooleanProperty("IsTankFieldOriented", false);
    public BooleanProperty mecanumFieldOriented = 
            new BooleanProperty("useFieldOrientedDrive", true);
    public BooleanProperty useTranslationVelocityPID = 
            new BooleanProperty("useTranslationVelocityPID", false);
    public DoubleProperty notifyOrientationTime =
            new DoubleProperty("notifyOrientationTime", 500);
    /**
     *
     */
    public DriveCore() {
        super("DriveCore");
    }
    
    private int numberOfMotors = 4;
    
    //<editor-fold defaultstate="collapsed" desc="properties">
    private boolean isRunningMecanum = true;
    
    /**
     *
     * @return
     */
    public boolean getIsMecanum()
    {
        return isRunningMecanum;
    }
    
    /**
     *
     * @param value
     */
    public void setIsMecanum(boolean value)
    {
        if(isRunningMecanum != value) {
            Info("isRunningMecanum = " + value);
        }
        isRunningMecanum = value;
    }
    
    private double leftMotorFront;

    /**
     * Get the value of LeftMotor1
     *
     * @return the value of LeftMotor1
     */
    public double getLeftMotorFront() {
        return leftMotorFront;
    }
    
    private double leftMotorRear;

    /**
     * Get the value of leftMotorRear
     *
     * @return the value of leftMotorRear
     */
    public double getLeftMotorRear() {
        return leftMotorRear;
    }

    private double rightMotorFront;

    /**
     * Get the value of rightMotorFront
     *
     * @return the value of rightMotorFront
     */
    public double getRightMotorFront() {
        return rightMotorFront;
    }

    private double rightMotorRear;

    /**
     * Get the value of rightMotorRear
     *
     * @return the value of rightMotorRear
     */
    public double getRightMotorRear() {
        return rightMotorRear;
    }
    
    // </editor-fold>
    
    double lastNotifyTime = -1000;
    private void setDesiredHeadingSetByCommand()
    {
        Info("SetDesiredHeadingSetByCommand called");
        lastNotifyTime = CommonTools.Get().time.GetMarker();
    }
    
    public boolean shouldIgnoreJoystickWhenRotating()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastNotifyTime) < notifyOrientationTime.get())
        {
            return true;
        }
        return false;
    }
    
    public void SetDesiredHeading(double heading, boolean ignoreJoystickForABit)
    {
        SetDesiredHeading(heading);
        if (ignoreJoystickForABit)
        {
            setDesiredHeadingSetByCommand();
        }
    }
    
    double lastHeadingChangedTime = -1000;
    double internalDesiredHeading = 0;
    public void SetDesiredHeading(double heading)
    {
        // massave the heading if invalid
        heading = heading % 360;
        if (heading < 0)
        {
            heading += 360;
        }
        
        Info("New DesiredHeading is: " + heading);
        lastHeadingChangedTime = CommonTools.Get().time.GetMarker();
        internalDesiredHeading = heading;
    }
    
    public boolean DesiredHeadingWasChangedRecently()
    {
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastHeadingChangedTime) < 1000)
        {
            return true;
        }
        return false;
    }
    
    public double GetDesiredHeading()
    {
        return internalDesiredHeading;
    }
    
    private double desiredRotationalForce = 0;
    public void SetDesiredRotationalForce(double force)
    {
        desiredRotationalForce = force;
    }
    
    public double GetDesiredRotationalForce()
    {
        return desiredRotationalForce;
    }

    /**
     *
     * @param frontLeft Power to the front left motor
     * @param frontRight    Power to the front right motor
     * @param rearLeft  Power to the rear left motor
     * @param rearRight Power to the rear right motor
     */
    public void setDriveMotors(double frontLeft, double frontRight, double rearLeft, double rearRight)
    {
        leftMotorFront = frontLeft;
        rightMotorFront = frontRight;
        leftMotorRear = rearLeft;
        rightMotorRear = rearRight;
    }
    
    /**
     *
     * @param left  Left (robot-oriented) tank power 
     * @param right Right (robot-oriented) tank power
     */
    public void tankDrive(double left, double right) {
        setIsMecanum(false);
        // TODO: need to do some bounds checking and the life
        leftMotorFront = left;
        leftMotorRear = left;
        
        rightMotorFront = right;
        rightMotorRear = right;
        
    }
    
    public void arcadeDrive(double moveValue, double rotateValue) {
        setIsMecanum(false);
        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue = limit(moveValue);

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        leftMotorFront = leftMotorSpeed;
        leftMotorRear = leftMotorSpeed;
        rightMotorFront = rightMotorSpeed;
        rightMotorRear = rightMotorSpeed;
    }
    
    public void stop() {
         leftMotorFront = 0;
        leftMotorRear = 0;
        rightMotorFront = 0;
        rightMotorRear = 0;
    }
    
    /**
     * Limit motor values to the -1.0 to +1.0 range.
     */
    protected static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }
    
    /**  * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle)
    {
        setIsMecanum(true);
        //Info("mecanumDrive_Cartesian " + x + " " + y + " " + rotation);
        double gyroAngleIn = -(gyroAngle - 90);
        //Subtract 90 since mecanum math assumes the positive x-axis is 0 degrees
        //and gyroAngle outputs 90 for positive y-axis.
        
        // Compenstate for gyro angle for field oriented controls
        XYPair rotated = CommonUtils.rotateVector(x, y, gyroAngleIn);
        double xIn = rotated.X;
        double yIn = rotated.Y;

        double wheelSpeeds[] = new double[numberOfMotors];
        //Intermediary variable for normalization of motor speeds
        // Based on Ether's paper www.chiefdelphi.com/media/papers/download/2722?, I'm
        // pretty sure the previous equations were incorrect. I have updated the values accordingly.
        
        wheelSpeeds[0] =  xIn + yIn - rotation; // Left Front Motor //1
        wheelSpeeds[1] = -xIn + yIn - rotation; //Left Rear Motor   //-1
        wheelSpeeds[2] = -xIn + yIn + rotation; //Right Front Motor //-1
        wheelSpeeds[3] =  xIn + yIn + rotation; //Right Rear Motor  //1

        //Normalizing the wheelspeeds if any are greater than 1
        //so that motor values are [-1 1]

        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        int i;
        for (i=1; i<numberOfMotors; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) maxMagnitude = temp;
        }
        if (maxMagnitude > 1.0) {
            for (i=0; i<numberOfMotors; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
        
        //After normalizing, assign motor values.
        leftMotorFront = wheelSpeeds[0];
        leftMotorRear = wheelSpeeds[1];
        rightMotorFront = wheelSpeeds[2];
        rightMotorRear = wheelSpeeds[3];
//        Info("Setting motor values " + leftMotorFront + " " + leftMotorRear 
//         + " " + rightMotorFront + " " + rightMotorRear);
    }
   
}
