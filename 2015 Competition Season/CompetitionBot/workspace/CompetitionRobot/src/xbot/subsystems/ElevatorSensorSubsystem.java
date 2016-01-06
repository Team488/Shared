package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import xbot.RobotMap;
import xbot.common.controls.XGyro;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XPowerDistributionPanel;
import xbot.common.wpi_extensions.mechanism_wrappers.XAnalogInput;

@Singleton
public class ElevatorSensorSubsystem extends BaseSubsystem {

    private static Logger log = Logger.getLogger(ElevatorSensorSubsystem.class);

    // TODO: Move all elevator sensors over here

    private XGyro gyro;
    private DoubleProperty tiltThreshold;
    private XPowerDistributionPanel pdp;
    private XAnalogInput homeSensor;

    private DoubleProperty elevatorMotorAChannel;
    private DoubleProperty elevatorMotorBChannel;

    @Inject
    public ElevatorSensorSubsystem(RobotMap map, PropertyManager propMan) {
        log.info("Creating ElevatorSensorSubsystem");

        tiltThreshold = propMan.createProperty(
                "Elevator tilt danger threshold", 25d);

        // TODO: Make IntegerProperty
        elevatorMotorAChannel = propMan.createProperty(
                "Elevator motor A PDP channel", 0d);
        elevatorMotorBChannel = propMan.createProperty(
                "Elevator motor B PDP channel", 15d);

        pdp = map.pdp;
        gyro = map.gyro;
        homeSensor = map.homePositionSensor;
    }
    

    public boolean isTiltInDangerZone() {
        return Math.abs(gyro.getPitch()) > tiltThreshold.get()
                || Math.abs(gyro.getRoll()) > tiltThreshold.get();
    }

    // My philosophy -- "Use sensors to collect data about the surrounding environment,
    // then discard it and drive into walls."
    
    public double getElevatorMotorACurrent() {
        return pdp.getCurrent((int) elevatorMotorAChannel.get());
    }

    public double getElevatorMotorBCurrent() {
        return pdp.getCurrent((int) elevatorMotorBChannel.get());
    }

    public double getAverageElevatorMotorCurrent() {
        return (getElevatorMotorACurrent() + getElevatorMotorBCurrent()) / 2d;
    }

    public boolean isAtHomePosition() {
        return !homeSensor.getAsDigital(3.3d);
    }
}
