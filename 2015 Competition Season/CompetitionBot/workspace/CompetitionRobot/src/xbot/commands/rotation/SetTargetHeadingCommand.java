package xbot.commands.rotation;

import org.apache.log4j.Logger;

import xbot.RobotMap;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.RotationalSubsystem;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;


public class SetTargetHeadingCommand extends BaseCommand {
    public enum Target {
        LEFT_HUMAN_STATION, RIGHT_HUMAN_STATION 
    }
    
    public DoubleProperty humanStationOffset;
    RotationalSubsystem rotationalSubsystem;
    private double targetHeading;
    
    private static final Logger log = Logger
            .getLogger(SetTargetHeadingCommand.class);
    
    @Inject
    public SetTargetHeadingCommand(RotationalSubsystem rotationalSubsystem, PropertyManager propMan) {        
        this.rotationalSubsystem = rotationalSubsystem;
        humanStationOffset = propMan.createProperty("Human loader station heading offset", 55d + 180d);
    }
    
    @AssistedInject
    public SetTargetHeadingCommand(@Assisted Double newTarget, RotationalSubsystem rotationalSubsystem, PropertyManager propMan) {
        this(rotationalSubsystem, propMan);
        this.targetHeading = newTarget;
    }
    
    @AssistedInject
    public SetTargetHeadingCommand(@Assisted Target target, RotationalSubsystem rotationalSubsystem, PropertyManager propMan) {
        this(rotationalSubsystem, propMan);
        if(target == Target.LEFT_HUMAN_STATION) {
            this.targetHeading = RobotMap.forwardAngle
                    + this.getHumanStationHeadingOffset();
        } else if (target == Target.RIGHT_HUMAN_STATION) {
            this.targetHeading = RobotMap.forwardAngle
                    - this.getHumanStationHeadingOffset();
        }
    }
    
    public void setTargetHeading(double newTarget) {
        targetHeading = newTarget;
    }
    
    @Override
    public void initialize() {
        log.info("Setting target heading to " + targetHeading);
        rotationalSubsystem.setHumanAssistTargetHeading(targetHeading);
    }

    @Override
    public void execute() {       
    }
    
    public boolean isFinished() {
        return true;
    }
    
    public double getHumanStationHeadingOffset() {
        return humanStationOffset.get();
    }
}