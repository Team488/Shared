package xbot.subsystems;

import org.apache.log4j.Logger;

import xbot.RobotMap;
import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DriveSubsystem extends BaseSubsystem
{

    private static Logger log = Logger.getLogger(DriveSubsystem.class);
    
    public XSpeedController leftFrontDrive; //Motor 0
    public XSpeedController leftRearDrive; //Motor 1
    public XSpeedController rightFrontDrive; //Motor 2
    public XSpeedController rightRearDrive; //Motor 3
    
    private DoubleProperty leftFrontSpeedProp;
    private DoubleProperty leftRearSpeedProp;
    private DoubleProperty rightFrontSpeedProp;
    private DoubleProperty rightRearSpeedProp;
    
    private double rotationalPower = 0;
    private XYPair translationalVector = new XYPair(0, 0);
    
    @Inject
    public DriveSubsystem(WPIFactory factory, PropertyManager propManager)
    {
        log.info("Creating DriveSubsystem");

        this.leftFrontDrive = factory.getSpeedController(0);
        this.leftRearDrive = factory.getSpeedController(2);
        
        this.rightFrontDrive = factory.getSpeedController(1);
        this.rightFrontDrive.setInverted(true);
        this.rightRearDrive = factory.getSpeedController(3);
        this.rightRearDrive.setInverted(true);
        
        this.leftFrontSpeedProp = new DoubleProperty("Left front power", 0, propManager);
        this.leftRearSpeedProp = new DoubleProperty("Left rear power", 0, propManager);
        this.rightFrontSpeedProp = new DoubleProperty("Right front power", 0, propManager);
        this.rightRearSpeedProp = new DoubleProperty("Right rear power", 0, propManager);
    }
    
    /**
     * Runs the motors using the specified robot-relative vector and angular power.
     * Best to use one of the wrapper methods instead of this one explicitly.
     * @param translationVector Robot-relative translational vector
     * @param angularPower Angular rotation power
     */
    public void robotRelativeMecanumDrive(XYPair translationVector, double angularPower)
    {        
        double transX = MathUtils.constrainDoubleToRobotScale(translationVector.x);
        double transY = MathUtils.constrainDoubleToRobotScale(translationVector.y);
        double cappedAngularPower = MathUtils.constrainDoubleToRobotScale(angularPower);
        
        //Calculate wheel power values
        double[] wheelPowers = {
                transX + transY - cappedAngularPower, // left front
                -transX + transY - cappedAngularPower, // left rear
                -transX + transY + cappedAngularPower, // right front
                transX + transY + cappedAngularPower // right rear
        };
        
        leftFrontSpeedProp.set(wheelPowers[0]);
        leftRearSpeedProp.set(wheelPowers[1]);
        rightFrontSpeedProp.set(wheelPowers[2]);
        rightRearSpeedProp.set(wheelPowers[3]);
        
        this.leftFrontDrive.set(wheelPowers[0]);
        this.leftRearDrive.set(wheelPowers[1]);
        this.rightFrontDrive.set(wheelPowers[2]);
        this.rightRearDrive.set(wheelPowers[3]);
    }
    
    /**
     * Sets the rotational power being sent to the mecanum drive
     * @param power
     */
    public void setRotationalPower(double power)
    {
        this.rotationalPower = power;
        updateDriveOutput();
    }
    
    /**
     * Sets the robot-relative vector that is being fed to the mecanum drive
     * @param newVector
     */
    public void setTranslatonalVector(XYPair newVector)
    {
        this.translationalVector = newVector;
        updateDriveOutput();
    }
    
    private void updateDriveOutput()
    {
        robotRelativeMecanumDrive(this.translationalVector, this.rotationalPower);        
    }    
}
