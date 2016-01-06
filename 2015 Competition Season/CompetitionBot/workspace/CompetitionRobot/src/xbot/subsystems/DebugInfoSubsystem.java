package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;

@Singleton
public class DebugInfoSubsystem extends BaseSubsystem
{
    private DoubleProperty gyroProp;
    private DoubleProperty frontLeftDistanceProp;
    private DoubleProperty frontRightDistanceProp;
    private BooleanProperty isInPositionProp;
    private DoubleProperty angleProp;
    private DoubleProperty powerProp;
    
    private RotationalSubsystem rotation;
    private ElevatorSubsystem elevator;
    private ElevatorSensorSubsystem elevatorSensors;
    private TranslationalMeasurementSubsystem translation;
    private ArmWheelSubsystem collector;
    private AutonomousSubsystem autoSub;
    
    private static final Logger log = Logger
			.getLogger(DebugInfoSubsystem.class);
    
    @Inject
    public DebugInfoSubsystem(
    		RobotMap map, 
    		RotationalSubsystem rotation,
    		ElevatorSubsystem elevator,
    		ElevatorSensorSubsystem elevatorSensors,
    		TranslationalMeasurementSubsystem translation,
    		ArmWheelSubsystem collector,
    		AutonomousSubsystem autoSub,
    		PropertyManager propMan)
    {
    	log.info("Creating DebugInfoSubsystem");
    	
        gyroProp = propMan.createProperty("Current yaw", 0d);
        isInPositionProp = propMan.createProperty("Is tote in position", false);
        angleProp = propMan.createProperty("Collector tote angle", 0d);
        powerProp = propMan.createProperty("Elevator average motor current (A)", 0d);
        
        frontLeftDistanceProp = propMan.createProperty("Front left distance", 0d);
        frontRightDistanceProp = propMan.createProperty("Front right distance", 0d);
        
        this.translation = translation;        
        
        this.rotation = rotation;
        this.elevator = elevator;
        this.collector = collector;
        this.elevatorSensors = elevatorSensors;
        this.autoSub = autoSub;
    }
    
    public void updateGyro()
    {
        gyroProp.set(rotation.getGyroYaw().getValue());
    }
    
    public void updateTranslationalMeasurements()
    {
        translation.updateTrackingState();
    }
    
    public void updateDistanceSensors()
    {
    	frontLeftDistanceProp.set(collector.getLeftDistance());
    	frontRightDistanceProp.set(collector.getRightDistance());
    	isInPositionProp.set(collector.isToteInPosition());
    	angleProp.set(collector.getToteAngle());
    }
    
    public void updateElevatorSensors()
    {
    	elevator.updateAndGetHeight();
    }
    
    public void updatePowerPanel()
    {
        powerProp.set(elevatorSensors.getAverageElevatorMotorCurrent());
    }
    
    public void updateAutoSwitch() {
        autoSub.updateAutoSwitch();
    }
}
