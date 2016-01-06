package xbot.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.controls.DistanceSensor;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;

@Singleton
public class ArmWheelSubsystem extends BaseSubsystem {

    private XSpeedController motorArmLeft;
    private XSpeedController motorArmRight;
    private DoubleProperty collectorPower;

    private DistanceSensor leftSensor;
    private DistanceSensor rightSensor;
    private DoubleProperty toteDistanceThreshold;

    private DoubleProperty distanceBetweenSensors;

    private DoubleProperty analogDistanceMinReliable;
    private DoubleProperty analogDistanceMaxReliable;

    @Inject
    public ArmWheelSubsystem(RobotMap map, PropertyManager propMan) {
        this.motorArmLeft = map.intakeArmLeftWheel;
        this.motorArmRight = map.intakeArmRightWheel;

        this.leftSensor = map.frontLeftDistanceSensor;
        this.rightSensor = map.frontRightDistanceSensor;

        collectorPower = propMan.createProperty("CollectorPower", 1d);
        toteDistanceThreshold = propMan.createProperty(
                "Collector tote distance threshold", 2.5d);
        distanceBetweenSensors = propMan.createProperty(
                "Collector distance between sensors (in)", 11d);

        analogDistanceMinReliable = propMan.createProperty(
                "Collector sensor minimum reliable distance (in)", 0.8d);
        analogDistanceMaxReliable = propMan.createProperty(
                "Collector sensor maximum reliable distance (in)", 6d);
    }

    public void armWheelsPowerLeft(double valueLeft) {
        motorArmLeft.set(valueLeft);
    }

    public void armWheelsPowerRight(double valueRight) {
        motorArmRight.set(valueRight);
    }

    public double getCollectorPower() {
        return collectorPower.get();
    }

    public void setCollectorPower(double collectorPower) {
        setCollectorPower(collectorPower);
    }

    public double getLeftDistance() {
        return leftSensor.getDistance();
    }

    public double getRightDistance() {
        return rightSensor.getDistance();
    }

    public boolean isToteInPosition() {
        return getLeftDistance() <= toteDistanceThreshold.get()
                && getRightDistance() <= toteDistanceThreshold.get();
    }

    public double getToteAngle()
    {
        double left = getLeftDistance();
        double right = getRightDistance();
        
        if(!isToteWithinDetectionRange())
        {
            return 0;
        }
        
        double angle = Math.atan2(right - left, distanceBetweenSensors.get()); 
        return Math.toDegrees(angle);
    }

    public boolean isToteWithinDetectionRange()
    {
        double left = getLeftDistance();
        double right = getRightDistance();
    
        return left > analogDistanceMinReliable.get() 
                && left < analogDistanceMaxReliable.get()
                && right > analogDistanceMinReliable.get() 
                && right < analogDistanceMaxReliable.get();
                
    }
}
