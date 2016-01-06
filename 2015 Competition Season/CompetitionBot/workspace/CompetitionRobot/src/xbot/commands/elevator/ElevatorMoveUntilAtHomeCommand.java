package xbot.commands.elevator;

import org.apache.log4j.Logger;

import xbot.RobotMap;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.common.wpi_extensions.mechanism_wrappers.XAnalogInput;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSensorSubsystem;
import xbot.subsystems.ElevatorSubsystem;

import com.google.inject.Inject;

public class ElevatorMoveUntilAtHomeCommand extends BaseCommand {
    ElevatorSubsystem elevator;
    ElevatorSensorSubsystem elevatorSensors;
    ElevatorHeightChooserSubsystem elevatorHeightChooser;

    private DoubleProperty powerProp;

    private static final Logger log = Logger
            .getLogger(ElevatorMoveUntilAtHomeCommand.class);

    @Inject
    public ElevatorMoveUntilAtHomeCommand(
            ElevatorSubsystem elevator,
            ElevatorSensorSubsystem elevatorSensors,
            ElevatorHeightChooserSubsystem elevatorHeightChooser,
            RobotMap map,
            PropertyManager propMan) {
        
        powerProp = propMan.createProperty("Elevator calibration motor power", 0.3);
        
        this.elevator = elevator;
        this.elevatorSensors = elevatorSensors;
        this.elevatorHeightChooser = elevatorHeightChooser;
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
        // Needs to clear calibration, so it can go below zero;
        elevator.clearEncoderCalibration();
        elevator.setLocked(false);
        elevator.setManualPower(-powerProp.get());
    }
    

    @Override
    public void execute() {
        // Intentionally left blank
    }
    
    @Override
    public void end() {
        log.info("Detected arrival at home position");
        elevator.setManualPower(0d);
        elevatorHeightChooser.setTargetHeightToCurrentHeight();
    }

    @Override
    public boolean isFinished() {
        return elevatorSensors.isAtHomePosition();
    }
}